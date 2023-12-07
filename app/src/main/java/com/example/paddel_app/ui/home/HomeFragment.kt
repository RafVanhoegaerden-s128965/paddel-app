package com.example.paddel_app.ui.home

import HomeViewModel
import android.os.Bundle
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.PopupWindow
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.paddel_app.MainActivity
import com.example.paddel_app.R
import com.example.paddel_app.databinding.FragmentHomeBinding
import com.example.paddel_app.model.Court

class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var homeViewModel: HomeViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        homeViewModel = ViewModelProvider(this).get(HomeViewModel::class.java)

        val btnMakeReservation: Button = binding.btnMakeReservation
        btnMakeReservation.setOnClickListener {
            // Call the function to get and display the court list
            (activity as? MainActivity)?.getCourts { courts ->
                val courtNames = courts.map { it.name }
                showCourtListPopup(courtNames)
            }
        }
    }

    private fun showCourtListPopup(courtList: List<String>) {
        val popupView = layoutInflater.inflate(R.layout.popup_court, null)

        val listView: ListView = popupView.findViewById(R.id.listViewCourts)
        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, courtList)
        listView.adapter = adapter

        val popupWindow = PopupWindow(
            popupView,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )

        // Set the dismiss listener
        popupWindow.setOnDismissListener {
            // Handle dismiss event if needed
        }

        // Show the popup at the center of the screen
        popupWindow.showAtLocation(requireView(), Gravity.CENTER, 0, 0)
    }
}
