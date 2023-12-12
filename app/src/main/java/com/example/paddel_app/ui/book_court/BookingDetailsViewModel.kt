// BookingDetailsViewModel
package com.example.paddel_app.ui.book_court

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paddel_app.model.TimeSlot
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookingDetailsViewModel : ViewModel() {

    private val _selectedDate = MutableLiveData<String>()
    private val _timeSlots = MutableLiveData<List<TimeSlot>>()

    // Set the selected date
    fun setSelectedDate(date: String) {
        _selectedDate.value = date
    }

    // Get the list of time slots
    fun getTimeSlots(): LiveData<List<TimeSlot>> {
        return _timeSlots
    }

    // Fetch time slots based on the selected date and court ID
    fun fetchTimeSlots() {

        loadDummyTimeSlots()
    }


    // Dummy function to load sample time slots
    private fun loadDummyTimeSlots() {
        val dummyTimeSlots = mutableListOf<TimeSlot>()
        val calendar = Calendar.getInstance()
        calendar.set(Calendar.HOUR_OF_DAY, 10)
        calendar.set(Calendar.MINUTE, 0)

        while (calendar.get(Calendar.HOUR_OF_DAY) < 20) {
            val time = SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
            dummyTimeSlots.add(TimeSlot(time, true))

            // Voeg 30 minuten toe aan de huidige tijd
            calendar.add(Calendar.MINUTE, 30)
        }

        _timeSlots.value = dummyTimeSlots
    }
}
