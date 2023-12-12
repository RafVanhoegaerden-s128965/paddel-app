package com.example.paddel_app.ui.book_court

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.DatePicker
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.paddel_app.R
import java.util.Calendar

class BookingDetailsFragment : Fragment() {

    private lateinit var datePicker: DatePicker
    private lateinit var btnNext: Button


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val root = inflater.inflate(R.layout.fragment_booking_details, container, false)

        datePicker = root.findViewById(R.id.datePicker)
        btnNext = root.findViewById(R.id.btnNext)

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
                val selectedDate = "$dayOfMonth/${monthOfYear + 1}/$year"
            }
        )

        btnNext.setOnClickListener {

        }

        return root
    }
}
