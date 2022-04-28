package com.example.lab04.ui.calories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CaloriesViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is calories Fragment"
    }
    val text: LiveData<String> = _text
}