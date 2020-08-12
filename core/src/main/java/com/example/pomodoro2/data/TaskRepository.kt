package com.example.pomodoro2.data

import com.example.pomodoro2.domain.Task

class TaskRepository(
    private val taskDataSource: TaskDataSource,
    private val inMemoryTaskDataSource: InMemoryTaskDataSource) {

    suspend fun addTask(task: Task) = taskDataSource.add(task)

    suspend fun getTasks() = taskDataSource.getAll()

    suspend fun removeTask(task: Task) = taskDataSource.remove(task)

    suspend fun removeAllTask() = taskDataSource.removeAll()

    fun setSelectedTask(task: Task) = inMemoryTaskDataSource.setSelectedTask(task)

    fun getSelectedTask() = inMemoryTaskDataSource.getSelectedTask()

    suspend fun initializeStartingTasks() = taskDataSource.initializeTutorialTasks()

}