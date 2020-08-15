package com.example.pomodoro2.interactors

import com.example.pomodoro2.data.interface_def.TaskRepository

class GetSelectedTaskUseCase(private val taskRepository: TaskRepository) {
    operator fun invoke() = taskRepository.getSelectedTask()
}
