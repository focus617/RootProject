package com.example.pomodoro2.features.activities.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pomodoro2.data.TaskRepository
import com.example.pomodoro2.features.infra.memory.AppInMemoryDataSource
import com.example.pomodoro2.features.infra.database.RoomTaskDataSource
import com.example.pomodoro2.features.tasks.domain.TaskInteractors
import com.example.pomodoro2.framework.platform.SingleLiveEvent
import com.example.pomodoro2.interactors.CreateNewTaskUseCase
import com.example.pomodoro2.interactors.GetSelectedTask
import com.example.pomodoro2.interactors.GetTasksUseCase
import com.example.pomodoro2.interactors.RemoveTask
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RuntimeEnvironment.application

@RunWith(AndroidJUnit4::class)
class ActivitiesViewModelTest {

    // Subject under test
    private lateinit var activitiesViewModel: ActivitiesViewModel

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {

    }

    @Test
    fun startTimer_setLaunchTimerEvent() {

        // Create an instance of Repository.
        val taskRepository = TaskRepository(
            RoomTaskDataSource(application),
            AppInMemoryDataSource()
        )

        // Given a fresh ViewModel
        val activitiesViewModel =
            ActivitiesViewModel(application, TaskInteractors(
                CreateNewTaskUseCase(taskRepository),
                RemoveTask(taskRepository),
                GetTasksUseCase(taskRepository),
                GetSelectedTask(taskRepository),
                GetSelectedTask(taskRepository)
            ))

        // Create observer - no need for it to do anything!
        val observer = Observer<SingleLiveEvent<Unit>> {}
        try {

            // Observe the LiveData forever
            activitiesViewModel.launchTimerEvent.observeForever(observer)

            // When launch a new countdown timer
            activitiesViewModel.doNavigating()

            // Then the launch CountDownTimer fragment event is triggered
            // Test LiveData
            val value = activitiesViewModel.launchTimerEvent.value
            assertThat(value?.getContentIfNotHandled(), (not(nullValue())))

        } finally {
            // Whatever happens, don't forget to remove the observer!
            activitiesViewModel.launchTimerEvent.removeObserver(observer)
        }

    }
}