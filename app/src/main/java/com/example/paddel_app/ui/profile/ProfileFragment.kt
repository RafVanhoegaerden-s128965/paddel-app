package com.example.paddel_app.ui.profile

import android.os.Bundle
import android.service.autofill.FieldClassification.Match
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
import com.example.paddel_app.enum.CourtPosition
import com.example.paddel_app.enum.Hand

import com.example.paddel_app.enum.MatchType
import com.example.paddel_app.enum.PreferredTime
import com.example.paddel_app.model.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.toObject
import kotlinx.coroutines.launch

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

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        profileViewModel = ViewModelProvider(this, ).get(ProfileViewModel::class.java)

        //region TextViews
        val userNameTextView: TextView = binding.usernameProfile
        val bestHandTextView: TextView = binding.bestHandTextView
        val courtPositionTextView: TextView = binding.courtPositionTextView
        val matchTypeTextView: TextView = binding.matchTypeTextView
        val preferredTimeTextView: TextView = binding.preferredTimeTextView
        //endregion

        //region Buttons
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

        //region CourtPosition
        val foreHandBtn: Button = binding.foreHandBtn
        foreHandBtn.setOnClickListener(){
            profileViewModel.updateCourtPosition(CourtPosition.FOREHAND)
        }

        val backHandBtn: Button = binding.backHandBtn
        backHandBtn.setOnClickListener(){
            profileViewModel.updateCourtPosition(CourtPosition.BACKHAND)
        }
        //endregion

        //region MatchType
        val competitiveBtn: Button = binding.competitiveBtn
        competitiveBtn.setOnClickListener(){
            profileViewModel.updateMatchType(MatchType.COMPETITIVE)
        }
        val friendlyBtn: Button = binding.friendlyBtn
        friendlyBtn.setOnClickListener(){
            profileViewModel.updateMatchType(MatchType.FRIENDLY)
        }
        val bothMatchTypeBtn: Button = binding.bothMatchTypeBtn
        bothMatchTypeBtn.setOnClickListener(){
            profileViewModel.updateMatchType(MatchType.BOTH)
        }
        //endregion

        //region PreferredTime
        val morningBtn: Button = binding.morginBtn
        morningBtn.setOnClickListener(){
            profileViewModel.updatePreferredTime(PreferredTime.MORNING)
        }
        val afternoonBtn: Button = binding.afternoonBtn
        afternoonBtn.setOnClickListener(){
            profileViewModel.updatePreferredTime(PreferredTime.AFTERNOON)
        }
        val eveningBtn: Button = binding.eveningBtn
        eveningBtn.setOnClickListener(){
            profileViewModel.updatePreferredTime(PreferredTime.EVENING)
        }
        //endregion

        //endregion

        //region Observables

        profileViewModel.bestHand.observe(viewLifecycleOwner) {bestHand ->
            //TODO this code not working
            bestHandTextView.text = bestHand.toString()

            // Change button text color if value is in firebase
            if (bestHand == Hand.LEFT){
                leftHandBtn.setTextColor(resources.getColor(R.color.white))
            } else if (bestHand == Hand.AMBIDEXTROUS){
                ambidextrousBtn.setTextColor(resources.getColor(R.color.white))
            } else if (bestHand == Hand.RIGHT){
                rightHandBtn.setTextColor(resources.getColor(R.color.white))
            }
        }

        profileViewModel.courtPosition.observe(viewLifecycleOwner) {courtPosition ->
            //TODO this code not working
            courtPositionTextView.text = courtPosition.toString()
        }

        profileViewModel.matchType.observe(viewLifecycleOwner) {matchType ->
            //TODO this code not working
            matchTypeTextView.text = matchType.toString()
        }

        profileViewModel.preferredTime.observe(viewLifecycleOwner) {preferredTime ->
            //TODO this code not working
            preferredTimeTextView.text = preferredTime.toString()
        }
        //endregion
        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
