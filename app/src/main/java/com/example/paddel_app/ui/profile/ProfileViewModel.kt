package com.example.paddel_app.ui.profile

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paddel_app.enum.Hand
import com.example.paddel_app.model.User

class ProfileViewModel : ViewModel() {

    // Private
    private val _userName = MutableLiveData<String>()

    // Public
    val userName: MutableLiveData<String> get() = _userName

    // Functions
    fun setUserName(user: User) {
        Log.d("ProfileViewModel.SetUserName", "User: ${user.firstName} ${user.lastName}")

        val userName = "${user.firstName} ${user.lastName}"

        _userName.value = userName
    }

    fun getUserName(): String? {
        return _userName.value
    }

    fun updateBestHand(besthand: Hand) {
        // TODO Add logic to add to firebase
    }
}
