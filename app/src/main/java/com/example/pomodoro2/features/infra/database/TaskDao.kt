package com.example.pomodoro2.features.infra.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Defines Database Access Object and methods for using the TaskEntity class with Room.
 */
@Dao
interface TaskDao {

    /**
     * Insert a task in the database. If the task already exists, replace it.
     *
     * @param taskEntity the task to be inserted.
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTask(taskEntity: TaskEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTasks(taskEntities: List<TaskEntity>)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param taskEntity new value to write
     * @return the number of tasks updated. This should always be 1.
     */
    @Update
    suspend fun updateTask(taskEntity: TaskEntity): Int

    /**
     * Update the complete status of a task
     *
     * @param taskId    id of the task
     * @param completed status to be updated
     */
    @Query("UPDATE TASK_TABLE SET Completed = :completed WHERE Id = :taskId")
    suspend fun updateCompleted(taskId: Long, completed: Boolean)


    /**
     * Delete a task by id.
     *
     * @return the number of tasks deleted. This should always be 1.
     */
    @Query("DELETE FROM TASK_TABLE WHERE Id = :taskId")
    suspend fun deleteTaskById(taskId: Long): Int

    /**
     * Deletes all tasks from the table.
     *
     * This does not delete the table, only its contents.
    */
    @Query("DELETE FROM TASK_TABLE")
    suspend fun deleteTasks()

    /**
     * Delete all completed tasks from the table.
     *
     * @return the number of tasks deleted.
     */
    @Query("DELETE FROM TASK_TABLE WHERE completed = 1")
    suspend fun deleteCompletedTasks(): Int

    /**
     * Selects and returns the row that matches the key.
     *
     * @param taskId id of the task
     * @return the entity match the Id.
     */
    @Query("SELECT * FROM TASK_TABLE WHERE Id = :taskId")
    suspend fun getTaskById(taskId: Long): TaskEntity?

    /**
     * Select all tasks from the tasks table.
     *
     * @return all tasks.
     */
    @Query("SELECT * FROM TASK_TABLE ORDER BY priority")
    suspend fun getTasks(): List<TaskEntity>

    /**
     * Selects and returns all rows in the table,
     * Room查询返回 LiveData，查询将自动在后台线程上异步运行。
     *
     * sorted by priority in ascending order.
     */
//    @Query("SELECT * FROM TASK_TABLE ORDER BY priority")
//    fun getAllTasksLive(): LiveData<List<TaskEntity>>
}