package com.example.paddel_app.ui.profile

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.example.paddel_app.databinding.FragmentProfileBinding
import com.example.paddel_app.enum.Hand

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null
    private lateinit var profileViewModel: ProfileViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        profileViewModel = ViewModelProvider(requireActivity()).get(ProfileViewModel::class.java)

        val userNameTextView: TextView = binding.usernameProfile
        profileViewModel.userName.observe(viewLifecycleOwner, Observer { userName ->
            userName?.let {
                userNameTextView.text = it
            }
        })

        //region BestHandButtons
        val leftHandBtn: Button = binding.leftHandBtn
        leftHandBtn.setOnClickListener() {
            profileViewModel.updateBestHand(Hand.LEFT)
        }
        val ambidextrousBtn: Button = binding.ambidextrousBtn
        ambidextrousBtn.setOnClickListener() {
            profileViewModel.updateBestHand(Hand.AMBIDEXTROUS)
        }
        val rightHandBtn: Button = binding.rightHandBtn
        rightHandBtn.setOnClickListener() {
            profileViewModel.updateBestHand(Hand.RIGHT)
        }
        //endregion
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
