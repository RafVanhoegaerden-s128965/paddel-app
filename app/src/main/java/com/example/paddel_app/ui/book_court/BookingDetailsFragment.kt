// BookingDetailsFragment
package com.example.paddel_app.ui.book_court

import android.os.Bundle
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paddel_app.R
import com.example.paddel_app.databinding.FragmentBookCourtBinding
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
    private lateinit var viewModel: BookingDetailsViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookingDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        datePicker = root.findViewById(R.id.datePicker)
        btnConfirmDate = root.findViewById(R.id.btnConfirmDate)
        recyclerViewTimeSlots = root.findViewById(R.id.recyclerViewTimeSlots)

        //region Up-Button
        // Go back to original fragment logic
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        //endregion

        // Set the minimum allowed date to the current date
        val calendar = Calendar.getInstance()
        datePicker.minDate = System.currentTimeMillis() - 1000

        // Initialize ViewModel
        viewModel = ViewModelProvider(this).get(BookingDetailsViewModel::class.java)

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
                viewModel.setSelectedDate(selectedDate)
            }
        )

        btnConfirmDate.setOnClickListener {
            // Fetch and display time slots based on the selected date
            viewModel.fetchTimeSlots()
        }

        // Initialize the RecyclerView and adapter for time slots
        timeSlotsAdapter = TimeSlotsAdapter()
        recyclerViewTimeSlots.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerViewTimeSlots.adapter = timeSlotsAdapter

        // Observe changes in the list of time slots
        viewModel.getTimeSlots().observe(viewLifecycleOwner, { timeSlots ->
            // Update the adapter with the new list of time slots
            timeSlotsAdapter.submitList(timeSlots)
            // Show or hide the RecyclerView based on the availability of time slots
            recyclerViewTimeSlots.visibility = if (timeSlots.isEmpty()) View.GONE else View.VISIBLE
        })

        return root
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Handle the Up-button click
                findNavController().navigateUp()
                return true
            }
            // Handle other menu items if needed
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
