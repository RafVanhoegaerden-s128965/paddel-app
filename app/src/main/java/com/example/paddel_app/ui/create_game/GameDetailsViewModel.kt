package com.example.paddel_app.ui.create_game

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paddel_app.enum.GenderType
import com.example.paddel_app.enum.MatchType
import com.example.paddel_app.model.Booking
import com.example.paddel_app.model.Court

class GameDetailsViewModel : ViewModel() {
    private lateinit var booking: Booking
    private val _matchType = MutableLiveData<MatchType?>()
    private val _genderType =  MutableLiveData<GenderType?>()

    val matchType: MutableLiveData<MatchType?> get() = _matchType
    val genderType: MutableLiveData<GenderType?> get() = _genderType

    fun setBooking(booking: Booking) {
        this.booking = booking
        Log.d("GameDetailsViewModel.BookingId", "Booking ID: ${booking.id}")
    }
}