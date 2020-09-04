package com.example.pomodoro2.features.tasks.presentation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.pomodoro2.R
import com.example.pomodoro2.domain.model.Task
import com.example.pomodoro2.features.tasks.domain.TaskInteractors
import com.example.pomodoro2.framework.base.BaseViewModel
import com.example.pomodoro2.framework.platform.SingleLiveEvent
import com.example.pomodoro2.interactors.TasksFilterType
import com.example.pomodoro2.platform.functional.Result
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch

/**
 * ViewModel for TaskFragment.
 */
class TasksViewModel(application: Application, val taskInteractors: TaskInteractors) :
    BaseViewModel(application) {

    private var _application: Application = application


    /**
     * To initialize the tasks variable as soon as possible
     */
    init {
        //refreshCacheFromRemote()
        setFiltering(TasksFilterType.ALL_TASKS)
        loadTasks(true)
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
    fun onTaskSelected(task: Task) {
        showInSnackBar("Start Task(${task.title})")
        setSelectedTask(task)
        doNavigating(task)
    }

    /**
     * LiveData for this viewModel
     */
    // Task list displayed on the screen
    private val _tasks: MutableLiveData<List<Task>> = MutableLiveData()
    val tasks: LiveData<List<Task>> = _tasks


    private var _currentFiltering = TasksFilterType.ALL_TASKS

    /**
     * Sets the current task filtering type.
     *
     * @param requestType Can be [TasksFilterType.ALL_TASKS],
     * [TasksFilterType.COMPLETED_TASKS], or
     * [TasksFilterType.ACTIVE_TASKS]
     */
    fun setFiltering(requestType: TasksFilterType) {
        _currentFiltering = requestType
        /*
            // Depending on the filter type, set the filtering label, icon drawables, etc.
            when (requestType) {
                TasksFilterType.ALL_TASKS -> {
                    setFilter(
                        R.string.label_all, R.string.no_tasks_all,
                        R.drawable.logo_no_fill, true
                    )
                }
                TasksFilterType.ACTIVE_TASKS -> {
                    setFilter(
                        R.string.label_active, R.string.no_tasks_active,
                        R.drawable.ic_check_circle_96dp, false
                    )
                }
                TasksFilterType.COMPLETED_TASKS -> {
                    setFilter(
                        R.string.label_completed, R.string.no_tasks_completed,
                        R.drawable.ic_verified_user_96dp, false
                    )
                }
            }
        */
    }

    /*
        private fun setFilter(
            @StringRes filteringLabelString: Int, @StringRes noTasksLabelString: Int,
            @DrawableRes noTaskIconDrawable: Int, tasksAddVisible: Boolean
        ) {
            _currentFilteringLabel.value = filteringLabelString
            _noTasksLabel.value = noTasksLabelString
            _noTaskIconRes.value = noTaskIconDrawable
            _tasksAddViewVisible.value = tasksAddVisible
        }
    */
    private val _dataLoading = MutableLiveData<Boolean>()
    val dataLoading: LiveData<Boolean> = _dataLoading

    /**
     * @param forceUpdate   Pass in true to refresh the data in the TasksDataSource
     */
    fun loadTasks(forceUpdate: Boolean) {
        //_dataLoading.value = true
        viewModelScope.launch {
            val tasksResult =
                taskInteractors.getTasksUseCase(forceUpdate, _currentFiltering)
            if (tasksResult is Result.Success) {
                _tasks.value = tasksResult.data
            } else {
                _tasks.value = emptyList()
                showInSnackBar("Error while loading tasks")
            }
            //_dataLoading.value = false
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
        val newTask = Task(
            task.id, task.title, task.description, !task.isActive,
            task.imageId, task.priority, task.createTime
        )
        newTask.imageId = R.drawable.read_book
        newTask.priority = (_tasks.value?.size ?: 0) + 1
        viewModelScope.launch {
            taskInteractors.createNewTaskUseCase(newTask)

            // Refresh view model
            loadTasks(false)
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
        loadTasks(false)
    }

    /**
     * UseCase: Clear the Task Table, used for testing purpose
     */
    fun clearTaskTable() {
        GlobalScope.launch {
            // Clear the database table.
            taskInteractors.removeAllTaskUseCase()

            // Refresh view model
            loadTasks(true)
        }
    }

    /**
     * UseCase: Create tutorial tasks
     */
    private fun initializeTutorialTasks() {
        viewModelScope.launch {
            taskInteractors.initStartingTasksUseCase()

            // Refresh view model
            loadTasks(true)
        }
    }


    /**
     * Refresh data from the repository. Use a coroutine launch to run in a
     * background thread.
     */
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
