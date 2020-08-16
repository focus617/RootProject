package com.example.pomodoro2.interactors

import com.example.pomodoro2.platform.data.TaskRepository
import com.example.pomodoro2.domain.Task

class CompleteTaskUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(task: Task) = taskRepository.completeTask(task)
}