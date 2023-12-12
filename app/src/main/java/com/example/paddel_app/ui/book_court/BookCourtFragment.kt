package com.example.paddel_app.ui.book_court

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.fragment.findNavController
import com.example.paddel_app.MainActivity
import com.example.paddel_app.R
import com.example.paddel_app.databinding.FragmentBookCourtBinding
import com.example.paddel_app.databinding.FragmentPlayBinding
import com.example.paddel_app.ui.play.PlayViewModel

class BookCourtFragment : Fragment() {

    private var _binding: FragmentBookCourtBinding? = null
    private lateinit var bookCourtViewModel: BookCourtViewModel

    private lateinit var clubSpinner: Spinner

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentBookCourtBinding.inflate(inflater, container, false)
        val root: View = binding.root

        //region Up-Button
        // Go back to original fragment logic
        val activity = requireActivity() as AppCompatActivity
        activity.supportActionBar?.setDisplayHomeAsUpEnabled(true)
        setHasOptionsMenu(true)
        //endregion

        // Load Courts
        (activity as? MainActivity)?.getCourts { courts ->
            //playViewModel.setCourtsList(courts)
        }



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