package com.example.paddel_app.ui.profile

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.paddel_app.R
import com.example.paddel_app.databinding.FragmentEditProfileBinding

class EditProfileFragment : Fragment() {

    private var _binding: FragmentEditProfileBinding? = null
    private lateinit var editProfileViewModel: EditProfileViewModel

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentEditProfileBinding.inflate(inflater, container, false)
        val root: View = binding.root

        editProfileViewModel = ViewModelProvider(this).get(EditProfileViewModel::class.java)

        // Observer voor het bijwerken van de UI wanneer gebruikersgegevens veranderen
        editProfileViewModel.user.observe(viewLifecycleOwner) { user ->
            user?.let {
                binding.etFirstName.setText(it.firstName)
                binding.etLastName.setText(it.lastName)
                binding.etEmail.setText(it.email)
                binding.etDOB.setText(it.birthDate)
                binding.spinnerGender.setSelection(getGenderPosition(it.gender))
            }
        }

        binding.btnUpdateProfile.setOnClickListener {
            updateProfile()
        }
        return root
    }

    private fun updateProfile() {
        val firstName = binding.etFirstName.text.toString()
        val lastName = binding.etLastName.text.toString()
        val email = binding.etEmail.text.toString()
        val birthDate = binding.etDOB.text.toString()
        val gender = resources.getStringArray(R.array.gender_options)[binding.spinnerGender.selectedItemPosition]
        editProfileViewModel.updateUserProfile(firstName, lastName, email, birthDate, gender)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
    private fun getGenderPosition(gender: String): Int {
        val genders = resources.getStringArray(R.array.gender_options)
        return genders.indexOf(gender)
    }
}
