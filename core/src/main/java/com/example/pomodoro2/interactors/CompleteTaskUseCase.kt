package com.example.pomodoro2.interactors

import com.example.pomodoro2.data.implementation.DefaultTaskRepository
import com.example.pomodoro2.domain.Task

class CompleteTaskUseCase(private val taskRepository: DefaultTaskRepository) {
    suspend operator fun invoke(task: Task) = taskRepository.completeTask(task)
}