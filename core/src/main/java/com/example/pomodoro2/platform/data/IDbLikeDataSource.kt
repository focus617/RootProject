package com.example.pomodoro2.platform.data

import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.platform.domain.BaseEntity
import com.example.pomodoro2.platform.functional.Result

interface IDbLikeDataSource<T: BaseEntity> {

    suspend fun createTask(t: T)

    suspend fun retrieveTask(Id: Long): Result<T>

    suspend fun retrieveTasks(): Result<List<T>>

    suspend fun updateTask(t: T)

    suspend fun deleteTask(t: T)

    suspend fun deleteAllTasks()

    suspend fun completeTask(t: T)

    suspend fun activateTask(t: T)

    suspend fun clearCompletedTasks()

    suspend fun initializeTutorialTasks()

}