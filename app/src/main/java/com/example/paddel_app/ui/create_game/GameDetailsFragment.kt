package com.example.paddel_app.ui.create_game

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.paddel_app.databinding.FragmentCreateGameBinding
import com.example.paddel_app.databinding.FragmentGameDetailsBinding

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

        val matchTypeTextView: TextView = binding.matchTypeTextView
        gameDetailsViewModel.matchType.observe(viewLifecycleOwner, Observer { matchType ->
            matchType?.let {
                val lowerCaseMatchType = it.name.toLowerCase().capitalize()
                matchTypeTextView.text = lowerCaseMatchType
                Log.d("GameDetailsFragment", "Match Type: ${it.toString()}" )
            }
        })


        val genderTypeTextView: TextView = binding.genderTextView
        gameDetailsViewModel.matchType.observe(viewLifecycleOwner, Observer { genderType ->
            genderType?.let {
                val lowerCaseGenderType = it.name.toLowerCase().capitalize()
                genderTypeTextView.text = lowerCaseGenderType
                Log.d("GameDetailsFragment", "Gender Type: ${it.toString()}" )
            }
        })

        return root
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