package com.example.pomodoro2.interactors

import com.example.pomodoro2.domain.model.Task
import com.example.pomodoro2.platform.data.IRepository

class GetSelectedTaskUseCase(private val taskRepository: IRepository<Task>) {
    operator fun invoke() = taskRepository.getSelectedTask()
}
