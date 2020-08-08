package com.example.pomodoro2.features.projects.presentation

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.pomodoro2.R
import com.example.pomodoro2.features.infra.database.Project
import com.example.pomodoro2.features.infra.database.ProjectDAO
import kotlinx.coroutines.*

/**
 * ViewModel for ProjectFragment.
 */
class ProjectsViewModel(val dataSource: ProjectDAO, application: Application) :
    AndroidViewModel(application) {

    private var _projectDao: ProjectDAO = dataSource

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


    private var _currentProject = MutableLiveData<Project?>()

    private var projects = dataSource.getAllProjectsLive()

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


    fun createTestData() {
        createDummyProjectsForTesting(dataSource)
    }

    fun clearTestData() {
        clearDummyProjectsForTesting(dataSource)
    }


    /**
     * UseCase: Create dummy project list for testing purpose
     */
    fun createDummyProjectsForTesting(dataSource: ProjectDAO) {
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
        for (i in images.indices) {
            uiScope.launch {
                // Create a new project , which captures the current time,
                // then insert it into the database.
                val project = Project(title = titles[i], imageId = images[i], priority = i + 1)
                insertProject(project)
            }
        }
    }

    /**
     * UseCase: Clear the dummy project list created for testing purpose
     */
    fun clearDummyProjectsForTesting(dataSource: ProjectDAO) {
        uiScope.launch {
            // Clear the database table.
            clearProjectTable()

            // And clear tonight since it's no longer in the database
            _currentProject.value = null

            // Show a snackbar message, because it's friendly.
            _showSnackbarEvent.value = true
        }
    }

    // TODO: Below functions will be integrated into Repository

    /**
     * Suspend functions to do the long-running work,
     * so that you don't block the UI thread while waiting for the result.
     *
     * Suspend functions return the result from a coroutine that runs in the Dispatchers.IO context.
     * Use the I/O dispatcher, because getting data from the database is an I/O operation
     * and has nothing to do with the UI.
     *
     **/
    private suspend fun getProjectFromDatabase(id: Long): Project? {
        return withContext(Dispatchers.IO) {
            _projectDao.getProjectById(id)
        }
    }

    private suspend fun getProjectsFromDatabase(): LiveData<List<Project>> {
        return _projectDao.getAllProjectsLive()
    }

    private suspend fun insertProject(project: Project) {
        withContext(Dispatchers.IO) {
            _projectDao.insert(project)
        }
    }

    private suspend fun updateProject(project: Project) {
        withContext(Dispatchers.IO) {
            _projectDao.update(project)
        }
    }

    private suspend fun clearProjectTable() {
        withContext(Dispatchers.IO) {
            _projectDao.clear()
        }
    }


    // TODO: for initial debug&testing only, will remove in future
    private val _text = MutableLiveData<String>().apply {
        value = "This is goal & project Fragment"
    }
    val text: LiveData<String> = _text


    /**
     * Request a toast by setting this value to true.
     *
     * This is private because we don't want to expose setting this value to the Fragment.
     */
    private var _showSnackbarEvent = MutableLiveData<Boolean>()

    /**
     * If this is true, immediately `show()` a toast and call `doneShowingSnackbar()`.
     */
    val showSnackBarEvent: LiveData<Boolean>
        get() = _showSnackbarEvent

    /**
     * Call this immediately after calling `show()` on a toast.
     *
     * It will clear the toast request, so if the user rotates their phone it won't show a duplicate
     * toast.
     */
    fun doneShowingSnackbar() {
        _showSnackbarEvent.value = false
    }
}
