package com.example.paddel_app.ui.profile

import android.os.Binder
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import com.example.paddel_app.MainActivity
import com.example.paddel_app.R
import com.example.paddel_app.databinding.FragmentProfileBinding
import com.example.paddel_app.enum.Hand
import com.example.paddel_app.model.User
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
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //val user =

        //val profileModelFactory = ProfileModelFactory(user)

        profileViewModel = ViewModelProvider(this, ).get(ProfileViewModel::class.java)

        // TextViews
        val userNameTextView: TextView = binding.usernameProfile
        val bestHandTextView: TextView = binding.bestHandTextView

        // Buttons
        //region BestHandButtons
        val leftHandBtn: Button = binding.leftHandBtn
        leftHandBtn.setOnClickListener(){
            profileViewModel.updateBestHand(Hand.LEFT)
        }
        val ambidextrousBtn: Button = binding.ambidextrousBtn
        ambidextrousBtn.setOnClickListener(){
            profileViewModel.updateBestHand(Hand.AMBIDEXTROUS)
        }
        val rightHandBtn: Button = binding.rightHandBtn
        rightHandBtn.setOnClickListener(){
            profileViewModel.updateBestHand(Hand.RIGHT)
        }
        //endregion

        // Updates


        // Observables
        profileViewModel.userName.observe(viewLifecycleOwner) {userName ->
            userNameTextView.text = userName
        }

//        profileViewModel.bestHand.observe(viewLifecycleOwner) {bestHand ->
//            bestHandTextView.text = bestHand
//        }

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}