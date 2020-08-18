package com.example.pomodoro2.features.tasks.presentation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.pomodoro2.R
import com.example.pomodoro2.framework.platform.SingleLiveEvent
import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.features.tasks.domain.TaskInteractors
import com.example.pomodoro2.framework.base.BaseViewModel
import com.example.pomodoro2.platform.functional.Result
import kotlinx.coroutines.*

/**
 * ViewModel for TaskFragment.
 */
class TasksViewModel(application: Application, val taskInteractors: TaskInteractors) :
    BaseViewModel(application) {

    private var _application: Application = application


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
     * To initialize the tasks variable as soon as possible
     */
    init {
        refreshCacheFromRemote()
        loadTasks()
    }

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
     * Event for navigation to activity fragment.
     *
     * Trigger this singleLiveEvent by setting a new Event as a new value
     *
     * It will be cleared automatically after the toast request,
     * so if the user rotates their phone it won't show a duplicate toast.
     *
     */
    private val _navigateToActivityFragment = MutableLiveData<SingleLiveEvent<Task>>()
    val navigateToActivityFragment: LiveData<SingleLiveEvent<Task>> = _navigateToActivityFragment

    fun doNavigating(task: Task) {
        _navigateToActivityFragment.value = SingleLiveEvent(task)
    }

    /**
     * Event for SnackBar displaying.
     *
     * Trigger this singleLiveEvent by setting a new Event as a new value
     * for example:  _showSnackbarEvent.value = SingleLiveEvent(Unit)
     *
     * It will be cleared automatically after the toast request,
     * so if the user rotates their phone it won't show a duplicate toast.
     *
     */
    private val _showSnackBarEvent = MutableLiveData<SingleLiveEvent<String>>()
    val showSnackBarEvent: LiveData<SingleLiveEvent<String>> = _showSnackBarEvent

    private fun showInSnackBar(str: String) {
        // TODO：研究如何显示 R.String 资源
        _showSnackBarEvent.value = SingleLiveEvent(str)
    }

    /**
     * ClickHandler for recyclerview item click
     */
    fun onTaskSelected(task: Task){
        showInSnackBar("Start Task(${task.title})")
        setSelectedTask(task)
        doNavigating(task)
    }

    /**
     * LiveData for this viewModel
     */
    // Task list displayed on the screen
    private val _tasks: MutableLiveData<List<Task>> = MutableLiveData()
    val tasks : LiveData<List<Task>> = _tasks

    /**
     * @param forceUpdate   Pass in true to refresh the data in the [TasksDataSource]
     */
    fun loadTasks() {
        viewModelScope.launch {
            val tasksResult = taskInteractors.getTasksUseCase()
            if (tasksResult is Result.Success) {
                _tasks.value = tasksResult.data
            } else {
                _tasks.value = emptyList()
                showInSnackBar("Error while loading tasks")
            }
        }
    }
    fun setSelectedTask(task: Task) {
        taskInteractors.setSelectedTaskUseCase(task)
    }

    /**
     * Below functions are used by Create or Edit Task Fragment
     */
    private var isNewTask: Boolean = true

    fun createNewTask(task: Task) {
        task.imageId = R.drawable.read_book
        task.priority = _tasks.value?.size ?: 1
        viewModelScope.launch {
                taskInteractors.createNewTaskUseCase(task)

            // Refresh view model
            loadTasks()
        }
    }

    fun updateTask(task: Task) {
        if (isNewTask) {
            throw RuntimeException("updateTask() was called but task is new.")
        }
        viewModelScope.launch {
            taskInteractors.updateTaskUseCase(task)
        }
    }

    fun completeTask(task: Task, completed: Boolean) = viewModelScope.launch {
        if (completed) {
            taskInteractors.completeTaskUseCase(task)
        } else {
            taskInteractors.activateTaskUseCase(task)
        }
        // Refresh list to show the new state
        loadTasks()
    }

    /**
     * UseCase: Clear the Task Table, used for testing purpose
     */
    fun clearTaskTable() {
        GlobalScope.launch {
            // Clear the database table.
            taskInteractors.removeAllTaskUseCase()

            // Refresh view model
            loadTasks()
        }
    }

    /**
     * UseCase: Create tutorial tasks
     */
    private fun initializeTutorialTasks() {
        viewModelScope.launch {
            taskInteractors.initStartingTasksUseCase()

            // Refresh view model
            loadTasks()
        }
    }



    /**
     * Refresh data from the repository. Use a coroutine launch to run in a
     * background thread.
     */
    private fun refreshCacheFromRemote() {}
/*
    private fun refreshCacheFromRemote() {
        uiScope.launch {
            try {
                repository.refreshTasks()
                networkErrorStateChange(error = false)
            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
                if(tasks.value.isNullOrEmpty()) {
                    networkErrorStateChange(error = true)
                }
            }
        }
    }
 */

    /**
     * Event triggered for network error.
     */
    private val _eventNetworkError = MutableLiveData<SingleLiveEvent<Boolean>>()
    val eventNetworkError: LiveData<SingleLiveEvent<Boolean>> = _eventNetworkError

    private fun networkErrorStateChange(error: Boolean) {
        _eventNetworkError.value = SingleLiveEvent(error)
    }


    /**
     * Blow functions are used for testing purpose.
     */

    fun createTestData() {
        initializeTutorialTasks()
        // Maybe add more dummy data builder here.
    }

    fun clearTestData() {
        clearTaskTable()
        // Maybe add more dummy data cleaner here.
    }

    /**
     * UseCase: test the snackBar
     */
    fun testSnackBar() {
        // Show a snackbar message, because it's friendly.
        showInSnackBar(_application.getString(R.string.goodbye_message))
    }

}
