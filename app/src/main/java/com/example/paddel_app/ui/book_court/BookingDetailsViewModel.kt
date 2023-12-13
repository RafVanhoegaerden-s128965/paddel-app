// BookingDetailsViewModel
package com.example.paddel_app.ui.book_court

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paddel_app.model.Court
import com.example.paddel_app.model.TimeSlot
import com.google.firebase.Firebase
import com.google.firebase.firestore.DocumentReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookingDetailsViewModel : ViewModel() {

    //region PrivateVariables
    private val _selectedDate = MutableLiveData<String>()
    private val _timeSlots = MutableLiveData<List<TimeSlot>>()

    private val _startTime = MutableLiveData<String>()
    private val _endTime = MutableLiveData<String>()
    //endregion

    //region PublicVariables
    val startTime: MutableLiveData<String> get() = _startTime
    val endTime: MutableLiveData<String> get() = _endTime
    //endregion

    //region Getters

    // Get the list of time slots
    fun getTimeSlots(): LiveData<List<TimeSlot>> {
        return _timeSlots
    }
    //endregion

    //region Setter
    fun setCourt(court: Court){
        Log.d("BookingDetailsViewModel.CourtId","Court ID: ${court.id}")
    }
    fun setSelectedDate(date: String) {
        _selectedDate.value = date
    }

    //endregion



    // Fetch time slots based on the selected date and court ID
    fun fetchTimeSlots() {
        loadDummyTimeSlots()
    }


    // Dummy function to load sample time slots
    private fun loadDummyTimeSlots() {
        val dummyTimeSlots = mutableListOf<TimeSlot>()
        val calendar = Calendar.getInstance()
        // TODO Set start time to time from firebase
        calendar.set(Calendar.HOUR_OF_DAY, 10)
        calendar.set(Calendar.MINUTE, 0)

        // TODO Set end time to time from firebase
        while (calendar.get(Calendar.HOUR_OF_DAY) < 20) {
            val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
            dummyTimeSlots.add(TimeSlot(time, true))

            // Voeg 30 minuten toe aan de huidige tijd
            calendar.add(Calendar.MINUTE, 30)
        }

        _timeSlots.value = dummyTimeSlots
    }
}

