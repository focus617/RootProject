package com.example.pomodoro2.interactors

import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.platform.data.IRepository

class RemoveAllTaskUseCase(private val taskRepository: IRepository<Task>) {
    suspend operator fun invoke() = taskRepository.removeAll()
}