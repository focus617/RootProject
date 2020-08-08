package com.example.pomodoro2.features.projects.domain

import android.content.Context
import androidx.lifecycle.LiveData
import com.example.pomodoro2.features.infra.database.AppDatabase
import com.example.pomodoro2.features.infra.database.Project
import com.example.pomodoro2.features.infra.database.ProjectDAO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *  Handling the database operation
 *  TODO: thinking how to integrate into single Repository
 */
public class AppRepository {

    private lateinit var _projectDao: ProjectDAO

    private lateinit var _prjListLive: LiveData<List<Project>>
    var prjListLive: LiveData<List<Project>> = _prjListLive


    fun AppRepository(context: Context) {
        val database: AppDatabase = AppDatabase.getInstance(context.applicationContext)
        _projectDao = database.projectDao
        _prjListLive = database.projectDao.getAllProjectsLive()
    }

    /**
     * Suspend functions to do the long-running work,
     * so that you don't block the UI thread while waiting for the result.
     *
     * Suspend functions return the result from a coroutine that runs in the Dispatchers.IO context.
     * Use the I/O dispatcher, because getting data from the database is an I/O operation
     * and has nothing to do with the UI.
     *
     **/
    suspend fun getProjectFromDatabase(id: Long): Project? {
        return withContext(Dispatchers.IO) {
            _projectDao.getProjectById(id)
        }
    }

    suspend fun getProjectsFromDatabase(): LiveData<List<Project>> {
        return _projectDao.getAllProjectsLive()
    }

    suspend fun insertProject(project: Project) {
        withContext(Dispatchers.IO) {
            _projectDao.insert(project)
        }
    }

    suspend fun updateProject(project: Project) {
        withContext(Dispatchers.IO) {
            _projectDao.update(project)
        }
    }

    suspend fun clearProjectTable() {
        withContext(Dispatchers.IO) {
            _projectDao.clear()
        }
    }
}