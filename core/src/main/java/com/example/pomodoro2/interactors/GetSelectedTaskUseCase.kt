package com.example.pomodoro2.interactors

import com.example.pomodoro2.domain.model.Task
import com.example.pomodoro2.domain.model.ITaskRepository

class GetSelectedTaskUseCase(private val taskRepository: ITaskRepository<Task>) {
    operator fun invoke() = taskRepository.getSelectedTask()
}
