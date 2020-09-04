package com.example.pomodoro2.interactors

import com.example.pomodoro2.domain.model.ITaskRepository
import com.example.pomodoro2.domain.model.Task

class SetSelectedTaskUseCase(private val taskRepository: ITaskRepository<Task>) {
    operator fun invoke(task: Task) = taskRepository.setSelectedTask(task)
}