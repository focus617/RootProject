package com.example.pomodoro2.interactors

import com.example.pomodoro2.data.implementation.DefaultTaskRepository

class RemoveAllTask(private val taskRepository: DefaultTaskRepository) {
    suspend operator fun invoke() = taskRepository.removeAllTask()
}