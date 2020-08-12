package com.example.pomodoro2.interactors

import com.example.pomodoro2.data.TaskRepository
import com.example.pomodoro2.domain.Task

class GetSelectedTask(private val taskRepository: TaskRepository) {
    operator fun invoke() = taskRepository.getSelectedTask()
}