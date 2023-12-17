package com.example.paddel_app.ui.book_court

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paddel_app.enum.Days
import com.example.paddel_app.model.Booking
import com.example.paddel_app.model.Court
import com.example.paddel_app.model.TimeSlot
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookingDetailsViewModel : ViewModel() {

    private val _selectedDate = MutableLiveData<String>()
    private val _timeSlots = MutableLiveData<List<TimeSlot>>()
    private lateinit var court: Court
    private val _currentUser = FirebaseAuth.getInstance().currentUser
    private val _existingBookings = MutableLiveData<List<Booking>>()

    fun getTimeSlots(): LiveData<List<TimeSlot>> {
        return _timeSlots
    }

    fun getExistingBookings(): LiveData<List<Booking>> {
        return _existingBookings
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

        // Fetch existing bookings for the selected day
        fetchExistingBookings(_selectedDate.value.orEmpty())

        // Load real-time slots after fetching existing bookings
        loadRealTimeSlots(openingTime, closingTime, closedDays)
    }

    // Fetch existing bookings for the selected day
    private fun fetchExistingBookings(selectedDate: String) {
        val db = FirebaseFirestore.getInstance()
        val bookingsCollection = db.collection("bookings")

        // Query bookings for the selected day
        bookingsCollection
            .whereEqualTo("courtId", court.id)
            .whereEqualTo("date", selectedDate)
            .get()
            .addOnSuccessListener { result ->
                val existingBookings = result.toObjects(Booking::class.java)
                _existingBookings.value = existingBookings
            }
            .addOnFailureListener { e ->
                Log.e("BookingDetailsViewModel", "Error fetching existing bookings", e)
            }
    }

    private fun loadRealTimeSlots(openingTime: String, closingTime: String, closedDays: List<Days>) {
        // Existing bookings for the selected day
        val existingBookings = _existingBookings.value ?: emptyList()

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

                // Check if the time slot is available (not overlapping with existing bookings)
                val isAvailable = existingBookings.none { booking ->
                    // Check for overlap
                    !(endTime <= booking.startTime || startTime >= booking.endTime)
                }

                // Check if the end time is before closing time
                val isBeforeClosingTime = sdf.parse(endTime)?.before(sdf.parse(closingTime)) ?: false

                // Add to realTimeSlots only if available and before closing time
                if (isAvailable && isBeforeClosingTime) {
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

    fun createBooking(timeSlot: TimeSlot) {
        val userId = _currentUser?.uid // Replace this with your actual method to get the current user ID
        val courtId = court.id // Assuming court is already set in the ViewModel

        val startTime = timeSlot.time.split(" - ")[0]
        val endTime = timeSlot.time.split(" - ")[1]

        // Ensure that the selectedDate is not null before using it
        val date = _selectedDate.value.orEmpty()

        val booking = Booking(
            id = "",
            userId = userId.orEmpty(),
            courtId = courtId,
            startTime = startTime,
            endTime = endTime,
            date = date,
        )

        val db = FirebaseFirestore.getInstance()
        val bookingsCollection = db.collection("bookings")

        // Add booking to collection
        bookingsCollection.add(booking)
            .addOnSuccessListener { documentReference ->
                val bookingId = documentReference.id

                // Update the booking with the generated ID
                booking.id = bookingId

                // Now the id field is set to the document ID
                Log.d("BookingDetailsViewModel", "Booking added with ID: $bookingId")

                // Update the document in Firestore with the correct id
                documentReference.update("id", bookingId)
                    .addOnSuccessListener {
                        Log.d("BookingDetailsViewModel", "Booking document updated with correct id")
                    }
                    .addOnFailureListener { updateError ->
                        Log.e("BookingDetailsViewModel", "Error updating booking document", updateError)
                    }
            }
            .addOnFailureListener { e ->
                Log.e("BookingDetailsViewModel", "Error adding booking", e)
            }
    }
}
