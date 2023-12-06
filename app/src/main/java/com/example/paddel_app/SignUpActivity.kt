package com.example.paddel_app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.example.paddel_app.databinding.ActivitySignUpBinding
import com.example.paddel_app.model.User
import com.google.firebase.firestore.FirebaseFirestore

class SignUpActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth

    private lateinit var binding: ActivitySignUpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        binding = ActivitySignUpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnSignUp.setOnClickListener {
            val firstName = binding.etFirstName.text.toString()
            val lastName = binding.etLastName.text.toString()
            val birthDate = binding.etDOB.text.toString()
            val gender = binding.etGender.text.toString()
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()


            if (firstName.isNotEmpty() && lastName.isNotEmpty() && gender.isNotEmpty() && birthDate.isNotEmpty() && email.isNotEmpty() && password.isNotEmpty()) {
                auth.createUserWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            // Create Firestore instance
                            val db = FirebaseFirestore.getInstance()
                            val usersCollection = db.collection("user")

                            // Add user to collection
                            val user = auth.currentUser
                            val userId = user!!.uid

                            val newUser = User(
                                userId,firstName, lastName, email, gender, birthDate)
                            // Set user fields
                            val userData = hashMapOf(
                                "firstName" to newUser.firstName,
                                "lastName" to newUser.lastName,
                                "email" to newUser.email,
                                "gender" to newUser.gender,
                                "birthDate" to newUser.birthDate,
                                "bestHand" to newUser.bestHand,
                                "courtPosition" to newUser.courtPosition,
                                "matchType" to newUser.matchType,
                                "preferredTime" to newUser.preferredTime,
                            )

                            // Add user data to Firestore userID as documentID
                            usersCollection.document(userId)
                                .set(userData)
                                .addOnSuccessListener {
                                    // User data added successfully
                                    // You can handle the transition to the next screen here
                                    val intent = Intent(this, LoginActivity::class.java)
                                    startActivity(intent)
                                    finish()
                                }
                                .addOnFailureListener { e ->
                                    // Handle errors
                                    Log.d("Firestore", "Error adding user data", e)
                                    Toast.makeText(this, "Error: Registration failed.", Toast.LENGTH_SHORT).show()
                                }

                        } else {
                            if (password.length < 6){
                                Toast.makeText(this, "Password must be atleast 6 characters.", Toast.LENGTH_SHORT).show()
                            }

                            Toast.makeText(this, "Registration failed. Try again.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Fill out all fields.", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnBackToLogin.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
