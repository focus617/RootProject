package com.example.pomodoro2.features.data.database

import androidx.room.*

/**
 * Defines Database Access Object and methods for using the DatabaseActivity class with Room.
 */
@Dao
interface ActivityDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertActivity(activity: ActivityEntity)

    @Query("SELECT * FROM ACTIVITY_TABLE WHERE taskID = :taskID")
    suspend fun getActivities(taskID: String): List<ActivityEntity>

    @Delete
    suspend fun removeActivity(activity: ActivityEntity)

    @Query("DELETE FROM ACTIVITY_TABLE")
    suspend fun clearActivityTable()
}