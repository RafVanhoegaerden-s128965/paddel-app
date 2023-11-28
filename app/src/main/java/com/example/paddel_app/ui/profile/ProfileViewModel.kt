package com.example.paddel_app.ui.profile

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ProfileViewModel : ViewModel() {
    // TODO: Implement the ViewModel
    private val _text = MutableLiveData<String>().apply { value = "initial_value" }
    var text: LiveData<String> = _text

    fun testPassingName(name: String) {
        Log.d("testPassingName", name)
        _text.value = name
    }




}