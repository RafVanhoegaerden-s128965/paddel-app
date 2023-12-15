package com.example.paddel_app.ui.book_court

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.paddel_app.MainActivity
import com.example.paddel_app.databinding.FragmentBookCourtBinding
import com.example.paddel_app.ui.play.ClubsAdapter

class BookCourtFragment : Fragment() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: ClubsAdapter
    private var _binding: FragmentBookCourtBinding? = null
    private lateinit var bookCourtViewModel: BookCourtViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookCourtBinding.inflate(inflater, container, false)
        val root: View = binding.root

        bookCourtViewModel = ViewModelProvider(this).get(BookCourtViewModel::class.java)

        //region Up-Button
        // Go back to original fragment logic
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        //endregion

        recyclerView = binding.recyclerView
        adapter = ClubsAdapter()

        recyclerView.layoutManager = LinearLayoutManager(requireContext())
        recyclerView.adapter = adapter

        // Load Courts
        (activity as? MainActivity)?.getCourts { courts ->
            bookCourtViewModel.setCourtsList(courts)
        }

        // Observe changes in the courts list
        bookCourtViewModel.getCourtsList().observe(viewLifecycleOwner, Observer { courtsList ->
            // Update the UI with the new list of courts
            adapter.submitList(courtsList)
            for (court in courtsList) {
                //Log.d("BookCourtFragment", "Court: ${court}")
                Log.d("BookCourtFragment","Court ID: ${court.id}")
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