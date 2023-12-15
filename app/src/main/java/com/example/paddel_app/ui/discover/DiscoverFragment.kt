package com.example.paddel_app.ui.discover

import DiscoverViewModel
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paddel_app.MainActivity
import com.example.paddel_app.R
import com.example.paddel_app.adapter.BookingsAdapter
import com.example.paddel_app.databinding.FragmentDiscoverBinding
import com.example.paddel_app.databinding.FragmentPlayBinding
import com.example.paddel_app.ui.play.ClubsAdapter
import com.example.paddel_app.ui.play.PlayViewModel

class DiscoverFragment : Fragment() {
    private lateinit var bookingsRecyclerView: RecyclerView
    private lateinit var bookingsAdapter: BookingsAdapter

    private var _binding: FragmentDiscoverBinding? = null
    private lateinit var discoverViewModel: DiscoverViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentDiscoverBinding.inflate(inflater, container, false)
        val root: View = binding.root

        discoverViewModel = ViewModelProvider(this).get(DiscoverViewModel::class.java)

        bookingsRecyclerView = binding.bookingsRecyclerView
        bookingsAdapter = BookingsAdapter()

        bookingsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        bookingsRecyclerView.adapter = bookingsAdapter

        // Load Bookings
        (activity as? MainActivity)?.getBookings { bookings ->
            discoverViewModel.setBookingsList(bookings)
        }

        // Observe changes in the bookings list
        discoverViewModel.getBookingsList().observe(viewLifecycleOwner, Observer { bookingsList ->
            // Update the UI with the new list of courts
            bookingsAdapter.submitList(bookingsList)
            for (booking in bookingsList) {
                Log.d("DiscoverFragment", "Booking: ${booking}")
            }
        })

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
