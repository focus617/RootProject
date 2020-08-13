package com.example.pomodoro2.features.countDownTimer.presentation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pomodoro2.features.tasks.domain.TaskInteractors
import com.example.pomodoro2.framework.platform.BaseViewModel

class CountDownTimerViewModel(application: Application, taskInteractors: TaskInteractors)
    : BaseViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is CountDown Timer Fragment"
    }
    val text: LiveData<String> = _text
}