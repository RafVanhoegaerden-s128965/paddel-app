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
import com.example.paddel_app.adapter.BookingsAdapter
import com.example.paddel_app.adapter.GamesAdapter
import com.example.paddel_app.databinding.FragmentDiscoverBinding
import com.google.firebase.auth.FirebaseAuth

class DiscoverFragment : Fragment() {

    private val auth = FirebaseAuth.getInstance()
    private val currentUser = auth.currentUser

    private lateinit var gamesRecyclerView: RecyclerView
    private lateinit var gamesAdapter: GamesAdapter

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

        //region GamesList
        gamesRecyclerView = binding.openGamesRecyclerView
        gamesAdapter = GamesAdapter(requireContext())

        gamesRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        gamesRecyclerView.adapter = gamesAdapter

        // Load Games
//        (activity as? MainActivity)?.getOwnGames(currentUser!!.uid) { games ->
//            discoverViewModel.setGamesList(games)
//            Log.d("DiscoverFragment.Game", "Games: ${games}")
//
//        }
        (activity as? MainActivity)?.getAllGames() { games ->
            discoverViewModel.setGamesList(games)
            Log.d("DiscoverFragment.Game", "Games: ${games}")
        }


        // Observe changes in the bookings list
        discoverViewModel.getGamesList().observe(viewLifecycleOwner, Observer { gamesList ->
            // Update the UI with the new list of courts
            gamesAdapter.submitList(gamesList)
//            for (game in gamesList) {
//                Log.d("DiscoverFragment.Game", "Game: ${game}")
//            }
        })
        //endregion

        //region BookingList
        bookingsRecyclerView = binding.bookingsRecyclerView
        bookingsAdapter = BookingsAdapter(isCancelVisible = true, isSelectVisible = false)

        bookingsRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        bookingsRecyclerView.adapter = bookingsAdapter

        // Load Bookings
        (activity as? MainActivity)?.getBookings(currentUser!!.uid) { bookings ->
            discoverViewModel.setBookingsList(bookings)
                Log.d("DiscoverFragment.Booking", "Bookings: ${bookings}")
        }

        // Observe changes in the bookings list
        discoverViewModel.getBookingsList().observe(viewLifecycleOwner, Observer { bookingsList ->
            // Update the UI with the new list of courts
            bookingsAdapter.submitList(bookingsList)
//            for (booking in bookingsList) {
//                Log.d("DiscoverFragment.Booking", "Booking: ${booking}")
//            }
        })
        //endregion

        return root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
