package com.example.paddel_app.ui.play

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.paddel_app.R
import com.example.paddel_app.model.Court

class CustomAdapter : ListAdapter<Court, CustomAdapter.ViewHolder>(CourtDiffCallback()) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val clubNameTextView: TextView = itemView.findViewById(R.id.textViewName)
        val clubAddressTextView: TextView = itemView.findViewById(R.id.textViewAddress)
        // Add other views according to your XML layout
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_card,
            parent,
            false
        )
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)
        holder.clubNameTextView.text = data.name
        holder.clubAddressTextView.text = data.address
        // Bind other views according to your XML layout
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