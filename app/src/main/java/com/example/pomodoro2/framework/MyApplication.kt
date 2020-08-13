package com.example.pomodoro2.framework

import android.app.Application
import com.example.pomodoro2.data.TaskRepository
import com.example.pomodoro2.features.infra.memory.InMemoryTaskDataSource
import com.example.pomodoro2.features.infra.database.RoomTaskDataSource
import com.example.pomodoro2.features.tasks.domain.Interactors
import com.example.pomodoro2.framework.platform.MyViewModelFactory
import com.example.pomodoro2.interactors.*
import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        val taskRepository = TaskRepository(
            RoomTaskDataSource(this),
            InMemoryTaskDataSource()
        )

        MyViewModelFactory.inject(
            this,
            Interactors(
                CreateNewTaskUseCase(taskRepository),
                GetTasksUseCase(taskRepository),
                RemoveTask(taskRepository),
                RemoveAllTask(taskRepository),
                UpdateTaskUseCase(taskRepository),
                GetSelectedTask(taskRepository),
                SetSelectedTask(taskRepository),
                InitializeStartingTasks(taskRepository)
            )
        )
    }
}