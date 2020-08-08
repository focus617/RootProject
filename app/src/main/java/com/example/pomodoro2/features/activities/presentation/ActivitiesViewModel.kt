package com.example.pomodoro2.features.activities.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pomodoro2.core.platform.BaseViewModel
import com.example.pomodoro2.core.platform.SingleLiveEvent
import com.example.pomodoro2.features.infra.database.ProjectDAO
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

/**
* ViewModel for the activitiesFragment.
*/
// TODO:change ProjectDAO to ActivityDAO later
class ActivitiesViewModel(
    private val projectKey: Long = 0L,
    private val dataSource: ProjectDAO
    //private val getTasksUseCase: GetTasksUseCase
) : BaseViewModel() {

    /** Coroutine variables */

    /**
     * viewModelJob allows us to cancel all coroutines started by this ViewModel.
     */
    private var viewModelJob = Job()

    /**
     * A [CoroutineScope] keeps track of all coroutines started by this ViewModel.
     *
     * Because we pass it [viewModelJob], any coroutine started in this uiScope can be cancelled
     * by calling `viewModelJob.cancel()`
     *
     * By default, all coroutines started in uiScope will launch in [Dispatchers.Main] which is
     * the main thread on Android. This is a sensible default because most coroutines started by
     * a [ViewModel] update the UI after performing some processing.
     */
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    /**
     * Called when the ViewModel is dismantled.
     * At this point, we want to cancel all coroutines;
     * otherwise we end up with processes that have nowhere to return to
     * using memory and resources.
     */
    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    /**
    * Event for navigation to CountDownTimer fragment.
    */
    private val _launchTimerEvent = MutableLiveData<SingleLiveEvent<Unit>>()
    val launchTimerEvent: LiveData<SingleLiveEvent<Unit>> = _launchTimerEvent

    /**
     * Called by the Data Binding library and the Start Button's click listener.
     */
    fun doNavigating() {
        _launchTimerEvent.value = SingleLiveEvent(Unit)
    }

    // TODO: remove in future
    private val _text = MutableLiveData<String>().apply {
        value = "This is activity Fragment"
    }
    val text: LiveData<String> = _text
}