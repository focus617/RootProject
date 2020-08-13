package com.example.pomodoro2.data

import com.example.pomodoro2.domain.Task

class TaskRepository(
    private val taskDataSource: TaskDataSource,
    private val inMemoryTaskDataSource: InMemoryTaskDataSource) {

    suspend fun createTask(task: Task) = taskDataSource.createTask(task)

    suspend fun getTasks() = taskDataSource.getTasks()

    suspend fun updateTask(task: Task) = taskDataSource.updateTask(task)

    suspend fun removeTask(task: Task) = taskDataSource.deleteTask(task)

    suspend fun removeAllTask() = taskDataSource.deleteAllTasks()

    fun setSelectedTask(task: Task) = inMemoryTaskDataSource.setSelectedTask(task)

    fun getSelectedTask() = inMemoryTaskDataSource.getSelectedTask()

    suspend fun initializeStartingTasks() = taskDataSource.initializeTutorialTasks()

}