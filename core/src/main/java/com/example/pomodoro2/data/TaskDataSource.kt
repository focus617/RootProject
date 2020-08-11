package com.example.pomodoro2.data

import com.example.pomodoro2.domain.Task

interface TaskDataSource {

    suspend fun add(task: Task)

    suspend fun readAll(): List<Task>

    suspend fun remove(task: Task)
}