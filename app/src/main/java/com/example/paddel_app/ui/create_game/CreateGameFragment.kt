package com.example.paddel_app.ui.create_game

import DiscoverViewModel
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paddel_app.MainActivity
import com.example.paddel_app.R
import com.example.paddel_app.adapter.BookingsAdapter
import com.example.paddel_app.databinding.FragmentCreateGameBinding
import com.example.paddel_app.databinding.FragmentCreateGameBinding.*
import com.example.paddel_app.ui.book_court.BookCourtViewModel
import com.google.firebase.auth.FirebaseAuth


class CreateGameFragment : Fragment() {

    private lateinit var bookingsRecyclerView: RecyclerView
    private lateinit var bookingsAdapter: BookingsAdapter
    private var _binding: FragmentCreateGameBinding? = null
    private lateinit var createGameViewModel: CreateGameViewModel
    private lateinit var discoverViewModel: DiscoverViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = inflate(inflater, container, false)
        val root: View = binding.root

        createGameViewModel = ViewModelProvider(this).get(CreateGameViewModel::class.java)
        discoverViewModel = ViewModelProvider(this).get(DiscoverViewModel::class.java)

        //region Up-Button
        // Go back to original fragment logic
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        //endregion

        //region BookingList
        bookingsRecyclerView = binding.bookingsRecyclerView
        bookingsAdapter = BookingsAdapter(isCancelVisible = false, isSelectVisible = true)

        bookingsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        bookingsRecyclerView.adapter = bookingsAdapter

        // Load Bookings
        val auth = FirebaseAuth.getInstance()
        val currentUser = auth.currentUser
        (activity as? MainActivity)?.getBookings(currentUser!!.uid) { bookings ->
            discoverViewModel.setBookingsList(bookings)
        }

        // Observe changes in the bookings list
        discoverViewModel.getBookingsList().observe(viewLifecycleOwner, Observer { bookingsList ->
            // Update the UI with the new list of courts
            bookingsAdapter.submitList(bookingsList)
            for (booking in bookingsList) {
                Log.d("CreateGameFragment", "Booking: ${booking}")
            }
        })
        //endregion


        val btnBookCourt: Button = binding.newBookingBtn
        btnBookCourt.setOnClickListener {
            // Call the function to navigate to book court page
            findNavController().navigate(R.id.navigation_bookCourt)
        }

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