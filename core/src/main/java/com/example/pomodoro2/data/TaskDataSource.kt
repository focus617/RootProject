package com.example.pomodoro2.data

import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.platform.functional.Result

interface TaskDataSource {

    suspend fun createTask(task: Task)

    suspend fun getTask(taskId: Long): Result<Task>

    suspend fun getTasks(): Result<List<Task>>

    suspend fun updateTask(task: Task)

    suspend fun deleteTask(task: Task)

    suspend fun deleteAllTasks()

    suspend fun completeTask(task: Task)

    suspend fun clearCompletedTasks()

    suspend fun initializeTutorialTasks()

}