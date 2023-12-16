package com.example.paddel_app.ui.create_game

import android.util.Log
import androidx.lifecycle.ViewModel
import com.example.paddel_app.model.Booking
import com.example.paddel_app.model.Court

class GameDetailsViewModel : ViewModel() {
    private lateinit var booking: Booking
    fun setBooking(booking: Booking) {
        this.booking = booking
        Log.d("GameDetailsViewModel.BookingId", "Booking ID: ${booking.id}")
    }
}