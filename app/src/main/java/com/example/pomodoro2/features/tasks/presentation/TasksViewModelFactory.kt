package com.example.pomodoro2.features.tasks.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pomodoro2.features.tasks.domain.TaskInteractors

/**
 * This is pretty much boiler plate code for a ViewModel Factory.
 * Provides the context to the ViewModel.
 */
object TasksViewModelFactory: ViewModelProvider.Factory {

    lateinit var application: Application
    lateinit var dependencies: TaskInteractors

    fun inject(application: Application, dependencies: TaskInteractors) {
        TasksViewModelFactory.application = application
        TasksViewModelFactory.dependencies = dependencies
    }

    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TasksViewModel::class.java)) {
            return TasksViewModel(application, dependencies) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}