package com.example.paddel_app.adapter

import DiscoverViewModel
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContentProviderCompat.requireContext
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.paddel_app.MainActivity
import com.example.paddel_app.R
import com.example.paddel_app.enum.GenderType
import com.example.paddel_app.model.Booking
import com.example.paddel_app.model.Game
import com.example.paddel_app.ui.discover.DiscoverFragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GamesAdapter(private val context: Context)  : ListAdapter<Game, GamesAdapter.ViewHolder>(BookingDiffCallback())  {

    private val currentUser = FirebaseAuth.getInstance().currentUser

    private var courtId: String = ""

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
            R.layout.item_game,
            parent,
            false

        )
        return GamesAdapter.ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        val data = getItem(position)

        // Set games values
        holder.matchTypeTextView.text = "Match Type: ${data.matchType.toString().toLowerCase().capitalize()}"
        data.genderType?.let {
            val displayText = when (it) {
                GenderType.MEN_ONLY -> "Men Only"
                GenderType.WOMEN_ONLY -> "Women Only"
                else -> it.name.toLowerCase().capitalize()
            }

            holder.matchGenderTextView.text = "Gender: ${displayText}"
        }

        // Firebase Instance
        val db = FirebaseFirestore.getInstance()

        //region GetUsers

        //region Owner
        val ownerId = data.userIdOwner
        val ownerCollection = db.collection("user")

        // Select user with userId
        ownerCollection.document(ownerId)
            .get()
            .addOnSuccessListener { document ->
                if (document != null && document.exists()) {
                    Log.d("GamesAdapter.Owner", "Owner: ${ownerId}")

                    val ownerFirstName = document.getString("firstName")
                    val ownerLastName = document.getString("lastName")

                    holder.ownerTextView.text = ownerFirstName
                    holder.ownerButton.text = "${ownerFirstName!!.capitalize().first()} ${ownerLastName!!.capitalize().first()}"
                }
            }
        //endregion

        //region Players
        fun loadPlayerData(playerId: String?, holder: ViewHolder) {
            if (playerId.isNullOrEmpty()) {
                // Handle the case where playerId is empty or null
                // Set button text to '+'
                holder.player2Button.text = "+"
                holder.player3Button.text = "+"
                holder.player4Button.text = "+"
                return
            }

            val playerCollection = db.collection("user")

            playerCollection.document(playerId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null && document.exists()) {
                        Log.d("GamesAdapter.Player", "Player: $playerId")

                        val firstName = document.getString("firstName")
                        val lastName = document.getString("lastName")

                        when (playerId) {
                            data.userIdPlayer2 -> {
                                holder.player2TextView.text = firstName
                                holder.player2Button.text = if (!firstName.isNullOrEmpty()) {
                                    "${firstName?.capitalize()?.first()} ${lastName?.capitalize()?.first()}"
                                } else {
                                    "+"
                                }
                                holder.player2Button.isClickable = holder.player2TextView.text.isEmpty()
                            }

                            data.userIdPlayer3 -> {
                                holder.player3TextView.text = firstName
                                holder.player3Button.text = if (!firstName.isNullOrEmpty()) {
                                    "${firstName?.capitalize()?.first()} ${lastName?.capitalize()?.first()}"
                                } else {
                                    "+"
                                }
                                holder.player3Button.isClickable = holder.player3TextView.text.isEmpty()
                            }

                            data.userIdPlayer4 -> {
                                holder.player4TextView.text = firstName
                                holder.player4Button.text = if (!firstName.isNullOrEmpty()) {
                                    "${lastName?.capitalize()?.first()} ${lastName?.capitalize()?.first()}"
                                } else {
                                    "+"
                                }
                                holder.player4Button.isClickable = holder.player4TextView.text.isEmpty()
                            }

                            else -> {
                                // Handle the case where playerId doesn't match any known ID
                                Log.e("GamesAdapter", "No playerId found!")
                            }
                        }
                    }
                }
        }

        loadPlayerData(data.userIdPlayer2, holder)
        loadPlayerData(data.userIdPlayer3, holder)
        loadPlayerData(data.userIdPlayer4, holder)
        //endregion

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
                    courtId = document.getString("courtId") ?: ""

                    // Booking document found, extract values and update TextViews
                    val bookingDate = document.getString("date")
                    val startTime = document.getString("startTime")
                    val endTime = document.getString("endTime")

                    // Set booking values
                    holder.dateTextView.text = bookingDate
                    holder.startEndTimeTextView.text = "${startTime} - ${endTime}"

                    //region GetCourt
                    if (courtId.isNotBlank()) {
                        val courtsCollection = db.collection("courts")

                        courtsCollection.document(courtId)
                            .get()
                            .addOnSuccessListener { courtDocument ->
                                if (courtDocument != null && courtDocument.exists()) {
                                    Log.d("GamesAdapter.Court", "Court: ${courtId}")

                                    // Court document found, extract values and update TextViews
                                    val courtName = courtDocument.getString("name")
                                    val courtAddress = courtDocument.getString("address")
                                    val price = courtDocument.getString("price")

                                    // Set court values
                                    holder.courtNameTextView.text = courtName
                                    holder.addressTextView.text = courtAddress
                                    holder.priceTextView.text = "€ ${price}"
                                } else {
                                    // Set default values to empty strings
                                    holder.courtNameTextView.text = ""
                                    holder.addressTextView.text = ""
                                    holder.priceTextView.text = ""
                                }
                            }
                            .addOnFailureListener { e ->
                                Log.e("GamesAdapter.GetCourt", "Error getting court document: $e")
                            }
                    }
                    //endregion GetCourt
                }
            }
            .addOnFailureListener { e ->
                Log.e("GamesAdapter.GetBooking", "Error getting booking document: $e")
            }

        //endregion

        //region Buttons
        // TODO Checks on gender / match type
        fun joinGame(playerNumber: Int, data: Game, holder: ViewHolder) {
            val gameId = data.id
            val gameRef = db.collection("games").document(gameId)

            if (data.userIdOwner == currentUser!!.uid) {
                Toast.makeText(context, "Can't join your own game!", Toast.LENGTH_SHORT).show()
            } else {
                val currentPlayerId: String = when (playerNumber) {
                    2 -> data.userIdPlayer2
                    3 -> data.userIdPlayer3
                    4 -> data.userIdPlayer4
                    else -> ""
                }

                val otherPlayerIds = listOf(data.userIdPlayer2, data.userIdPlayer3, data.userIdPlayer4)

                if (currentPlayerId == currentUser!!.uid || currentUser!!.uid in otherPlayerIds) {
                    Toast.makeText(context, "Can't join a game twice!", Toast.LENGTH_SHORT).show()
                } else {
                    gameRef.update("userIdPlayer$playerNumber", currentUser!!.uid)
                        .addOnSuccessListener {
                            // Update succes
                            // TODO values only show after reload of page
                        }
                        .addOnFailureListener { e ->
                            Log.e("GamesAdapter.UpdatePlayer$playerNumber", "Error updating document: $e")
                        }
                }
            }
        }


        holder.player2Button.setOnClickListener {
            joinGame(2, data, holder)
        }

        holder.player3Button.setOnClickListener {
            joinGame(3, data, holder)
        }

        holder.player4Button.setOnClickListener {
            joinGame(4, data, holder)
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