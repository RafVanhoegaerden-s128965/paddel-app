package com.example.paddel_app.ui.play

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paddel_app.model.Court

class PlayViewModel : ViewModel() {
    private val _courtsList = MutableLiveData<List<Court>>()

    fun setCourtsList(courts: List<Court>) {
            Log.d("PlayViewModel", "Court: ${_courtsList.value}")
        _courtsList.value = courts
    }

    fun getCourtsList(): LiveData<List<Court>> {
        return _courtsList
    }
}
