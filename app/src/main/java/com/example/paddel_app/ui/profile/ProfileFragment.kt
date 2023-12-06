package com.example.paddel_app.ui.profile

import android.os.Binder
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.service.autofill.FieldClassification.Match
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.lifecycleScope
import com.example.paddel_app.MainActivity
import com.example.paddel_app.R
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
    private lateinit var profileViewModel : ProfileViewModel

    private val binding get() = _binding!!

    private var currentUser : User? = null

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

        //val profileModelFactory = ProfileModelFactory(user)

        profileViewModel = ViewModelProvider(this, ).get(ProfileViewModel::class.java)

        //TODO Get current User -- In plaats van profileViewModel functie zou die de user vanuit de MainActivity moeten ophalen
        profileViewModel.getUser { user ->
            if (user != null) {
                Log.d("ProfileFragment", "User Name: ${user.firstName} ${user.lastName}")
                Log.d("ProfileFragment", "User Id: ${user.id}") //TODO ID is empty???

                currentUser = user
            } else {
                Log.e("ProfileFragment", "User is null")
            }
        }

        //region TextViews
        val userNameTextView: TextView = binding.usernameProfile
        val bestHandTextView: TextView = binding.bestHandTextView
        val courtPositionTextView: TextView = binding.courtPositionTextView
        val matchTypeTextView: TextView = binding.matchTypeTextView
        val preferredTimeTextView: TextView = binding.preferredTimeTextView
        //endregion

        //region Buttons
        //region BestHandButtons
        val leftHandBtn: Button = binding.leftHandBtn
        leftHandBtn.setOnClickListener(){
            profileViewModel.updateBestHand(currentUser!!.id, Hand.LEFT)
        }
        val ambidextrousBtn: Button = binding.ambidextrousBtn
        ambidextrousBtn.setOnClickListener(){
            profileViewModel.updateBestHand(currentUser!!.id, Hand.AMBIDEXTROUS)
        }
        val rightHandBtn: Button = binding.rightHandBtn
        rightHandBtn.setOnClickListener(){
            profileViewModel.updateBestHand(currentUser!!.id, Hand.RIGHT)
        }
        //endregion

        //region CourtPosition
        val foreHandBtn: Button = binding.foreHandBtn
        foreHandBtn.setOnClickListener(){
            profileViewModel.updateCourtPosition(currentUser!!.id, CourtPosition.FOREHAND)
        }

        val backHandBtn: Button = binding.backHandBtn
        backHandBtn.setOnClickListener(){
            profileViewModel.updateCourtPosition(currentUser!!.id, CourtPosition.BACKHAND)
        }
        //endregion

        //region MatchType
        val competitiveBtn: Button = binding.competitiveBtn
        competitiveBtn.setOnClickListener(){
            profileViewModel.updateMatchType(currentUser!!.id, MatchType.COMPETITIVE)
        }
        val friendlyBtn: Button = binding.friendlyBtn
        friendlyBtn.setOnClickListener(){
            profileViewModel.updateMatchType(currentUser!!.id, MatchType.FRIENDLY)
        }
        val bothMatchTypeBtn: Button = binding.bothMatchTypeBtn
        bothMatchTypeBtn.setOnClickListener(){
            profileViewModel.updateMatchType(currentUser!!.id, MatchType.BOTH)
        }
        //endregion

        //region PreferredTime
        val morningBtn: Button = binding.morginBtn
        morningBtn.setOnClickListener(){
            profileViewModel.updatePreferredTime(currentUser!!.id, PreferredTime.MORNING)
        }
        val afternoonBtn: Button = binding.afternoonBtn
        afternoonBtn.setOnClickListener(){
            profileViewModel.updatePreferredTime(currentUser!!.id, PreferredTime.AFTERNOON)
        }
        val eveningBtn: Button = binding.eveningBtn
        eveningBtn.setOnClickListener(){
            profileViewModel.updatePreferredTime(currentUser!!.id, PreferredTime.EVENING)
        }
        //endregion

        //endregion

        //region Observables
        profileViewModel.userName.observe(viewLifecycleOwner) {userName ->
            userNameTextView.text = userName
        }

        profileViewModel.bestHand.observe(viewLifecycleOwner) {bestHand ->
            bestHandTextView.text = bestHand.toString()
        }

        profileViewModel.courtPosition.observe(viewLifecycleOwner) {courtPosition ->
            courtPositionTextView.text = courtPosition.toString()
        }

        profileViewModel.matchType.observe(viewLifecycleOwner) {matchType ->
            matchTypeTextView.text = matchType.toString()
        }

        profileViewModel.preferredTime.observe(viewLifecycleOwner) {preferredTime ->
            preferredTimeTextView.text = preferredTime.toString()
        }
        //endregion
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}