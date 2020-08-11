package com.example.pomodoro2.features.activities.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pomodoro2.features.infra.database.ProjectDAO
import com.example.pomodoro2.domain.Task

/**
 * This is pretty much boiler plate code for a ViewModel Factory.
 *
 * Provides the key for the night and the SleepDatabaseDao to the ViewModel.
 */
class ActivitiesViewModelFactory(
    private val task: Task,
    private val dataSource: ProjectDAO) : ViewModelProvider.Factory {
    // TODO:change ProjectDAO to ActivityDAO later
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ActivitiesViewModel::class.java)) {
            return ActivitiesViewModel(task, dataSource) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}