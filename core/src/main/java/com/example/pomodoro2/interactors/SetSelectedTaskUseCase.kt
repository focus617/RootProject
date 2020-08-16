package com.example.pomodoro2.interactors

import com.example.pomodoro2.platform.interface_def.TaskRepository
import com.example.pomodoro2.domain.Task

class SetSelectedTaskUseCase(private val taskRepository: TaskRepository) {
    operator fun invoke(task: Task) = taskRepository.setSelectedTask(task)
}