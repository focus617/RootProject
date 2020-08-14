package com.example.pomodoro2.features.activities.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pomodoro2.features.activities.domain.ActivityInteractors
import com.example.pomodoro2.features.tasks.domain.TaskInteractors

/**
 * This is pretty much boiler plate code for a ViewModel Factory.
 * Provides the context to the ViewModel.
 */
object ActivitiesViewModelFactory: ViewModelProvider.Factory {

    lateinit var application: Application
    lateinit var dependencies: ActivityInteractors

    fun inject(application: Application, dependencies: ActivityInteractors) {
        ActivitiesViewModelFactory.application = application
        ActivitiesViewModelFactory.dependencies = dependencies
    }

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivitiesViewModel::class.java)) {
            return ActivitiesViewModel(application, dependencies) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}