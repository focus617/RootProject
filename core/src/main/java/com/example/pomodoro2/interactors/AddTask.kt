package com.example.pomodoro2.interactors

import com.example.pomodoro2.data.TaskRepository
import com.example.pomodoro2.domain.Task

class AddTask(private val taskRepository: TaskRepository) {
    suspend operator fun invoke(task: Task) = taskRepository.addTask(task)
}