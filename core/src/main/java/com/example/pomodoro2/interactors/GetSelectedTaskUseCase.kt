package com.example.pomodoro2.interactors

import com.example.pomodoro2.platform.data.TaskRepository

class GetSelectedTaskUseCase(private val taskRepository: TaskRepository) {
    operator fun invoke() = taskRepository.getSelectedTask()
}
