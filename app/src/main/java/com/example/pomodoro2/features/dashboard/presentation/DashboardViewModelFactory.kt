package com.example.pomodoro2.features.dashboard.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pomodoro2.features.dashboard.domain.DashboardInteractors

/**
 * This is pretty much boiler plate code for a ViewModel Factory.
 * Provides the context to the ViewModel.
 */
object DashboardViewModelFactory: ViewModelProvider.Factory {

    lateinit var application: Application
    lateinit var dependencies: DashboardInteractors

    fun inject(application: Application, dependencies: DashboardInteractors) {
        DashboardViewModelFactory.application = application
        DashboardViewModelFactory.dependencies = dependencies
    }

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DashboardViewModel::class.java)) {
            return DashboardViewModel(application, dependencies) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}