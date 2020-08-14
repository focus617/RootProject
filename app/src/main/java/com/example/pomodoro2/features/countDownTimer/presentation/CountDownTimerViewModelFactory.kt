package com.example.pomodoro2.features.countDownTimer.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pomodoro2.features.countDownTimer.domain.CountDownTimerInteractors
import com.example.pomodoro2.features.dashboard.domain.DashboardInteractors

/**
 * This is pretty much boiler plate code for a ViewModel Factory.
 * Provides the context to the ViewModel.
 */
object CountDownTimerViewModelFactory: ViewModelProvider.Factory {

    lateinit var application: Application
    lateinit var dependencies: CountDownTimerInteractors

    fun inject(application: Application, dependencies: CountDownTimerInteractors) {
        CountDownTimerViewModelFactory.application = application
        CountDownTimerViewModelFactory.dependencies = dependencies
    }

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(CountDownTimerViewModel::class.java)) {
            return CountDownTimerViewModel(application, dependencies) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}