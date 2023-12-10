package com.example.paddel_app.ui.bookCourt

import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import com.example.paddel_app.MainActivity
import com.example.paddel_app.R
import com.example.paddel_app.databinding.FragmentBookCourtBinding
import com.example.paddel_app.databinding.FragmentPlayBinding
import com.example.paddel_app.ui.play.PlayViewModel

class BookCourtFragment : Fragment() {

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

        val btnCloseFragment: Button = binding.btnCloseFragment
        btnCloseFragment.setOnClickListener {
            // Close the fragment

        }


        return root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}