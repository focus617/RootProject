package com.example.pomodoro2.interactors

import com.example.pomodoro2.platform.data.IRepository
import com.example.pomodoro2.domain.Task

class SetSelectedTaskUseCase(private val taskRepository: IRepository<Task>) {
    operator fun invoke(task: Task) = taskRepository.setSelectedTask(task)
}