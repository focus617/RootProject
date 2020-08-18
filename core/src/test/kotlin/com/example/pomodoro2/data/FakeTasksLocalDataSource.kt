package com.example.pomodoro2.data

import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.platform.data.IDbLikeDataSource
import com.example.pomodoro2.platform.functional.Result

/**
 * Implementation of a local data source with static access to the data for easy testing on Repository.
 */
class FakeTasksLocalDataSource(
    var tasks: MutableList<Task>? = mutableListOf()
) : IDbLikeDataSource<Task> {

    override suspend fun createTask(t: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun retrieveTask(Id: Long): Result<Task> {
        TODO("Not yet implemented")
    }

    override suspend fun retrieveTasks(): Result<List<Task>> {
        TODO("Not yet implemented")
    }

    override suspend fun updateTask(t: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteTask(t: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun deleteAllTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun completeTask(t: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun activateTask(t: Task) {
        TODO("Not yet implemented")
    }

    override suspend fun clearCompletedTasks() {
        TODO("Not yet implemented")
    }

    override suspend fun initializeTutorialTasks() {
        TODO("Not yet implemented")
    }

}