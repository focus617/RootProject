package com.example.pomodoro2.features.projects.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pomodoro2.R
import com.example.pomodoro2.core.platform.SingleLiveEvent
import com.example.pomodoro2.features.infra.database.ProjectDAO
import com.example.pomodoro2.features.infra.repository.AppRepository
import com.example.pomodoro2.domain.Project
import com.example.pomodoro2.domain.asDatabaseEntity
import kotlinx.coroutines.*
import java.io.IOException

/**
 * ViewModel for ProjectFragment.
 */
class ProjectsViewModel(application: Application) :
    AndroidViewModel(application) {

    private var _application: Application = application

    /**
     * The data source this ViewModel will fetch results from.
     */
    private val repository = AppRepository(_application)

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
     * Event triggered for network error. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _eventNetworkError = MutableLiveData<Boolean>(false)

    /**
     * Event triggered for network error. Views should use this to get access
     * to the data.
     */
    val eventNetworkError: LiveData<Boolean>
        get() = _eventNetworkError

    /**
     * Flag to display the error message. This is private to avoid exposing a
     * way to set this value to observers.
     */
    private var _isNetworkErrorShown = MutableLiveData<Boolean>(false)

    /**
     * Flag to display the error message. Views should use this to get access
     * to the data.
     */
    val isNetworkErrorShown: LiveData<Boolean>
        get() = _isNetworkErrorShown

    /**
     * Event for navigation to activity fragment.
     *
     * Trigger this singleLiveEvent by setting a new Event as a new value
     *
     * It will be cleared automatically after the toast request,
     * so if the user rotates their phone it won't show a duplicate toast.
     *
     */
    private val _navigateToActivityFragment = MutableLiveData<SingleLiveEvent<Project>>()
    val navigateToActivityFragment: LiveData<SingleLiveEvent<Project>> = _navigateToActivityFragment

    fun doNavigating(project: Project) {
        _navigateToActivityFragment.value = SingleLiveEvent(project)
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
    fun onProjectClicked(project: Project){
        showInSnackBar("Start Project(${project.title})")
        doNavigating(project)
    }

    /**
     * LiveData for this viewModel
     */
    // project list displayed on the screen
    var projects : LiveData<List<Project>> = repository.prjListLive

    /**
     * To initialize the projects variable as soon as possible
     */
    init {
        refreshDataFromRepository()
    }

    /**
     * Refresh data from the repository. Use a coroutine launch to run in a
     * background thread.
     */
    private fun refreshDataFromRepository() {
        uiScope.launch {
            try {
                repository.refreshProjects()
                _eventNetworkError.value = false
                _isNetworkErrorShown.value = false

            } catch (networkError: IOException) {
                // Show a Toast error message and hide the progress bar.
                if(projects.value.isNullOrEmpty()) {
                    _eventNetworkError.value = true
                }
            }
        }
    }


    fun createTestData() {
        createDummyProjectsForTesting()
    }

    fun clearTestData() {
        clearDummyProjectsForTesting()
    }


    /**
     * UseCase: Create dummy project list for testing purpose
     */
    private fun createDummyProjectsForTesting() {
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

        // Create some sample projects and insert them into database.
        for ((index, element) in images.withIndex()) {
            uiScope.launch {
                // Create a new project , which captures the current time,
                // then insert it into the database.
                val project = Project(
                    title = titles[index],
                    imageId = element,
                    priority = index + 1
                )
                repository.insertProject(project.asDatabaseEntity())
            }
        }
    }

    /**
     * UseCase: Clear the dummy project list created for testing purpose
     */
    private fun clearDummyProjectsForTesting() {
        uiScope.launch {
            // Clear the database table.
            repository.clearProjectTable()
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
