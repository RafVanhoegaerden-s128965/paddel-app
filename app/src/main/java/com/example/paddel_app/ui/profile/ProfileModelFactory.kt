package com.example.paddel_app.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.paddel_app.model.User

class ProfileModelFactory(private val user: User) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProfileViewModel::class.java)) {
            return ProfileViewModel(/*user*/) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
