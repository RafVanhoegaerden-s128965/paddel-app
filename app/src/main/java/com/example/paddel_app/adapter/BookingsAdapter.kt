package com.example.paddel_app.adapter

import android.util.Log
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.core.os.bundleOf
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.findViewTreeViewModelStoreOwner
import androidx.navigation.findNavController
import com.example.paddel_app.R
import com.example.paddel_app.model.Booking
import com.example.paddel_app.ui.book_court.BookingDetailsViewModel
import com.example.paddel_app.ui.create_game.GameDetailsFragment
import com.example.paddel_app.ui.create_game.GameDetailsViewModel
import com.google.firebase.firestore.FirebaseFirestore


class BookingsAdapter(
    private val isCancelVisible: Boolean = false,
  private val isSelectVisible: Boolean = false
) : ListAdapter<Booking, BookingsAdapter.ViewHolder>(BookingDiffCallback() ) {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val courtNameTextView: TextView = itemView.findViewById(R.id.textViewName)
        val courtPriceTextView: TextView = itemView.findViewById(R.id.textViewPrice)
        val courtAdressTextView: TextView = itemView.findViewById(R.id.textViewAdress)
        val dateTextView: TextView = itemView.findViewById(R.id.textViewDate)
        val startEndTimeTextView: TextView = itemView.findViewById(R.id.textViewStartEndTime)
        val cancelBtn: Button = itemView.findViewById(R.id.cancelBtn)
        val selectBtn: Button = itemView.findViewById(R.id.selectBtn)
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

        // Fetch court details using courtId
        val courtId = data.courtId

        //region SetCourtValues
        // Firebase Instance
        val db = FirebaseFirestore.getInstance()
        val courtsCollection = db.collection("courts")

        // Select court with courtId
        courtsCollection.document(courtId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    Log.d("BookingsAdapter.Court", "Court: ${courtId}")

                    // Court document found, extract values and update TextViews
                    val courtName = document.getString("name")
                    val courtPrice = document.getString("price")
                    val courtAddress = document.getString("address")

                    // Set court values
                    holder.courtNameTextView.text = courtName
                    holder.courtPriceTextView.text = "€ $courtPrice"
                    holder.courtAdressTextView.text = courtAddress
                } else {
                    // Set default values to empty strings
                    holder.courtNameTextView.text = ""
                    holder.courtPriceTextView.text = ""
                    holder.courtAdressTextView.text = ""
                }
            }
            .addOnFailureListener { e ->
                Log.e("BookingsAdapter.Get", "Error getting document: $e")
            }
        //endregion

        holder.dateTextView.text = data.date
        holder.startEndTimeTextView.text = "${data.startTime} - ${data.endTime}"

        holder.cancelBtn.visibility = if (isCancelVisible) View.VISIBLE else View.GONE
        holder.selectBtn.visibility = if (isSelectVisible) View.VISIBLE else View.GONE

        holder.cancelBtn.setOnClickListener {
            val bookingsCollection = db.collection("bookings")
            val bookingId = data.id

            val gamesCollection = db.collection("games")
            gamesCollection.whereEqualTo("bookingId", bookingId)
                .get()
                .addOnSuccessListener { querySnapshot ->
                    for (document in querySnapshot.documents) {
                        // Delete each game document found
                        gamesCollection.document(document.id)
                            .delete()
                            .addOnSuccessListener {
                                Log.d("BookingsAdapter.Delete", "Game document successfully deleted!")
                            }
                            .addOnFailureListener { e ->
                                Log.e("BookingsAdapter.Delete", "Error deleting game document: $e")
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("BookingsAdapter.Delete", "Error querying game documents: $e")
                }

            // Delete the booking document
            bookingsCollection.document(bookingId)
                .delete()
                .addOnSuccessListener {

                    Log.d("BookingsAdapter.Delete", "Booking document successfully deleted!")
                }
                .addOnFailureListener { e ->
                    Log.e("BookingsAdapter.Delete", "Error deleting booking document: $e")
                }

            holder.itemView.findNavController().navigate(R.id.navigation_discover)
        }

        holder.selectBtn.setOnClickListener {
            val bundle = bundleOf("bookingId" to data.id)
            holder.itemView.findNavController().navigate(R.id.navigation_gameDetails, bundle)

            // Get GameDetailsViewModel
            val gameDetailsModel = ViewModelProvider(holder.itemView.findViewTreeViewModelStoreOwner()!!).get(GameDetailsViewModel::class.java)

            // Select Booking & Set in GameDetailsViewModel
            val selectedBooking = data
            gameDetailsModel.setBooking(selectedBooking)
        }
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
