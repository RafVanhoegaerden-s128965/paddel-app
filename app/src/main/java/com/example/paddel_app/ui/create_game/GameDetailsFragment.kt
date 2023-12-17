package com.example.paddel_app.ui.create_game

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.paddel_app.databinding.FragmentCreateGameBinding
import com.example.paddel_app.databinding.FragmentGameDetailsBinding
import com.example.paddel_app.enum.GenderType
import com.example.paddel_app.enum.Hand
import com.example.paddel_app.enum.MatchType
import com.example.paddel_app.enum.PreferredTime
import com.google.android.material.snackbar.Snackbar

class GameDetailsFragment : Fragment() {

    private var _binding: FragmentGameDetailsBinding? = null

    private lateinit var gameDetailsViewModel: GameDetailsViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentGameDetailsBinding.inflate(inflater, container, false)
        val root: View = binding.root

        gameDetailsViewModel = ViewModelProvider(this).get(GameDetailsViewModel::class.java)

        //region Up-Button
        // Go back to original fragment logic
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        //endregion

        //region MatchType
        val matchTypeTextView: TextView = binding.matchTypeTextView
        gameDetailsViewModel.matchType.observe(viewLifecycleOwner, Observer { matchType ->
            matchType?.let {
                val lowerCaseMatchType = it.name.toLowerCase().capitalize()
                matchTypeTextView.text = lowerCaseMatchType
                Log.d("GameDetailsFragment", "Match Type: ${it.toString()}" )
            }
        })
        val competitiveBtn: Button = binding.comptetiveBtn
        competitiveBtn.setOnClickListener() {
            gameDetailsViewModel.setMatchType(MatchType.COMPETITIVE)
        }
        val friendlyBtn: Button = binding.friendlyBtn
        friendlyBtn.setOnClickListener() {
            gameDetailsViewModel.setMatchType(MatchType.FRIENDLY)
        }
        //endregion

        //region GenderType
        val genderTypeTextView: TextView = binding.genderTextView
        gameDetailsViewModel.genderType.observe(viewLifecycleOwner, Observer { genderType ->
            genderType?.let {
                val displayText = when (it) {
                    GenderType.MEN_ONLY -> "Men Only"
                    GenderType.WOMEN_ONLY -> "Women Only"
                    else -> it.name.toLowerCase().capitalize()
                }

                genderTypeTextView.text = displayText

                Log.d("GameDetailsFragment", "Gender Type: ${it.toString()}" )
            }
        })

        val allPlayersBtn: Button = binding.allBtn
        allPlayersBtn.setOnClickListener() {
            gameDetailsViewModel.setGenderType(GenderType.ALL)
        }
        val mixedBtn: Button = binding.mixedBtn
        mixedBtn.setOnClickListener() {
            gameDetailsViewModel.setGenderType(GenderType.MIXED)
        }
        // TODO Switch button if user is Male or Female
        val menBtn: Button = binding.menBtn
        menBtn.setOnClickListener() {
            gameDetailsViewModel.setGenderType(GenderType.MEN_ONLY)
        }
        val womenBtn: Button = binding.womenBtn
        womenBtn.setOnClickListener() {
            gameDetailsViewModel.setGenderType(GenderType.WOMEN_ONLY)
        }
        //endregion

        // TODO Fix error, lateinit property Booking has not been initialized
        val confirmMatchBtn: Button = binding.confirmBtn
        confirmMatchBtn.setOnClickListener(){
            // Call createGame function
            gameDetailsViewModel.createGame()

            // Add logic to let the user know that booking is done
            showGameSuccessMessage()

            // Navigate to Play Page
            findNavController().navigateUp()
        }

        return root
    }

    private fun showGameSuccessMessage() {
        val rootView = requireActivity().findViewById<View>(android.R.id.content)
        Snackbar.make(rootView, "New game created!", Snackbar.LENGTH_LONG).show()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            android.R.id.home -> {
                // Handle the Up-button click
                findNavController().navigateUp()
                return true
            }
            // Handle other menu items if needed
            else -> return super.onOptionsItemSelected(item)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}