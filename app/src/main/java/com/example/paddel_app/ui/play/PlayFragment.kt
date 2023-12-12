package com.example.paddel_app.ui.play

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.Navigation.findNavController
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paddel_app.MainActivity
import com.example.paddel_app.R
import com.example.paddel_app.databinding.FragmentPlayBinding

class PlayFragment : Fragment() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: CustomAdapter

    private var _binding: FragmentPlayBinding? = null
    private lateinit var playViewModel: PlayViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        _binding = FragmentPlayBinding.inflate(inflater, container, false)
        val root: View = binding.root

        playViewModel = ViewModelProvider(this).get(PlayViewModel::class.java)

        recyclerView = binding.recyclerView
        adapter = CustomAdapter()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Load Courts
        (activity as? MainActivity)?.getCourts { courts ->
            playViewModel.setCourtsList(courts)
        }

        // Observe changes in the courts list
        playViewModel.getCourtsList().observe(viewLifecycleOwner, Observer { courtsList ->
            // Update the UI with the new list of courts
            adapter.submitList(courtsList)
            for (court in courtsList) {
                Log.d("PlayFragment", "Court: ${court}")
            }
        })

        val btnMakeReservation: Button = binding.btnBookCourt
        btnMakeReservation.setOnClickListener {
            // Call the function to get and display the court list
            findNavController().navigate(R.id.navigation_bookCourt)
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
