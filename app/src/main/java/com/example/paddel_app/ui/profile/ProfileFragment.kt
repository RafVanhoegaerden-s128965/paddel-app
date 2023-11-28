package com.example.paddel_app.ui.profile

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.paddel_app.R
import com.example.paddel_app.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private lateinit var profileViewModel : ProfileViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root
        val profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)

        val textView: TextView = binding.textProfile
        profileViewModel.text.observe(viewLifecycleOwner) {
            textView.text = it
        }

        val textName = textView.findViewById<TextView>(R.id.text_profile)

        getName( { name ->
            textName.text = name
        }, textName)

        return root
    }

    fun getName(callback: (String?) -> Unit, textView: TextView) {
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

        // Get the "name" field
        docRef.get().addOnSuccessListener { document ->
            if (document != null && document.exists()) {
                // Check if the "name" field exists in the document
                if (document.contains("name")) {
                    // Retrieve the value of the "name" field
                    val name = document.getString("name")
                    if (name != null) {
                        // Use the name as a string
                        Log.d("Profile.Name", "Name: $name")
                        val profileViewModel = ViewModelProvider(this).get(ProfileViewModel::class.java)
                        profileViewModel.testPassingName(name)
                    }
                }
            }
        }.addOnFailureListener { e ->
            Log.e("Profile.Name", "Error getting document: $e")
        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}