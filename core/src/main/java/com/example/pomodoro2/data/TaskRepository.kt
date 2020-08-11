package com.example.pomodoro2.data

import com.example.pomodoro2.domain.Task

class TaskRepository(
    private val taskDataSource: TaskDataSource,
    private val openTaskDataSource: OpenTaskDataSource) {

    suspend fun addTask(task: Task) = taskDataSource.add(task)

    suspend fun getTasks() = taskDataSource.readAll()

    suspend fun removeTask(task: Task) = taskDataSource.remove(task)

    fun setOpenTask(task: Task) = openTaskDataSource.setOpenTask(task)

    fun getOpenTask() = openTaskDataSource.getOpenTask()
}