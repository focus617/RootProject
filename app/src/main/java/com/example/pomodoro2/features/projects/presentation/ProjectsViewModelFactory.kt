package com.example.pomodoro2.features.projects.presentation

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pomodoro2.features.infra.database.ProjectDAO

/**
 * This is pretty much boiler plate code for a ViewModel Factory.
 *
 * Provides the ProjectDAO and context to the ViewModel.
 */
class ProjectsViewModelFactory(
    private val dataSource: ProjectDAO,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(ProjectsViewModel::class.java)) {
            return ProjectsViewModel(dataSource, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}