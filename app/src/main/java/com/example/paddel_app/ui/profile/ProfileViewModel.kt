package com.example.paddel_app.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    // TODO: Implement the ViewModel

    //<editor-fold desc="Profile Name">
    private val _name = MutableLiveData<String>().apply { value = "" }
    var textName: LiveData<String> = _name

    fun getName(name: String) {
        Log.d("User.Name", name)
        _name.value = name
    }
    //</editor-fold>
}