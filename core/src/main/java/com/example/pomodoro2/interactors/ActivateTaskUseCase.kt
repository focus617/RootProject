package com.example.pomodoro2.interactors

import com.example.pomodoro2.data.interface_def.TaskRepository
import com.example.pomodoro2.domain.Task

class ActivateTaskUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(task: Task) = taskRepository.activateTask(task)
}