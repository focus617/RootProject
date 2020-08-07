package com.example.pomodoro2.features.projects.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pomodoro2.features.infra.database.Project
import com.example.pomodoro2.features.infra.database.ProjectDAO
import kotlinx.coroutines.*

/**
 * ViewModel for ProjectFragment.
 */
class ProjectsViewModel(val database: ProjectDAO, application: Application) :
    AndroidViewModel(application) {

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


    private var currentProject = MutableLiveData<Project?>()

    private var projects = database.getAllProjectsLive()

    /**
     * To initialize the projects variable as soon as possible
     */
    init {
        initializeProjects()
    }

    private fun initializeProjects() {
        uiScope.launch {
            projects = getProjectsFromDatabase()
        }
    }

    /**
     * Return the result from a coroutine that runs in the Dispatchers.IO context.
     * Use the I/O dispatcher, because getting data from the database is an I/O operation
     * and has nothing to do with the UI.
     */
    private suspend fun getProjectsFromDatabase(): LiveData<List<Project>> {
        return withContext(Dispatchers.IO) {
            database.getAllProjectsLive()
        }
    }

    // TODO: for initial debug&testing only, will remove in future
    private val _text = MutableLiveData<String>().apply {
        value = "This is goal & project Fragment"
    }
    val text: LiveData<String> = _text
}