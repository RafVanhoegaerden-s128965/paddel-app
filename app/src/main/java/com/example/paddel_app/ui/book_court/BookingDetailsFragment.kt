package com.example.paddel_app.ui.book_court

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paddel_app.R
import com.example.paddel_app.databinding.FragmentBookingDetailsBinding
import com.example.paddel_app.model.Court
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookingDetailsFragment : Fragment() {

    private var _binding: FragmentBookingDetailsBinding? = null

    private lateinit var datePicker: DatePicker
    private lateinit var btnConfirmDate: Button
    private lateinit var recyclerViewTimeSlots: RecyclerView
    private lateinit var timeSlotsAdapter: TimeSlotsAdapter
    private lateinit var bookingDetailsViewModel: BookingDetailsViewModel
    private lateinit var selectedDate: String // Declare selectedDate at a higher scope

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        // Get CourtId
        val bundle = arguments
        if (bundle != null && bundle.containsKey("courtId")) {
            // Hier krijg je de courtId uit de bundel
            val courtId = bundle.getString("courtId", "")

            getCourtWithID(courtId) { court ->
                if (court != null) {
                    Log.d("BookingDetails.CourtName", "Court Name: ${court.name}")

                    val courtName: TextView = root.findViewById(R.id.courtName)
                    courtName.text = "Court Name: ${court.name}"

                    // Set Court in ViewModel
                    bookingDetailsViewModel.setCourt(court)
                } else {
                    // Geen gevonden met de opgegeven ID
                    Log.e("BookingDetails", "No court found with ID: $courtId")
                }
            }
        }

        // Initialize ViewModel
        bookingDetailsViewModel = ViewModelProvider(this).get(BookingDetailsViewModel::class.java)
        datePicker = root.findViewById(R.id.datePicker)
        btnConfirmDate = root.findViewById(R.id.btnConfirmDate)
        recyclerViewTimeSlots = root.findViewById(R.id.recyclerViewTimeSlots)

        //region Up-Button
        // Go back to original fragment logic
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        //endregion

        //region DatePicker
        // Set the minimum allowed date to the current date
        val calendar = Calendar.getInstance()
        datePicker.minDate = System.currentTimeMillis() - 1000

        // Declare selectedDate outside the listener
        selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
            Calendar.getInstance().apply {
                set(Calendar.YEAR, datePicker.year)
                set(Calendar.MONTH, datePicker.month)
                set(Calendar.DAY_OF_MONTH, datePicker.dayOfMonth)
            }.time
        )

        // Add OnDateChangedListener to get the selected date
        datePicker.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            DatePicker.OnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
                // Handle the date change event
                selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                    Calendar.getInstance().apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, monthOfYear)
                        set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    }.time
                )
            }
        )
        //endregion

        btnConfirmDate.setOnClickListener {
            // Use the selected date here
            bookingDetailsViewModel.setSelectedDate(selectedDate)

            // Fetch and display time slots based on the selected date
            bookingDetailsViewModel.fetchTimeSlots()
        }

        // Initialize the RecyclerView and adapter for time slots
        timeSlotsAdapter = TimeSlotsAdapter()
        recyclerViewTimeSlots.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewTimeSlots.adapter = timeSlotsAdapter

        // Observe changes in the list of time slots
        bookingDetailsViewModel.getTimeSlots().observe(viewLifecycleOwner) { timeSlots ->
            // Update the adapter with the new list of time slots
            timeSlotsAdapter.submitList(timeSlots)
            // Show or hide the RecyclerView based on the availability of time slots
            recyclerViewTimeSlots.visibility = if (timeSlots.isEmpty()) View.GONE else View.VISIBLE
        }

        return root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            android.R.id.home -> {
                // Handle the Up-button click
                findNavController().navigateUp()
                true
            }
            // Handle other menu items if needed
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun getCourtWithID(courtId: String, callback: (Court?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val courtsCollection = db.collection("courts")

        val courtRef = courtsCollection.document(courtId)

        courtRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Convert the document to a Court object
                    val court = document.toObject(Court::class.java)
                    court?.id = document.id

                    // Invoke the callback with the retrieved court (or null if not found)
                    callback(court)
                } else {
                    // Document with the specified ID not found
                    callback(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("BookingDetailsFragment", "Firestore query failed: $exception")
                // Handle errors here
                callback(null)
            }
    }
}
