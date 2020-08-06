package com.example.pomodoro2.features.activities.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.pomodoro2.core.platform.BaseViewModel
import com.example.pomodoro2.core.platform.SingleLiveEvent

/**
* ViewModel for the activities screen.
*/
class ActivitiesViewModel(
    //private val getTasksUseCase: GetTasksUseCase
) : BaseViewModel() {

    // Event for launch CountDownTimer fragment
    private val _launchTimerEvent = MutableLiveData<SingleLiveEvent<Unit>>()
    val launchTimerEvent: LiveData<SingleLiveEvent<Unit>> = _launchTimerEvent

    /**
     * Called by the Data Binding library and the Start Button's click listener.
     */
    fun launchTimer() {
        _launchTimerEvent.value = SingleLiveEvent(Unit)
    }

    // TODO: remove in future
    private val _text = MutableLiveData<String>().apply {
        value = "This is activity Fragment"
    }
    val text: LiveData<String> = _text
}