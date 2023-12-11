package com.example.paddel_app.ui.play

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.Gravity
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.ListView
import android.widget.PopupWindow
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paddel_app.MainActivity
import com.example.paddel_app.R
import com.example.paddel_app.databinding.FragmentHomeBinding
import com.example.paddel_app.databinding.FragmentPlayBinding
import com.example.paddel_app.model.Court

class PlayFragment : Fragment() {

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

        val btnMakeReservation: Button = binding.btnBookCourt
        btnMakeReservation.setOnClickListener {
            // Call the function to get and display the court list
            (activity as? MainActivity)?.getCourts { courts ->
                //val courtNames = courts.map { it.name }
                //showCourtListPopup(courtNames)

                //TODO Switch Activity

            }
        }

        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

//    private fun showCourtListPopup(courtList: List<String>) {
//        val popupView = layoutInflater.inflate(R.layout.popup_court, null)
//
//        val listView: ListView = popupView.findViewById(R.id.listViewCourts)
//        val adapter = ArrayAdapter(requireContext(), android.R.layout.simple_list_item_1, courtList)
//        listView.adapter = adapter
//
//        val popupWindow = PopupWindow(
//            popupView,
//            ViewGroup.LayoutParams.WRAP_CONTENT,
//            ViewGroup.LayoutParams.WRAP_CONTENT
//        )
//
//        // Set the dismiss listener
//        popupWindow.setOnDismissListener {
//            // Handle dismiss event if needed
//        }
//
//        // Show the popup at the center of the screen
//        popupWindow.showAtLocation(requireView(), Gravity.CENTER, 0, 0)
//    }
}