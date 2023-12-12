package com.example.paddel_app.ui.book_court

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.paddel_app.model.Court

class BookCourtViewModel : ViewModel() {
    private val _courtsList = MutableLiveData<List<Court>>()

    fun setCourtsList(courts: List<Court>) {
        Log.d("BookCourtViewModel", "Court: ${_courtsList.value}")
        _courtsList.value = courts
    }

    fun getCourtsList(): LiveData<List<Court>> {
        return _courtsList
    }
}