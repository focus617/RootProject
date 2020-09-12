package com.example.backend.utils

import com.example.pomodoro2.domain.model.Task
import com.example.pomodoro2.platform.data.IDbLikeDataSource
import com.example.pomodoro2.platform.functional.Result
import com.example.pomodoro2.platform.functional.Result.Success
import com.example.pomodoro2.platform.functional.Result.Error

class FakeDataSource(
    var tasks: MutableList<Task>? = mutableListOf()
) : IDbLikeDataSource<Task> {

    override suspend fun createOrUpdateTask(t: Task) {
        tasks?.add(t)
    }

    override suspend fun retrieveTask(Id: String): Result<Task> {
        tasks?.firstOrNull { it.id == Id }?.let { return Success(it) }
        return Error(Exception("Task not found"))
    }

    override suspend fun retrieveTasks(): Result<List<Task>> {
        tasks?.let { return Success(it) }
        return Error(Exception("Tasks not found"))
    }

    override suspend fun deleteTask(t: Task) {
        tasks?.removeIf { it.id == t.id }
    }

    override suspend fun deleteAllTasks() {
        tasks?.clear()
    }

    override suspend fun completeTask(task: Task) {
        tasks?.firstOrNull { it.id == task.id }?.complete()
    }


    override suspend fun activateTask(task: Task) {
        tasks?.firstOrNull { it.id == task.id }?.activate()
    }


    override suspend fun clearCompletedTasks() {
        tasks?.removeIf { !it.isActive }
    }

    override suspend fun initializeTutorialTasks(): Result<List<Task>> {
        val task1 = Task(
            title = "title4",
            description = "Description1",
            isCompleted = false,
            imageId = 1,
            priority = 1
        )
        val task2 = Task(
            title = "title5",
            description = "Description2",
            isCompleted = true,
            imageId = 2,
            priority = 2
        )
        val task3 = Task(
            title = "title6",
            description = "Description3",
            isCompleted = true,
            imageId = 3,
            priority = 3
        )
        val taskList = listOf(task1, task2, task3).sortedBy { it.priority }
        return Success(taskList)
    }
}
