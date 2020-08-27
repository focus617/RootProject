package com.example.pomodoro2.features.countDownTimer.domain

import android.os.CountDownTimer
import android.text.format.DateUtils
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import timber.log.Timber

/**
 * 倒计时计数器
 */
class TaskCountDownTimer(private val initialSecond: Long){

    // Time when the game is over
    private val DONE = 0L

    // Countdown time interval
    private val ONE_SECOND = 1000L

    // observable TimeUp Event
    val eventTimeUp: LiveData<Boolean>
        get() = _eventTimeUp
    private var _eventTimeUp = MutableLiveData<Boolean>()


    private var timer: CountDownTimer? = null

    // current countdown Timer value
    private lateinit var _secondUntilFinished: MutableLiveData<Long>

    // observable current countdown Timer value
    val secondUntilFinished: LiveData<Long>
        get() = _secondUntilFinished


    init{
        _secondUntilFinished.value = initialSecond
    }


    fun launchTimerUseCase(){
        if(_secondUntilFinished.value == null) return
        if(_secondUntilFinished.value!! <= DONE) return

        timer?.cancel()
        _eventTimeUp.value = false

        // Creates a timer which triggers countdown timer per second
        timer = object: CountDownTimer(_secondUntilFinished.value!! * ONE_SECOND, ONE_SECOND){

            override fun onFinish() {
                Timber.d("CountDownTimer onFinish:")
                _secondUntilFinished.value = DONE
                _eventTimeUp.value = true
            }

            override fun onTick(millisUntilFinished: Long) {
                _secondUntilFinished.value = millisUntilFinished / ONE_SECOND

                val currentTimeString = DateUtils.formatElapsedTime(_secondUntilFinished.value?:DONE)
                Timber.d("CountDownTimer onTick - $currentTimeString")
            }

        }.start()

    }

    fun pauseTimerUseCase(){
        Timber.d("onPauseTimer: ")
        timer?.cancel()
    }

    fun resumeTimerUseCase(){
        Timber.d("onResumeTimer: ")
        launchTimerUseCase()
    }

    fun resetTimerUseCase(){
        Timber.d("onResetTimer: ")
        _secondUntilFinished.value = initialSecond
        launchTimerUseCase()
    }
}

