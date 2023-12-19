package com.example.paddel_app.ui.create_game

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.fragment.findNavController
import com.example.paddel_app.R
import com.example.paddel_app.databinding.FragmentGameDetailsBinding
import com.example.paddel_app.enum.GenderType
import com.example.paddel_app.enum.MatchType
import com.example.paddel_app.model.Booking
import com.example.paddel_app.model.Court
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.firestore.FirebaseFirestore

class GameDetailsFragment : Fragment() {

    private var _binding: FragmentGameDetailsBinding? = null
    private lateinit var gameDetailsViewModel: GameDetailsViewModel
    private lateinit var btnCreateGame: Button

    private lateinit var bookingId: String
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //region GetBookingId
        val bundle = arguments
        if (bundle != null && bundle.containsKey("bookingId")) {
            bookingId = bundle.getString("bookingId", "")

            getBookingWithID(bookingId) { booking ->
                if (booking != null) {
                    // Set Booking in ViewModel
                    gameDetailsViewModel.setBooking(booking)
                } else {
                    // Geen gevonden met de opgegeven ID
                    Log.e("BookingDetails", "No court found with ID: $bookingId")
                }
            }
        }
        //endregion

        gameDetailsViewModel = ViewModelProvider(this).get(GameDetailsViewModel::class.java)
        btnCreateGame = root.findViewById(R.id.confirmBtn)
        //region Up-Button
        // Go back to original fragment logic
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        //endregion

        //region MatchType
        val matchTypeTextView: TextView = binding.matchTypeTextView
        gameDetailsViewModel.matchType.observe(viewLifecycleOwner, Observer { matchType ->
            matchType?.let {
                val lowerCaseMatchType = it.name.toLowerCase().capitalize()
                matchTypeTextView.text = lowerCaseMatchType
                Log.d("GameDetailsFragment", "Match Type: ${it.toString()}")
            }
        })
        val competitiveBtn: Button = binding.comptetiveBtn
        competitiveBtn.setOnClickListener() {
            gameDetailsViewModel.setMatchType(MatchType.COMPETITIVE)
        }
        val friendlyBtn: Button = binding.friendlyBtn
        friendlyBtn.setOnClickListener() {
            gameDetailsViewModel.setMatchType(MatchType.FRIENDLY)
        }
        //endregion

        //region GenderType
        val genderTypeTextView: TextView = binding.genderTextView
        gameDetailsViewModel.genderType.observe(viewLifecycleOwner, Observer { genderType ->
            genderType?.let {
                val displayText = when (it) {
                    GenderType.MEN_ONLY -> "Men Only"
                    GenderType.WOMEN_ONLY -> "Women Only"
                    else -> it.name.toLowerCase().capitalize()
                }

                genderTypeTextView.text = displayText

                Log.d("GameDetailsFragment", "Gender Type: ${it.toString()}")
            }
        })

        val allPlayersBtn: Button = binding.allBtn
        allPlayersBtn.setOnClickListener() {
            gameDetailsViewModel.setGenderType(GenderType.ALL)
        }
        val mixedBtn: Button = binding.mixedBtn
        mixedBtn.setOnClickListener() {
            gameDetailsViewModel.setGenderType(GenderType.MIXED)
        }
        // TODO Switch button if the user is Male or Female
        val menBtn: Button = binding.menBtn
        menBtn.setOnClickListener() {
            gameDetailsViewModel.setGenderType(GenderType.MEN_ONLY)
        }
        val womenBtn: Button = binding.womenBtn
        womenBtn.setOnClickListener() {
            gameDetailsViewModel.setGenderType(GenderType.WOMEN_ONLY)
        }
        //endregion

        btnCreateGame.setOnClickListener{
            gameDetailsViewModel.createGame()

            // TODO add to active list

            showGameSuccessMessage()

            findNavController().navigate(R.id.navigation_discover)
        }

        return root
    }

    private fun showGameSuccessMessage() {
        val rootView = requireActivity().findViewById<View>(android.R.id.content)
        Snackbar.make(rootView, "New game created!", Snackbar.LENGTH_LONG).show()
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

    private fun getBookingWithID(bookingId: String, callback: (Booking?) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val bookingsCollection = db.collection("bookings")

        val bookingRef = bookingsCollection.document(bookingId)

        bookingRef.get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    // Convert the document to a Booking object
                    val booking = document.toObject(Booking::class.java)
                    booking?.id = document.id

                    // Invoke the callback with the retrieved court (or null if not found)
                    callback(booking)
                } else {
                    // Document with the specified ID not found
                    callback(null)
                }
            }
            .addOnFailureListener { exception ->
                Log.e("GameDetailsFragment", "Firestore query failed: $exception")
                // Handle errors here
                callback(null)
            }
    }
}
