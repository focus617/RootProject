package com.example.pomodoro2.framework

import android.app.Application
import com.example.pomodoro2.data.ActivityRepository
import com.example.pomodoro2.data.TaskRepository
import com.example.pomodoro2.features.infra.database.RoomActivityDataSource
import com.example.pomodoro2.features.infra.memory.InMemoryDataSource
import com.example.pomodoro2.features.infra.database.RoomTaskDataSource
import com.example.pomodoro2.features.tasks.domain.Interactors
import com.example.pomodoro2.framework.platform.MyViewModelFactory
import com.example.pomodoro2.interactors.*
import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        val inMemoryDataSource = InMemoryDataSource()

        val taskRepository = TaskRepository(
            RoomTaskDataSource(this),
            inMemoryDataSource
        )

        val activityRepository = ActivityRepository(
            RoomActivityDataSource(this),
            inMemoryDataSource
        )

        MyViewModelFactory.inject(
            this,
            Interactors(
                AddTask(taskRepository),
                RemoveTask(taskRepository),
                GetTasks(taskRepository),
                RemoveAllTask(taskRepository),
                GetSelectedTask(taskRepository),
                SetSelectedTask(taskRepository),
                InitializeStartingTasks(taskRepository)
            )
        )
    }
}