package com.example.paddel_app.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.paddel_app.R
import com.example.paddel_app.model.Booking
import com.example.paddel_app.model.Game
import com.google.firebase.firestore.FirebaseFirestore

class GamesAdapter  : ListAdapter<Game, GamesAdapter.ViewHolder>(BookingDiffCallback())  {
    private lateinit var courtId: String
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courtNameTextView: TextView = itemView.findViewById(R.id.textViewName)
        val addressTextView: TextView = itemView.findViewById(R.id.textViewAddressGame)
        val dateTextView: TextView = itemView.findViewById(R.id.textViewDate)
        val startEndTimeTextView: TextView = itemView.findViewById(R.id.textViewStartEndTime)
        val ownerButton: Button = itemView.findViewById(R.id.ownerBtn)
        val ownerTextView: TextView = itemView.findViewById(R.id.textViewOwner)
        val player2Button: Button = itemView.findViewById(R.id.player2Btn)
        val player2TextView: TextView = itemView.findViewById(R.id.textViewPlayer2)
        val player3Button: Button = itemView.findViewById(R.id.player3Btn)
        val player3TextView: TextView = itemView.findViewById(R.id.textViewPlayer3)
        val player4Button: Button = itemView.findViewById(R.id.player4Btn)
        val player4TextView: TextView = itemView.findViewById(R.id.textViewPlayer4)
        val matchTypeTextView: TextView = itemView.findViewById(R.id.textViewMatchType)
        val matchGenderTextView: TextView = itemView.findViewById(R.id.textViewGenderType)
        val priceTextView: TextView = itemView.findViewById(R.id.textViewPrice)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GamesAdapter.ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(
            R.layout.item_booking,
            parent,
            false
        )
        return GamesAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val data = getItem(position)

        holder.matchTypeTextView.text = data.matchType.toString()
        holder.matchGenderTextView.text = data.genderType.toString()

        // Firebase Instance
        val db = FirebaseFirestore.getInstance()

        //region GetBooking

        // Fetch booking details using bookingId
        val bookingId = data.bookingId

        val bookingsCollection = db.collection("bookings")

        // Select booking with bookingId
        bookingsCollection.document(bookingId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    Log.d("GamesAdapter.Booking", "Booking: ${bookingId}")

                    // Fetch court details using courtId
                    courtId = document.getString("courtId")!!

                    // Booking document found, extract values and update TextViews
                    val bookingDate = document.getString("date")
                    val startTime = document.getString("startTime")
                    val endTime = document.getString("endTime")

                    // Set booking values
                    holder.dateTextView.text = bookingDate
                    holder.startEndTimeTextView.text = "${startTime} - ${endTime}"

                } else {
                    // Set default values to empty strings
                    holder.dateTextView.text = ""
                    holder.startEndTimeTextView.text = ""
                }
            }
            .addOnFailureListener { e ->
                Log.e("BookingsAdapter.Get", "Error getting document: $e")
            }
        //endregion

        //region GetBooking

        val courtsCollection = db.collection("courts")

        // Select booking with bookingId
        courtsCollection.document(courtId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    Log.d("GamesAdapter.Court", "Court: ${courtId}")

                    // Booking document found, extract values and update TextViews
                    val courtName = document.getString("name")
                    val adress = document.getString("adress")
                    val price = document.getString("price")

                    // Set booking values
                    holder.courtNameTextView.text = courtName
                    holder.addressTextView.text = adress
                    holder.priceTextView.text = price

                } else {
                    // Set default values to empty strings
                    holder.courtNameTextView.text = ""
                    holder.addressTextView.text = ""
                    holder.priceTextView.text = ""
                }
            }
            .addOnFailureListener { e ->
                Log.e("BookingsAdapter.Get", "Error getting document: $e")
            }
        //endregion
    }

}

private class BookingDiffCallback : DiffUtil.ItemCallback<Game>() {
    override fun areItemsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Game, newItem: Game): Boolean {
        return oldItem == newItem
    }
}