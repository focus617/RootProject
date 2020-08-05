package com.example.pomodoro2.features.activities.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.pomodoro2.core.platform.SingleLiveEvent
import org.hamcrest.MatcherAssert.assertThat
import org.hamcrest.Matchers.not
import org.hamcrest.Matchers.nullValue
import org.junit.Before
import org.junit.Test
import org.junit.Rule
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ActivitiesViewModelTest {

    // Subject under test
    private lateinit var activitiesViewModel: ActivitiesViewModel

    // Executes each task synchronously using Architecture Components.
    @get:Rule
    var instantExecutorRule = InstantTaskExecutorRule()

    @Before
    fun setupViewModel() {
        // Given a fresh ViewModel
        val activitiesViewModel = ActivitiesViewModel()
    }

    @Test
    fun startTimer_setLaunchTimerEvent() {

        // Create observer - no need for it to do anything!
        val observer = Observer<SingleLiveEvent<Unit>> {}
        try {

            // Observe the LiveData forever
            activitiesViewModel.launchTimerEvent.observeForever(observer)

            // When launch a new countdown timer
            activitiesViewModel.launchTimer()

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