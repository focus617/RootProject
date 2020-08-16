package com.example.pomodoro2.interactors

import com.example.pomodoro2.platform.interface_def.TaskRepository
import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.platform.functional.Result

class GetTasksUseCase(private val taskRepository: TaskRepository) {
    suspend operator fun invoke():Result<List<Task>> = taskRepository.getTasks()
}