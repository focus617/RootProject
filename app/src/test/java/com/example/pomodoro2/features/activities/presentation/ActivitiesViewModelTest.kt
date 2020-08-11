package com.example.pomodoro2.features.activities.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pomodoro2.R
import com.example.pomodoro2.core.platform.SingleLiveEvent
import com.example.pomodoro2.features.infra.database.AppDatabase
import com.example.pomodoro2.domain.Project
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.Before
import org.junit.Test
import org.junit.Rule
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

        // Create an instance of Database.
        // TODO:change ProjectDAO to ActivityDAO later
        val dataSource = AppDatabase.getInstance(application).projectDao
        val project = Project(
            1L,
            "番茄工作",
            R.drawable.read_book,
            1
        )

        // Given a fresh ViewModel
        val activitiesViewModel = ActivitiesViewModel(project, dataSource)

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