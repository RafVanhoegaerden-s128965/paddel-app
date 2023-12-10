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
import org.w3c.dom.Text

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

        //region UserName
        val userNameTextView: TextView = binding.usernameProfile
        profileViewModel.userName.observe(viewLifecycleOwner, Observer { userName ->
            userName?.let {
                userNameTextView.text = it
            }
        })
        //endregion

        //region BestHand
        val bestHandTextView: TextView = binding.bestHandTextView
        profileViewModel.bestHand.observe(viewLifecycleOwner, Observer { bestHand ->
            bestHand?.let {
                bestHandTextView.text = it.toString()
                Log.d("ProfileViewModel", "Best hand: ${it.toString()}" )
            }
        })

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
        val courtPositionTextView: TextView = binding.courtPositionTextView
        profileViewModel.courtPosition.observe(viewLifecycleOwner, Observer { courtPosition ->
            courtPosition?.let {
                courtPositionTextView.text = it.toString()
            }
        })

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
        val matchTypeTextView: TextView = binding.matchTypeTextView
        profileViewModel.matchType.observe(viewLifecycleOwner, Observer { matchType ->
            matchType?.let {
                matchTypeTextView.text = it.toString()
            }
        })

        val comptetitveBtn: Button = binding.comptetitveBtn
        comptetitveBtn.setOnClickListener(){
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
        val preferredTimeTextView: TextView = binding.preferredTimeTextView
        profileViewModel.preferredTime.observe(viewLifecycleOwner, Observer { preferredTime ->
            preferredTime?.let {
                preferredTimeTextView.text = it.toString()
            }
        })

        val morningBtn: Button = binding.morningBtn
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

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
