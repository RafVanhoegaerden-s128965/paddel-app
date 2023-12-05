package com.example.paddel_app.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paddel_app.MainActivity
import com.example.paddel_app.model.User

class ProfileViewModel() : ViewModel() {
    // Variables
    private val _firstName = MutableLiveData<String>()
    private val _lastName = MutableLiveData<String>()
    private val _userName = MutableLiveData<String>()
    val userName: MutableLiveData<String> get() = _userName

    // Functions
    fun setUser(user : User){
        Log.d("ProfileViewModel", "User: ${user.firstName} ${user.lastName}")
        _firstName.value = user.firstName
        _lastName.value = user.lastName
    }
    fun setUserName(){
        Log.d("ProfileViewModel.SetUserName", "User: ${_firstName.value} ${_lastName.value}")

        val userName = "${_firstName.value} ${_lastName.value}"

        _userName.value = userName
    }
}