package com.example.pomodoro2.data.interface_def

import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.platform.functional.Result

/**
 * Interface to the data layer.
 */
interface TaskRepository{

    suspend fun createTask(task: Task)

    suspend fun getTasks(): Result<List<Task>>

    suspend fun updateTask(task: Task)

    suspend fun removeTask(task: Task)

    suspend fun removeAllTask()

    suspend fun completeTask(task: Task)

    suspend fun activateTask(task: Task)

    fun setSelectedTask(task: Task)

    fun getSelectedTask(): Task

    suspend fun initializeStartingTasks()

}