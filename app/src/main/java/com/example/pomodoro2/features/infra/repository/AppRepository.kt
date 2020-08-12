package com.example.pomodoro2.features.infra.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.Transformations
import com.example.pomodoro2.domain.Task
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
    val db = AppDatabase.getInstance(context.applicationContext)

    // singleton Retrofit service instance
    val api = Api.create()

    // Network Status
    var status: Api.Companion.ApiStatus = Api.Companion.ApiStatus.DONE

    fun closeDB() {
        if (db.isOpen) db.close()
    }

    /**
     * LiveData object kept inside Repository, which is automatically updated when the database is updated.
     */
    val taskListLive: LiveData<List<Task>> =
        Transformations.map(db.taskDao.getAllTasksLive()){
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
    suspend fun refreshTasks(){
        withContext(Dispatchers.IO) {
            Timber.d("refresh tasks is called");

            // creates and starts the network call on a background thread
            var getPropertiesDeferred = api.getPropertiesAsync()

            try {
                status = Api.Companion.ApiStatus.LOADING
                var taskList = getPropertiesDeferred.await()
                status = Api.Companion.ApiStatus.DONE
            } catch (e: Exception) {
                status = Api.Companion.ApiStatus.ERROR
            }

            // Store into database as local cache
            // database.taskDao.insertAll(taskList.asDatabaseModel())
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
    suspend fun getTaskFromDatabase(id: Long): TaskEntity? {
        return withContext(Dispatchers.IO) {
            db.taskDao.getTaskById(id)
        }
    }

    suspend fun getTasksFromDatabase(): LiveData<List<TaskEntity>> {
        return db.taskDao.getAllTasksLive()
    }

    suspend fun insertTask(taskEntity: TaskEntity) {
        withContext(Dispatchers.IO) {
            db.taskDao.insertTask(taskEntity)
        }
    }

    suspend fun updateTask(taskEntity: TaskEntity) {
        withContext(Dispatchers.IO) {
            db.taskDao.updateTask(taskEntity)
        }
    }

    suspend fun clearTaskTable() {
        withContext(Dispatchers.IO) {
            db.taskDao.clearTaskTable()
        }
    }
}