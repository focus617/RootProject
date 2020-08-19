package com.example.pomodoro2.features.data.network

import android.content.Context
import com.example.pomodoro2.platform.data.INetworkDataSource
import com.example.pomodoro2.features.data.database.AppDatabase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber

/**
 *  Handling the data operation via network
 *  TODO: thinking how to integrate into Repository, and synchronization strategy
 */
public class AppNetworkDataSource(val context: Context): INetworkDataSource{

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

    override fun getTasksNum(): Int = 5

}