package com.example.pomodoro2.interactors

import com.example.pomodoro2.platform.data.IRepository
import com.example.pomodoro2.domain.Task

class CompleteTaskUseCase(private val taskRepository: IRepository<Task>) {
    suspend operator fun invoke(task: Task) = taskRepository.completeTask(task)
}