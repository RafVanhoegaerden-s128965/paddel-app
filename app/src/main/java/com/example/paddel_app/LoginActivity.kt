package com.example.paddel_app

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.paddel_app.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        auth = FirebaseAuth.getInstance()

        binding.btnSignIn.setOnClickListener {
            val email = binding.etEmail.text.toString()
            val password = binding.etPassword.text.toString()

            if (email.isNotEmpty() && password.isNotEmpty()) {
                auth.signInWithEmailAndPassword(email, password)
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {
                            // Gebruiker succesvol ingelogd
                            Toast.makeText(this, "Inloggen succesvol", Toast.LENGTH_SHORT).show()
                            // Start MainActivity en sluit com.example.paddel_app.LoginActivity
                            startActivity(Intent(this, MainActivity::class.java))
                            finish()
                        } else {
                            // Inloggen mislukt, toon een foutmelding
                            Toast.makeText(this, "Inloggen mislukt. Controleer je gegevens.", Toast.LENGTH_SHORT).show()
                        }
                    }
            } else {
                Toast.makeText(this, "Vul alle velden in.", Toast.LENGTH_SHORT).show()
            }
        }
        binding.btnToSignUp.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
