package com.example.pomodoro2.interactors

import com.example.pomodoro2.data.implementation.DefaultTaskRepository

class GetSelectedTask(private val taskRepository: DefaultTaskRepository) {
    operator fun invoke() = taskRepository.getSelectedTask()
}