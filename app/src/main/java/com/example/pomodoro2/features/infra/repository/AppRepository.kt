package com.example.pomodoro2.features.infra.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.pomodoro2.domain.Project
import com.example.pomodoro2.features.infra.database.*
import com.example.pomodoro2.features.infra.network.Api
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 *  Handling the database operation
 *  TODO: thinking how to integrate into single Repository
 */
public class AppRepository(context: Context) {

    // singleton database instance
    val database: AppDatabase = AppDatabase.getInstance(context.applicationContext)

    // singleton Retrofit service instance
    val api = Api.create()

    /**
     * LiveData kept inside Repository
     */
    val prjListLive: LiveData<List<Project>> =
        Transformations.map(database.projectDao.getAllProjectsLive()){
            it.asDomainModel()
        }

    /**
     * Refresh the database entities stored in the offline cache.
     *
     * This function uses the IO dispatcher to ensure the database insert database operation
     * happens on the IO dispatcher. By switching to the IO dispatcher using `withContext` this
     * function is now safe to call from any thread including the Main thread.
     *
     */
    suspend fun refreshProjects(){
        withContext(Dispatchers.IO) {
            Timber.d("refresh projects is called");
            val user = api.getPropertiesAsync().await()

            // Store into local cache
            // database.projectDao.insertAll(projectList.asDatabaseModel())
        }
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
    private val _projectDao = database.projectDao

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