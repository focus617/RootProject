package com.example.pomodoro2.interactors

import com.example.pomodoro2.domain.model.Task
import com.example.pomodoro2.domain.model.ITaskRepository

class RemoveAllTaskUseCase(private val taskRepository: ITaskRepository<Task>) {
    suspend operator fun invoke() = taskRepository.removeAll()
}