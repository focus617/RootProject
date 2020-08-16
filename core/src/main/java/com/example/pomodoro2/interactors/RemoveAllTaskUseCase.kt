package com.example.pomodoro2.interactors

import com.example.pomodoro2.platform.interface_def.TaskRepository

class RemoveAllTaskUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke() = taskRepository.removeAllTask()
}