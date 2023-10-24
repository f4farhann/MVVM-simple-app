package com.example.testtapplication.viewModel

import TicketType
import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.MutableLiveData
import com.example.testtapplication.model.ApiService
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class TicketViewModel : ViewModel() {
    private val apiService: ApiService by lazy {
        val retrofit = Retrofit.Builder()
            .baseUrl("http://43.205.1.83:8066/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        retrofit.create(ApiService::class.java)
    }

    val ticketTypesLiveData: MutableLiveData<List<TicketType>> = MutableLiveData()

    suspend fun fetchTicketTypes() {
        try {
            val ticketTypes = apiService.getTicketTypes()
            Log.d("API Response", ticketTypes.toString()) // Log the response
            ticketTypesLiveData.postValue(ticketTypes)
        } catch (e: Exception) {
            Log.e("API Error", e.message, e) // Log any errors
            // Handle the error, e.g., show an error message
        }
    }


}
