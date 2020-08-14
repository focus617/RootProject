package com.example.pomodoro2.framework.platform.deprecated

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.pomodoro2.data.TaskRepository
import com.example.pomodoro2.features.tasks.domain.TaskInteractors
import com.example.pomodoro2.framework.platform.BaseViewModel

object MyViewModelFactory : ViewModelProvider.Factory {

    lateinit var application: Application
    lateinit var dependencies: TaskInteractors


    fun inject(
        application: Application,
        dependencies: TaskInteractors,
        taskRepository: TaskRepository
    ) {
        MyViewModelFactory.application = application
        MyViewModelFactory.dependencies = dependencies
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        if (BaseViewModel::class.java.isAssignableFrom(modelClass)) {
            return modelClass.getConstructor(Application::class.java, TaskInteractors::class.java)
                .newInstance(
                    application,
                    dependencies
                )
        } else {
            throw IllegalStateException("ViewModel must extend BaseViewModel")
        }
    }
}

/*
    // Usage method: Put below statement into MyApplication.onCreate()
    MyViewModelFactory.inject(
        this,
        TaskInteractors(
            CreateNewTaskUseCase(taskRepository),
            GetTasksUseCase(taskRepository),
            RemoveTask(taskRepository),
            RemoveAllTask(taskRepository),
            UpdateTaskUseCase(taskRepository),
            GetSelectedTask(taskRepository),
            SetSelectedTask(taskRepository),
            InitializeStartingTasks(taskRepository)
        ),
    )
*/
