package com.example.pomodoro2.features.tasks.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pomodoro2.LiveDataTestUtil
import com.example.pomodoro2.MainCoroutineRule
import com.example.pomodoro2.domain.model.Task
import com.example.pomodoro2.features.tasks.domain.TaskInteractors
import com.example.pomodoro2.interactors.*
import com.example.pomodoro2.mock.FakeRepository
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith


/**
 * Unit tests for the implementation of [TasksViewModel]
 */
@ExperimentalCoroutinesApi
@RunWith(AndroidJUnit4::class)
class TasksViewModelTest {


    // Subject under test
    private lateinit var tasksViewModel: TasksViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var tasksRepository: FakeRepository

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        // We initialise the tasks to 3, with one active and two completed
        tasksRepository = FakeRepository()
        val task1 = Task(
            title = "title1",
            description = "Description1",
            isCompleted = false,
            imageId = 1,
            priority = 1
        )
        val task2 = Task(
            title = "title2",
            description = "Description2",
            isCompleted = true,
            imageId = 2,
            priority = 2
        )
        val task3 = Task(
            title = "title3",
            description = "Description3",
            isCompleted = true,
            imageId = 3,
            priority = 3
        )
        tasksRepository.addTasks(task1, task2, task3)

        val taskInteractors = TaskInteractors(
        CreateNewTaskUseCase(tasksRepository),
        GetTasksUseCase(tasksRepository),
        RemoveTaskUseCase(tasksRepository),
        RemoveAllTaskUseCase(tasksRepository),
        UpdateTaskUseCase(tasksRepository),
        CompleteTaskUseCase(tasksRepository),
        ActivateTaskUseCase(tasksRepository),
        GetSelectedTaskUseCase(tasksRepository),
        SetSelectedTaskUseCase(tasksRepository),
        InitializeStartingTasksUseCase(tasksRepository)
        )

        tasksViewModel = TasksViewModel(ApplicationProvider.getApplicationContext(), taskInteractors)
    }


    @Test
    fun loadAllTasksFromRepository_loadingTogglesAndDataLoaded() {
        // Pause dispatcher so we can verify initial values
        mainCoroutineRule.pauseDispatcher()

        // Given an initialized TasksViewModel with initialized tasks
        // When loading of Tasks is requested
        tasksViewModel.setFiltering(TasksFilterType.ALL_TASKS)

        // Trigger loading of tasks
        tasksViewModel.loadTasks(true)

        // Then progress indicator is shown
        //assertThat(LiveDataTestUtil.getValue(tasksViewModel.dataLoading)).isTrue()

        // Execute pending coroutines actions
        mainCoroutineRule.resumeDispatcher()

        // Then progress indicator is hidden
        //assertThat(LiveDataTestUtil.getValue(tasksViewModel.dataLoading)).isFalse()

        // And data correctly loaded
        assertThat(LiveDataTestUtil.getValue(tasksViewModel.tasks)).hasSize(3)
    }
}