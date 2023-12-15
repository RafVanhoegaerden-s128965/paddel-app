package com.example.paddel_app.adapter

import DiscoverViewModel
import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.paddel_app.R
import com.example.paddel_app.model.Booking
import com.example.paddel_app.model.Court
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.firestore.FirebaseFirestore


class BookingsAdapter : ListAdapter<Booking, BookingsAdapter.ViewHolder>(BookingDiffCallback()) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courtNameTextView: TextView = itemView.findViewById(R.id.textViewName)
        val courtPriceTextView: TextView = itemView.findViewById(R.id.textViewPrice)
        val courtAdressTextView: TextView = itemView.findViewById(R.id.textViewAdress)
        val dateTextView: TextView = itemView.findViewById(R.id.textViewDate)
        val startEndTimeTextView: TextView = itemView.findViewById(R.id.textViewStartEndTime)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_booking,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)

        // Current Booking-data
        val courtId = data.courtId


        holder.dateTextView.text = "Date: ${data.date}"
        holder.startEndTimeTextView.text = "Time: ${data.startTime} - ${data.endTime}"
    }

    private class BookingDiffCallback : DiffUtil.ItemCallback<Booking>() {
        override fun areItemsTheSame(oldItem: Booking, newItem: Booking): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Booking, newItem: Booking): Boolean {
            return oldItem == newItem
        }
    }

}
