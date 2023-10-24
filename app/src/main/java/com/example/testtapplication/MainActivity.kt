package com.example.testtapplication

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.testtapplication.view.TicketAdapter
import com.example.testtapplication.viewModel.TicketViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Locale

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: TicketViewModel
    private lateinit var adapter: TicketAdapter


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val startDateEditText = findViewById<EditText>(R.id.startDate)
        val endDateEditText = findViewById<EditText>(R.id.endDate)
        val searchButton = findViewById<Button>(R.id.searchButton)
        val recyclerView = findViewById<RecyclerView>(R.id.recyclerView)

        adapter = TicketAdapter(emptyList()) // Create an instance of the adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
        recyclerView.adapter = adapter

        viewModel = ViewModelProvider(this).get(TicketViewModel::class.java)
        viewModel.ticketTypesLiveData.observe(this, Observer { ticketTypes ->
            adapter.submitList(ticketTypes)
        })
        val noDataToast = Toast.makeText(this, "NO DATA", Toast.LENGTH_SHORT)
        viewModel.ticketTypesLiveData.observe(this, Observer { ticketTypes ->
            adapter.submitList(ticketTypes)

            if (ticketTypes.isEmpty()) {
                noDataToast.show()
            } else {
                noDataToast.cancel()
            }
        })
        searchButton.setOnClickListener {
            val startDate = startDateEditText.text.toString()
            val endDate = endDateEditText.text.toString()

            if (startDate.isEmpty() || endDate.isEmpty()) {
                showAlertDialog("Date Range Required", "Please fill in both Start Date and End Date.")
                return@setOnClickListener
            }

            if (!isDateFormatValid(startDate) || !isDateFormatValid(endDate)) {
                showAlertDialog("Invalid Date Format", "Please use the date format 'yyyy-MM-dd'.")
                return@setOnClickListener
            }

            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val startDateDate = sdf.parse(startDate)
            val endDateDate = sdf.parse(endDate)

            if (startDateDate != null && endDateDate != null) {
                val filteredList = viewModel.ticketTypesLiveData.value?.filter { ticketType ->
                    val createdOnDate = ticketType.getCreatedOnDate()

                    createdOnDate != null && (createdOnDate.after(startDateDate) || createdOnDate == startDateDate) && (createdOnDate.before(endDateDate) || createdOnDate == endDateDate)
                }

                if (filteredList != null) {
                    adapter.submitList(filteredList)

                    if (filteredList.isEmpty()) {
                        noDataToast.show()
                    } else {
                        noDataToast.cancel() // Hide the toast if filtered data is available
                    }
                }
            } else {
                showAlertDialog("Invalid Date Format", "An error occurred while parsing dates.")
            }
        }

        // Fetch initial ticket types
        CoroutineScope(Dispatchers.IO).launch {
            viewModel.fetchTicketTypes()
        }
    }

    private fun isDateFormatValid(dateString: String): Boolean {
        val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        dateFormat.isLenient = false
        return try {
            dateFormat.parse(dateString)
            true
        } catch (e: ParseException) {
            false
        }
    }

    private fun showAlertDialog(title: String, message: String) {
        val alertDialog = AlertDialog.Builder(this)
            .setTitle(title)
            .setMessage(message)
            .setPositiveButton("OK") { _, _ -> }
            .create()
        alertDialog.show()
    }
}

