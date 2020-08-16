package com.example.pomodoro2.features.tasks.domain

import com.example.pomodoro2.interactors.*
import com.example.pomodoro2.platform.interactor.Interactors

/**
 * Definition of UseCase Set for each feature
 */
data class TaskInteractors (
    val createNewTaskUseCase: CreateNewTaskUseCase,
    val getTasksUseCase: GetTasksUseCase,
    val removeTaskUseCase: RemoveTaskUseCase,
    val removeAllTaskUseCase: RemoveAllTaskUseCase,
    val updateTaskUseCase: UpdateTaskUseCase,
    val completeTaskUseCase: CompleteTaskUseCase,
    val activateTaskUseCase: ActivateTaskUseCase,
    val getSelectedTaskUseCase: GetSelectedTaskUseCase,
    val setSelectedTaskUseCase: SetSelectedTaskUseCase,
    val initStartingTasksUseCase: InitializeStartingTasksUseCase
) : Interactors