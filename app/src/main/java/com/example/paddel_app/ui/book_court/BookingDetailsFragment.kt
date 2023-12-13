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

            // Nu kun je de courtId gebruiken zoals nodig in je fragment
            // Bijvoorbeeld, toon het in een TextView
            Log.d("BookingDetails.CourtId","Court ID: ${courtId}")
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

        // Add OnDateChangedListener to get the selected date
        datePicker.init(
            calendar.get(Calendar.YEAR),
            calendar.get(Calendar.MONTH),
            calendar.get(Calendar.DAY_OF_MONTH),
            DatePicker.OnDateChangedListener { _, year, monthOfYear, dayOfMonth ->
                // Handle the date change event
                val selectedDate = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault()).format(
                    Calendar.getInstance().apply {
                        set(Calendar.YEAR, year)
                        set(Calendar.MONTH, monthOfYear)
                        set(Calendar.DAY_OF_MONTH, dayOfMonth)
                    }.time
                )
                // Pass the selected date to the ViewModel
                bookingDetailsViewModel.setSelectedDate(selectedDate)
            }
        )
        //endregion
        btnConfirmDate.setOnClickListener {
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
}