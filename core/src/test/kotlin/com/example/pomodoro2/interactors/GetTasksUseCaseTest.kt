package com.example.pomodoro2.interactors

import com.example.pomodoro2.data.FakeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.platform.functional.Result
import com.example.pomodoro2.platform.functional.Result.Success
import com.example.pomodoro2.platform.functional.Result.Error
import com.example.pomodoro2.interactors.TasksFilterType.ALL_TASKS
import com.example.pomodoro2.interactors.TasksFilterType.ACTIVE_TASKS
import com.example.pomodoro2.interactors.TasksFilterType.COMPLETED_TASKS
import org.junit.*


/**
 * Unit tests for [GetTasksUseCase].
 */
@ExperimentalCoroutinesApi
class GetTasksUseCaseTest {

    private val tasksRepository = FakeRepository()
    private val useCase = GetTasksUseCase(tasksRepository)


    @Test
    operator fun invoke() {
    }
}