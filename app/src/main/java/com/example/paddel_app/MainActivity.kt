package com.example.paddel_app

import DiscoverViewModel
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.paddel_app.databinding.ActivityMainBinding
import com.example.paddel_app.model.Booking
import com.example.paddel_app.model.Court
import com.example.paddel_app.model.User
import com.example.paddel_app.ui.play.PlayViewModel
import com.example.paddel_app.ui.profile.ProfileViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var discoverViewModel: DiscoverViewModel
    private lateinit var playViewModel: PlayViewModel
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        discoverViewModel = ViewModelProvider(this).get(DiscoverViewModel::class.java)
        playViewModel = ViewModelProvider(this).get(PlayViewModel::class.java)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        //region NavigationBar
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_discover, R.id.navigation_play, R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        //endregion

        //region Getters
        getUser { user ->
            if (user != null) {
                Log.d("MainActivity", "User: ${user.firstName} ${user.lastName}")
                profileViewModel.setUser(user)
            } else {
                Log.e("MainActivity", "User is null")
            }
        }

        getCourts { courts ->
            for (court in courts) {
                Log.d("MainActivity", "Court: ${court}")
                playViewModel.setCourtsList(courts)
            }
        }
        //endregion
    }

    private fun getUser(callback: (User?) -> Unit) {
        // Create Firestore instance
        val db = FirebaseFirestore.getInstance()

        // Get userId
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            Log.d("currentUser", "user: ${user.email.toString()}")
        }
        val userId = user!!.uid

        // Get document
        val docRef = db.collection("user").document(userId)

        // Get the User object
        docRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                // Convert Firestore document to User object
                val user = document.toObject<User>()
                callback(user)
            } else {
                callback(null)
            }
        }.addOnFailureListener { e ->
            Log.e("MainActivity.User", "Error getting document: $e")
            callback(null)
        }
    }

    fun getCourts(callback: (List<Court>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val courtsCollection = db.collection("courts")

        courtsCollection.get()
            .addOnSuccessListener { result ->
                val courtsList = mutableListOf<Court>()

                for (document in result) {
                    // Convert each document to a Court object
                    val court = document.toObject(Court::class.java)
                    court.id = document.id
                    courtsList.add(court)
                }

                // Invoke the callback with the list of courts
                callback(courtsList)
            }
            .addOnFailureListener { exception ->
                Log.e("MainActivity", "Firestore query failed: $exception")
                // Handle errors here
            }
    }

    fun getBookings(userId: String, callback: (List<Booking>) -> Unit) {
        val db = FirebaseFirestore.getInstance()
        val bookingsCollection = db.collection("bookings")

        // Add a whereEqualTo clause to filter bookings by user ID
        bookingsCollection.whereEqualTo("userId", userId)
            .get()
            .addOnSuccessListener { result ->
                val bookingsList = mutableListOf<Booking>()

                for (document in result) {
                    // Convert each document to a Booking object
                    val booking = document.toObject(Booking::class.java)
                    booking.id = document.id
                    bookingsList.add(booking)
                }
                // Invoke the callback with the list of bookings
                callback(bookingsList)
            }
            .addOnFailureListener { exception ->
                Log.e("MainActivity", "Firestore query failed: $exception")
                // Handle errors here
            }
    }
}
