package com.example.pomodoro2.interactors

import com.example.pomodoro2.BaseUnitTest
import com.example.pomodoro2.data.FakeRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi

import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.platform.functional.Result
import com.example.pomodoro2.platform.functional.Result.Success
import com.example.pomodoro2.platform.functional.Result.Error
import com.example.pomodoro2.interactors.TasksFilterType.ALL_TASKS
import com.example.pomodoro2.interactors.TasksFilterType.ACTIVE_TASKS
import com.example.pomodoro2.interactors.TasksFilterType.COMPLETED_TASKS
import kotlinx.coroutines.test.runBlockingTest
import org.junit.*


/**
 * Unit tests for [GetTasksUseCase].
 */
@ExperimentalCoroutinesApi
class GetTasksUseCaseTest : BaseUnitTest() {

    private val tasksRepository = FakeRepository()
    private val useCase = GetTasksUseCase(tasksRepository)


    @Test
    fun loadTasks_noFilter_empty() = runBlockingTest {
        // Given an empty repository

        // When calling the use case
        val result = useCase()

        // Verify the result is a success and empty
        Assert.assertTrue(result is Success)
        Assert.assertTrue((result as Success).data.isEmpty())
    }

    @Test
    fun loadTasks_error() = runBlockingTest {
        // Make the repository return errors
        tasksRepository.setReturnError(true)

        // Load tasks
        val result = useCase()

        // Verify the result is an error
        Assert.assertTrue(result is Error)
    }

    @Test
    fun loadTasks_noFilter() = runBlockingTest {
        // Given a repository with 1 active and 2 completed tasks:
        tasksRepository.addTasks(
            Task( title = "title1", description = "desc1", isCompleted = false, imageId = 1, priority = 1),
            Task( title = "title2", description = "desc2", isCompleted = true, imageId = 2, priority = 2),
            Task( title = "title3", description = "desc3", isCompleted = true, imageId = 3, priority = 3)
        )

        // Load tasks
        val result = useCase()

        // Verify the result is filtered correctly
        Assert.assertTrue(result is Success)
        Assert.assertEquals((result as Success).data.size, 3)
    }

    @Test
    fun loadTasks_completedFilter() = runBlockingTest{
        // Given a repository with 1 active and 2 completed tasks:
        tasksRepository.addTasks(
            Task( title = "title1", description = "desc1", isCompleted = false, imageId = 1, priority = 1),
            Task( title = "title2", description = "desc2", isCompleted = true, imageId = 2, priority = 2),
            Task( title = "title3", description = "desc3", isCompleted = true, imageId = 3, priority = 3)
        )

        // Load tasks
        val result = useCase(currentFiltering = COMPLETED_TASKS)

        // Verify the result is filtered correctly
        Assert.assertTrue(result is Success)
        Assert.assertEquals((result as Success).data.size, 2)
    }

    @Test
    fun loadTasks_activeFilter() = runBlockingTest{
        // Given a repository with 1 active and 2 completed tasks:
        tasksRepository.addTasks(
            Task( title = "title1", description = "desc1", isCompleted = false, imageId = 1, priority = 1),
            Task( title = "title2", description = "desc2", isCompleted = true, imageId = 2, priority = 2),
            Task( title = "title3", description = "desc3", isCompleted = true, imageId = 3, priority = 3)
        )

        // Load tasks
        val result = useCase(currentFiltering = ACTIVE_TASKS)

        // Verify the result is filtered correctly
        Assert.assertTrue(result is Success)
        Assert.assertEquals((result as Success).data.size, 1)
    }
}