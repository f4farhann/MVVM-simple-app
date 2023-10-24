package com.example.testtapplication.view

import TicketType
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.testtapplication.R


class TicketAdapter(private var ticketList: List<TicketType>) :
    RecyclerView.Adapter<TicketAdapter.TicketViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TicketViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.item_ticket, parent, false)
        return TicketViewHolder(view)
    }

    override fun onBindViewHolder(holder: TicketViewHolder, position: Int) {
        val ticketItem: TicketType = ticketList[position]
        holder.typeId.text = "Ticket ID: ${ticketItem.typeId}"
        holder.description.text = "Description: ${ticketItem.description}"
        holder.createdOn.text = "createdOn: ${ticketItem.createdOn}"
        holder.updatedOn.text = "updatedOn: ${ticketItem.updatedOn}"

        holder.btnDelete.setOnClickListener {
            // Handle delete button click here
            removeItem(ticketItem)
        }
    }

    fun removeItem(ticketItem: TicketType) {
        val mutableTicketList = ticketList.toMutableList()
        mutableTicketList.remove(ticketItem)
        ticketList = mutableTicketList
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return ticketList.size
    }

    class TicketViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var typeId: TextView = itemView.findViewById(R.id.typeId)
        var description: TextView = itemView.findViewById(R.id.Description)
        var createdOn: TextView = itemView.findViewById(R.id.createdOn)
        var updatedOn: TextView = itemView.findViewById(R.id.updatedOn)
        var btnDelete: Button = itemView.findViewById(R.id.btnDelete)
    }

    fun submitList(newTicketList: List<TicketType>) {
        this.ticketList = newTicketList ?: emptyList()
        notifyDataSetChanged()
    }
}


