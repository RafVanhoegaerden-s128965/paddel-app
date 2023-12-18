package com.example.paddel_app.ui.create_game

import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paddel_app.databinding.FragmentGameDetailsBinding
import com.example.paddel_app.enum.GenderType
import com.example.paddel_app.enum.MatchType
import com.example.paddel_app.model.Booking
import com.example.paddel_app.model.Court
import com.example.paddel_app.model.Game
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class GameDetailsViewModel : ViewModel() {
    private val _currentUser = FirebaseAuth.getInstance().currentUser
    private lateinit var booking: Booking
    private val _matchType = MutableLiveData<MatchType?>()
    private val _genderType =  MutableLiveData<GenderType?>()

    val matchType: MutableLiveData<MatchType?> get() = _matchType
    val genderType: MutableLiveData<GenderType?> get() = _genderType

    fun setBooking(booking: Booking) {
        this.booking = booking
        Log.d("GameDetailsViewModel.BookingId", "Booking ID: ${booking.id}")
    }

    fun setMatchType(matchType: MatchType){
        // Set data value
        _matchType.value = matchType
    }

    fun setGenderType(genderType: GenderType){
        // Set data value
        _genderType.value = genderType
    }

    fun createGame() {
        val userId = _currentUser?.uid
        val bookingId = booking?.id

        if (userId != null && bookingId != null) {
            val game = Game(
                id = "",
                userIdOwner = userId,
                userIdPlayer2 = "",
                userIdPlayer3 = "",
                userIdPlayer4 = "",
                bookingId = bookingId,
                matchType = _matchType.value,
                genderType = _genderType.value
            )

            val db = FirebaseFirestore.getInstance()
            val gamesCollection = db.collection("games")

            // Add booking to collection
            gamesCollection.add(game)
                .addOnSuccessListener { documentReference ->
                    val gameId = documentReference.id

                    // Update the booking with the generated ID
                    game.id = gameId

                    // Now the id field is set to the document ID
                    Log.d("GameDetailsViewModel", "Game added with ID: $gameId")

                    // Update the document in Firestore with the correct id
                    documentReference.update("id", gameId)
                        .addOnSuccessListener {
                            Log.d("GameDetailsViewModel", "Game document updated with correct id")
                        }
                        .addOnFailureListener { updateError ->
                            Log.e("GameDetailsViewModel", "Error updating game document", updateError)
                        }
                }
                .addOnFailureListener { e ->
                    Log.e("GameDetailsViewModel", "Error adding game", e)
                }
        } else {
            Log.e("GameDetailsViewModel", "User ID or Booking ID is null")
        }
    }
}