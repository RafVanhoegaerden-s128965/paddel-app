package com.example.paddel_app.ui.play

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.navigation.findNavController
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.paddel_app.R
import com.example.paddel_app.model.Court
import com.example.paddel_app.ui.book_court.BookingDetailsViewModel

class ClubsAdapter : ListAdapter<Court, ClubsAdapter.ViewHolder>(CourtDiffCallback()) {
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val clubNameTextView: TextView = itemView.findViewById(R.id.textViewName)
        val clubAddressTextView: TextView = itemView.findViewById(R.id.textViewAddress)
        val clubPriceTextView: TextView = itemView.findViewById(R.id.textViewPrice)
        val clubOpenClosedTextView: TextView = itemView.findViewById(R.id.textViewOpenClosedHours)
        val clubClosedDays: TextView = itemView.findViewById(R.id.textViewClosedDays)
        val bookBtn: Button = itemView.findViewById(R.id.bookBtn)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_club,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.clubNameTextView.text = data.name
        holder.clubAddressTextView.text = data.address
        holder.clubPriceTextView.text = "€ ${data.price}"
        holder.clubOpenClosedTextView.text = "Open: ${data.openClosedHours}"

        // Closed Days
        val closedDaysString = data.closedDays.joinToString(", ")
        val formattedClosedDays = closedDaysString.split(", ").joinToString(", ") { it.toLowerCase().capitalize() }
        holder.clubClosedDays.text = "Closed: ${formattedClosedDays}"

        holder.bookBtn.setOnClickListener {
            val bundle = bundleOf("courtId" to data.id)
            holder.itemView.findNavController().navigate(R.id.navigation_bookingDetails, bundle)

            // Get BookingsDetailsViewModel
            val bookingDetailsViewModel = ViewModelProvider(holder.itemView.findViewTreeViewModelStoreOwner()!!).get(BookingDetailsViewModel::class.java)

            // Select Court & Set in BookingDetailsViewModel
            val selectedCourt = data
            bookingDetailsViewModel.setCourt(selectedCourt)
        }
    }

    private class CourtDiffCallback : DiffUtil.ItemCallback<Court>() {
        override fun areItemsTheSame(oldItem: Court, newItem: Court): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Court, newItem: Court): Boolean {
            return oldItem == newItem
        }
    }
}
