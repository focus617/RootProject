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

    suspend fun getProjectFromDatabase(id: Long): Project? {
        return withContext(Dispatchers.IO) {
            var project = _projectDao.getProjectById(id)
            project
        }
    }

    private suspend fun insert(project: Project) {
        withContext(Dispatchers.IO) {
            _projectDao.insert(project)
        }
    }

    private suspend fun update(night: Project) {
        withContext(Dispatchers.IO) {
            _projectDao.update(night)
        }
    }

    private suspend fun clear() {
        withContext(Dispatchers.IO) {
            _projectDao.clear()
        }
    }
}