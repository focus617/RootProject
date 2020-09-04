package com.example.pomodoro2.interactors

import com.example.pomodoro2.domain.model.ITaskRepository
import com.example.pomodoro2.domain.model.Task

class ActivateTaskUseCase(private val taskRepository: ITaskRepository<Task>) {
    suspend operator fun invoke(task: Task) = taskRepository.activateTask(task)
}