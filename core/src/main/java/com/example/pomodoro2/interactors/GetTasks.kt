package com.example.pomodoro2.interactors

import com.example.pomodoro2.data.TaskRepository
import com.example.pomodoro2.domain.Task

class GetTasks(private val taskRepository: TaskRepository) {
    suspend operator fun invoke() = taskRepository.getTasks()
}