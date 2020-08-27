package com.example.pomodoro2.features.countDownTimer.presentation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pomodoro2.features.countDownTimer.domain.CountDownTimerInteractors
import com.example.pomodoro2.features.countDownTimer.domain.TaskCountDownTimer
import com.example.pomodoro2.framework.base.BaseViewModel

class CountDownTimerViewModel(
    application: Application,
    val countDownTimerInteractors: CountDownTimerInteractors
) : BaseViewModel(application) {

    // TODO: getSelectedTask - Acitivity, then get it's time counter
    val timer = TaskCountDownTimer(300L)

    fun getCurrentActivityTile() = "测试计时器"


    private val _text = MutableLiveData<String>().apply {
        value = "This is CountDown Timer Fragment"
    }
    val text: LiveData<String> = _text
}