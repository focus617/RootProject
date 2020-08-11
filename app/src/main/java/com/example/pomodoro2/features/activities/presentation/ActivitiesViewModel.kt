package com.example.pomodoro2.features.activities.presentation

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pomodoro2.framework.platform.BaseViewModel
import com.example.pomodoro2.framework.platform.SingleLiveEvent
import com.example.pomodoro2.features.infra.database.ProjectDAO
import com.example.pomodoro2.domain.Task
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job

/**
 * ViewModel for the activitiesFragment.
 */
// TODO:change ProjectDAO to ActivityDAO later
class ActivitiesViewModel(
    task: Task,
    private val dataSource: ProjectDAO
    //private val getTasksUseCase: GetTasksUseCase
) : BaseViewModel() {

    /**
     * Hold a reference to AppDatabase via its ActivityDatabaseDao.
     */
    val database = dataSource

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

    fun doNavigating() {
        _launchTimerEvent.value = SingleLiveEvent(Unit)
    }

    /**
     * LiveData for this viewModel
     */
    // The internal MutableLiveData for the selected project
    private var _selectedProject = MutableLiveData<Task>()
    // The external LiveData for the SelectedProject
    val selectedTask: LiveData<Task>
        get() = _selectedProject



    // TODO: remove in future
    private val _text = MutableLiveData<String>().apply {
        value = "This is activity Fragment"
    }
    val text: LiveData<String> = _text


    // Initialize the _selectedProperty MutableLiveData
    init {
        _selectedProject.value = task
        _text.value = selectedTask.value?.title
    }
}