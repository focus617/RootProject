package com.example.pomodoro2.features.countDownTimer.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class CountDownTimerViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is CountDown Timer Fragment"
    }
    val text: LiveData<String> = _text
}