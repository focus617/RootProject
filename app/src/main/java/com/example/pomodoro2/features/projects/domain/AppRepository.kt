package com.example.pomodoro2.features.projects.domain

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.pomodoro2.features.infra.database.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

/**
 *  Handling the database operation
 *  TODO: thinking how to integrate into single Repository
 */
public class AppRepository(context: Context) {

    private lateinit var _projectDao: ProjectDAO

    private lateinit var _prjListLive: LiveData<List<Project>>
    var prjListLive: LiveData<List<Project>> = _prjListLive


    init {
        val database: AppDatabase = AppDatabase.getInstance(context.applicationContext)
        _projectDao = database.projectDao
        _prjListLive = Transformations.map(database.projectDao.getAllProjectsLive()){
            it.asDomainModel()
        }

    }

    fun refreshProjects(){

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
    suspend fun getProjectFromDatabase(id: Long): DatabaseProject? {
        return withContext(Dispatchers.IO) {
            _projectDao.getProjectById(id)
        }
    }

    suspend fun getProjectsFromDatabase(): LiveData<List<DatabaseProject>> {
        return _projectDao.getAllProjectsLive()
    }

    suspend fun insertProject(project: DatabaseProject) {
        withContext(Dispatchers.IO) {
            _projectDao.insert(project)
        }
    }

    suspend fun updateProject(project: DatabaseProject) {
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