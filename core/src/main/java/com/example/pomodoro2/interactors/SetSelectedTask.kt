package com.example.pomodoro2.interactors

import com.example.pomodoro2.data.implementation.DefaultTaskRepository
import com.example.pomodoro2.domain.Task

class SetSelectedTask(private val taskRepository: DefaultTaskRepository) {
    operator fun invoke(task: Task) = taskRepository.setSelectedTask(task)
}