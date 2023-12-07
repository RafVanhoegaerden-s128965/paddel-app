package com.example.paddel_app

import HomeViewModel
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.example.paddel_app.databinding.ActivityMainBinding
import com.example.paddel_app.model.User
import com.example.paddel_app.ui.profile.ProfileViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var homeViewModel: HomeViewModel
    private lateinit var profileViewModel: ProfileViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)
        profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        //region NavigationBar
        val navView: BottomNavigationView = binding.navView

        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        // Passing each menu ID as a set of Ids because each
        // menu should be considered as top level destinations.
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home, R.id.navigation_dashboard, R.id.navigation_profile
            )
        )

        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
        //endregion

        getUser { user ->
            if (user != null) {
                Log.d("MainActivity", "User: ${user.firstName} ${user.lastName}")
                profileViewModel.setUser(user)
            } else {
                Log.e("MainActivity", "User is null")
            }
        }
    }

    private fun getUser(callback: (User?) -> Unit) {
        // Create Firestore instance
        val db = FirebaseFirestore.getInstance()

        // Get userId
        val user = FirebaseAuth.getInstance().currentUser
        if (user != null) {
            Log.d("currentUser", "user: ${user.email.toString()}")
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
}
