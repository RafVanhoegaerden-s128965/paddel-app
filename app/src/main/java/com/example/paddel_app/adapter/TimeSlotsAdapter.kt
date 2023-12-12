package com.example.paddel_app.ui.book_court

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.paddel_app.R
import com.example.paddel_app.model.TimeSlot

class TimeSlotsAdapter : ListAdapter<TimeSlot, TimeSlotsAdapter.TimeSlotViewHolder>(TimeSlotDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TimeSlotViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_time_slot, parent, false)
        return TimeSlotViewHolder(view)
    }

    override fun onBindViewHolder(holder: TimeSlotViewHolder, position: Int) {
        val timeSlot = getItem(position)
        holder.bind(timeSlot)
    }

    class TimeSlotViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val btnTimeSlot: Button = itemView.findViewById(R.id.btnTimeSlot)

        fun bind(timeSlot: TimeSlot) {
            btnTimeSlot.text = timeSlot.time
            btnTimeSlot.isEnabled = timeSlot.isAvailable
            // TODO: Add click listener to handle time slot selection
            // You can use interface or LiveData to communicate the selected time slot back to the fragment
        }
    }

    private class TimeSlotDiffCallback : DiffUtil.ItemCallback<TimeSlot>() {
        override fun areItemsTheSame(oldItem: TimeSlot, newItem: TimeSlot): Boolean {
            return oldItem.time == newItem.time
        }

        override fun areContentsTheSame(oldItem: TimeSlot, newItem: TimeSlot): Boolean {
            return oldItem == newItem
        }
    }
}