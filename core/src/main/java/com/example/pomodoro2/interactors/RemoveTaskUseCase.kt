package com.example.pomodoro2.interactors

import com.example.pomodoro2.platform.data.IRepository
import com.example.pomodoro2.domain.model.Task

class RemoveTaskUseCase(private val taskRepository: IRepository<Task>) {
    suspend operator fun invoke(task: Task) = taskRepository.remove(task)
}