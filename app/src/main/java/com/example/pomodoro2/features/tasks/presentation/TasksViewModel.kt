package com.example.pomodoro2.features.tasks.presentation

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pomodoro2.R
import com.example.pomodoro2.framework.platform.SingleLiveEvent
import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.features.tasks.domain.Interactors
import com.example.pomodoro2.framework.platform.BaseViewModel
import kotlinx.coroutines.*

/**
 * ViewModel for TaskFragment.
 */
class TasksViewModel(application: Application, interactors: Interactors) :
    BaseViewModel(application, interactors) {

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
        _showSnackBarEvent.value = SingleLiveEvent(str)
    }

    /**
     * ClickHandler for recyclerview item click
     */
    fun onTaskClicked(task: Task){
        showInSnackBar("Start Task(${task.title})")
        doNavigating(task)
    }

    /**
     * LiveData for this viewModel
     */
    // Task list displayed on the screen
    private val _tasks: MutableLiveData<List<Task>> = MutableLiveData()
    val tasks : LiveData<List<Task>> = _tasks

    fun loadTasks() {
        GlobalScope.launch {
            _tasks.postValue(interactors.getTasks())
        }
    }

    fun addTask(task: Task) {
        GlobalScope.launch {
            withContext(Dispatchers.IO) {
                interactors.addTask(task)
            }

            loadTasks()
        }
    }

    fun setSelectedTask(task: Task) {
        interactors.setSelectedTask(task)
    }


    /**
     * To initialize the tasks variable as soon as possible
     */
    init {
        refreshCacheFromRemote()
        loadTasks()
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
     * Event triggered for network error. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _eventNetworkError :Boolean = false
    /**
     * Event triggered for network error. Views should use this to get access
     * to the data.
     */
    private val _isNetworkErrorShown = MutableLiveData<SingleLiveEvent<Boolean>>()
    val isNetworkErrorShown: LiveData<SingleLiveEvent<Boolean>> = _isNetworkErrorShown

    private fun networkErrorStateChange(error: Boolean) {
        _eventNetworkError = error
        _isNetworkErrorShown.value = SingleLiveEvent(error)
    }


    /**
     * Blow functions are used for testing purpose.
     * */

    fun createTestData() {
        createDummyTasksForTesting()
        // Maybe add more dummy data builder here.
    }

    fun clearTestData() {
        clearDummyTasksForTesting()
    }


    /**
     * UseCase: Create dummy task list for testing purpose
     */
    private fun createDummyTasksForTesting() {
        val titles = arrayOf(
            "读书",
            "锻炼身体",
            "学习Android开发",
            "冥想",
            "工作",
            "读书",
            "锻炼身体",
            "学习Android开发",
            "冥想",
            "工作"
        )

        val images = intArrayOf(
            R.drawable.read_book,
            R.drawable.exercise,
            R.drawable.study,
            R.drawable.thinking,
            R.drawable.work,
            R.drawable.read_book,
            R.drawable.exercise,
            R.drawable.study,
            R.drawable.thinking,
            R.drawable.work
        )

        // Create some sample Tasks and insert them into database.
        for ((index, element) in images.withIndex()) {
            // Create a new Task , which captures the current time,
            // then insert it into the database.
            val task = Task(
                title = titles[index],
                imageId = element,
                priority = index + 1
            )
            addTask(task)
        }
    }

    /**
     * UseCase: Clear the dummy Task list created for testing purpose
     */
    private fun clearDummyTasksForTesting() {
        GlobalScope.launch {
            // Clear the database table.
            interactors.removeAllTask()
            loadTasks()
        }
    }

    /**
     * UseCase: test the snackBar
     */
    fun testSnackBar() {
        // Show a snackbar message, because it's friendly.
        showInSnackBar(_application.getString(R.string.goodbye_message))
    }

}
