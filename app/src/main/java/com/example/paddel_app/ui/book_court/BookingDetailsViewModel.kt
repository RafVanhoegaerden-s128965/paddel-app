// BookingDetailsViewModel

package com.example.paddel_app.ui.book_court

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paddel_app.enum.Days
import com.example.paddel_app.model.Court
import com.example.paddel_app.model.TimeSlot
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookingDetailsViewModel : ViewModel() {

    private val _selectedDate = MutableLiveData<String>()
    private val _timeSlots = MutableLiveData<List<TimeSlot>>()
    private val _startTime = MutableLiveData<String>()
    private val _endTime = MutableLiveData<String>()
    private lateinit var court: Court

    val startTime: MutableLiveData<String> get() = _startTime
    val endTime: MutableLiveData<String> get() = _endTime

    fun getTimeSlots(): LiveData<List<TimeSlot>> {
        return _timeSlots
    }

    fun setCourt(court: Court) {
        this.court = court
        Log.d("BookingDetailsViewModel.CourtId", "Court ID: ${court.id}")
    }

    fun setSelectedDate(date: String) {
        _selectedDate.value = date
    }

    fun fetchTimeSlots() {
        val openingHours = court.openClosedHours.split(" - ")
        val openingTime = openingHours[0]
        val closingTime = openingHours[1]

        val closedDays = court.closedDays

        loadRealTimeSlots(openingTime, closingTime, closedDays)
    }

    private fun loadRealTimeSlots(openingTime: String, closingTime: String, closedDays: List<Days>) {
        val realTimeSlots = mutableListOf<TimeSlot>()
        val calendar = Calendar.getInstance()

        val matchDuration = 90
        val timeSlotDuration = 30

        val sdf = SimpleDateFormat("HH:mm", Locale.getDefault())
        val startTimeCalendar = Calendar.getInstance().apply {
            time = sdf.parse(openingTime)
        }

        val endTimeCalendar = Calendar.getInstance().apply {
            time = sdf.parse(closingTime)
        }

        // Check if the day is not closed before entering the loop
        if (!isClosedDay(_selectedDate.value, closedDays)) {
            // Set the start time to the nearest rounded 30 minutes after the opening time
            calendar.time = startTimeCalendar.time
            calendar.add(
                Calendar.MINUTE,
                (timeSlotDuration - calendar.get(Calendar.MINUTE) % timeSlotDuration) % timeSlotDuration
            )

            // Load the time slots until the end time is reached
            while (calendar.time.before(endTimeCalendar.time)) {
                val startTime = sdf.format(calendar.time)
                calendar.add(Calendar.MINUTE, matchDuration)
                val endTime = sdf.format(calendar.time)

                // Check if the end time is before closing time
                if (calendar.time.before(endTimeCalendar.time)) {
                    realTimeSlots.add(TimeSlot("$startTime - $endTime", true))
                }

                // Add 30 minutes to the current time for the next iteration
                calendar.add(Calendar.MINUTE, timeSlotDuration - matchDuration)
            }
        }
        _timeSlots.value = realTimeSlots
    }

    private fun isClosedDay(selectedDate: String?, closedDays: List<Days>): Boolean {
        val calendar = Calendar.getInstance()
        val sdf = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
        calendar.time = sdf.parse(selectedDate)

        val dayOfWeek = when (calendar.get(Calendar.DAY_OF_WEEK)) {
            Calendar.SUNDAY -> Days.SUNDAY
            Calendar.MONDAY -> Days.MONDAY
            Calendar.TUESDAY -> Days.TUESDAY
            Calendar.WEDNESDAY -> Days.WEDNESDAY
            Calendar.THURSDAY -> Days.THURSDAY
            Calendar.FRIDAY -> Days.FRIDAY
            Calendar.SATURDAY -> Days.SATURDAY
            else -> null
        }

        val isClosed = dayOfWeek in closedDays

        // Add logic to log the results
        Log.d("BookingDetailsViewModel", "Day: $dayOfWeek, Is Closed: $isClosed")

        return isClosed
    }
}
