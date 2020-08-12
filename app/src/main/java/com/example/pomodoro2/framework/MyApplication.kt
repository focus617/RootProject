package com.example.pomodoro2.framework

import android.app.Application
import com.example.pomodoro2.data.TaskRepository
import com.example.pomodoro2.features.infra.database.InMemorySelectedTaskDataSource
import com.example.pomodoro2.features.infra.database.RoomTaskDataSource
import com.example.pomodoro2.features.tasks.domain.Interactors
import com.example.pomodoro2.framework.platform.MyViewModelFactory
import com.example.pomodoro2.interactors.AddTask
import com.example.pomodoro2.interactors.GetSelectedTask
import com.example.pomodoro2.interactors.GetTasks
import com.example.pomodoro2.interactors.RemoveTask
import timber.log.Timber

class MyApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        Timber.plant(Timber.DebugTree())

        val taskRepository = TaskRepository(
            RoomTaskDataSource(this),
            InMemorySelectedTaskDataSource()
        )

        MyViewModelFactory.inject(
            this,
            Interactors(
                AddTask(taskRepository),
                RemoveTask(taskRepository),
                GetTasks(taskRepository),
                GetSelectedTask(taskRepository),
                GetSelectedTask(taskRepository)
            )
        )
    }
}