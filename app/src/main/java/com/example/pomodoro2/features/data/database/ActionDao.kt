package com.example.pomodoro2.features.data.database

import androidx.room.*

/**
 * Defines Database Access Object and methods for using the DatabaseActivity class with Room.
 */
@Dao
interface ActionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAction(action: ActionEntity)

    @Query("SELECT * FROM ACTION_TABLE WHERE taskID = :taskID")
    suspend fun getAction(taskID: String): List<ActionEntity>

    @Delete
    suspend fun removeAction(action: ActionEntity)

    @Query("DELETE FROM ACTION_TABLE")
    suspend fun clearActionTable()
}