package com.example.pomodoro2.interactors

import com.example.pomodoro2.platform.interface_def.TaskRepository
import com.example.pomodoro2.domain.Task

class CreateNewTaskUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(task: Task) = taskRepository.createTask(task)
}