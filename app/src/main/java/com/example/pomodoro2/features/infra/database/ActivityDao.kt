package com.example.pomodoro2.features.infra.database

import androidx.room.*

/**
 * Defines Database Access Object and methods for using the DatabaseActivity class with Room.
 */
@Dao
interface ActivityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: ActivityEntity)

    @Query("SELECT * FROM ACTIVITY_TABLE WHERE taskID = :taskID")
    suspend fun getActivities(taskID: Long): List<ActivityEntity>

    @Delete
    suspend fun removeActivity(activity: ActivityEntity)

}