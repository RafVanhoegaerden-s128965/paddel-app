package com.example.paddel_app.ui.discover
import DiscoverViewModel
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
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

    private val handler = Handler()
    private val updateInterval = 10000L // 10 seconds

    private var isShowingCreatedGames = true // Default to true since you initially load created games

    private val updateRunnable = object : Runnable {
        override fun run() {
            // Decide which games to load based on the current state
            if (isShowingCreatedGames) {
                // Load Created Games
                (activity as? MainActivity)?.getCreatedGames(currentUser!!.uid) { games ->
                    discoverViewModel.setGamesList(games)
                    Log.d("DiscoverFragment.Game", "Games: $games")
                }
            } else {
                // Load Open Games
                (activity as? MainActivity)?.getOpenGames(currentUser!!.uid) { games ->
                    discoverViewModel.setGamesList(games)
                    Log.d("DiscoverFragment.Game", "Open Games: $games")
                }
            }

            // Load Bookings
            (activity as? MainActivity)?.getBookings(currentUser!!.uid) { bookings ->
                discoverViewModel.setBookingsList(bookings)
                Log.d("DiscoverFragment.Booking", "Bookings: $bookings")
            }

            // Schedule the next update after the specified interval
            handler.postDelayed(this, updateInterval)
        }
    }

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

        // Load Open Games immediately when the fragment is created
        (activity as? MainActivity)?.getOpenGames(currentUser!!.uid) { games ->
            discoverViewModel.setGamesList(games)
            Log.d("DiscoverFragment.Game", "Open Games: $games")
        }

        // Load Created Games
        val showCreatedGamesBtn: Button = binding.showCreatedGamesBtn
        showCreatedGamesBtn.setOnClickListener {
            isShowingCreatedGames = true
            // Load Created Games
            (activity as? MainActivity)?.getCreatedGames(currentUser!!.uid) { games ->
                discoverViewModel.setGamesList(games)
                Log.d("DiscoverFragment.Game", "Games: $games")
            }
        }

        // Load Open Games
        val showOpenGamesBtn: Button = binding.showOpenGamesBtn
        showOpenGamesBtn.setOnClickListener {
            isShowingCreatedGames = false
            // Load Open Games
            (activity as? MainActivity)?.getOpenGames(currentUser!!.uid) { games ->
                discoverViewModel.setGamesList(games)
                Log.d("DiscoverFragment.Game", "Open Games: $games")
            }
        }

        // Observe changes in the games list
        discoverViewModel.getGamesList().observe(viewLifecycleOwner, Observer { gamesList ->
            // Update the UI with the new list of games
            gamesAdapter.submitList(gamesList)
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
            Log.d("DiscoverFragment.Booking", "Bookings: $bookings")
        }

        // Observe changes in the bookings list
        discoverViewModel.getBookingsList().observe(viewLifecycleOwner, Observer { bookingsList ->
            // Update the UI with the new list of bookings
            bookingsAdapter.submitList(bookingsList)
        })
        //endregion

        // Schedule the initial data update after the specified interval
        handler.postDelayed(updateRunnable, updateInterval)

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // Remove the update callback when the fragment is destroyed
        handler.removeCallbacks(updateRunnable)
        _binding = null
    }
}
