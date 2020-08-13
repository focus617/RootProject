package com.example.pomodoro2.features.infra.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Defines Database Access Object and methods for using the TaskEntity class with Room.
 */
@Dao
interface TaskDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(taskEntity: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(taskEntities: List<TaskEntity>)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param Tasks new value to write
     */
    @Update
    suspend fun updateTask(taskEntity: TaskEntity)


    @Delete
    suspend fun removeTask(taskEntity: TaskEntity)

   /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
   // TODO: check why Query, not Delete?
    @Query("DELETE FROM TASK_TABLE")
    suspend fun clearTaskTable()

    /**
     * Selects and returns the row that matches the key.
     *
     * @param id key of row to match
     */
    @Query("SELECT * FROM TASK_TABLE WHERE Id = :id")
    suspend fun getTaskById(id: Long): TaskEntity?


    @Query("SELECT * FROM TASK_TABLE ORDER BY priority")
    suspend fun getTasks(): List<TaskEntity>

    /**
     * Selects and returns all rows in the table,
     * Room查询返回 LiveData，查询将自动在后台线程上异步运行。
     *
     * sorted by priority in ascending order.
     */
    @Query("SELECT * FROM TASK_TABLE ORDER BY priority")
    fun getAllTasksLive(): LiveData<List<TaskEntity>>
}