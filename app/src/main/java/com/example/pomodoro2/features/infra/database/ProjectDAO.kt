package com.example.pomodoro2.features.infra.database

import androidx.lifecycle.LiveData
import androidx.room.*

/**
 * Defines Database Access Object and methods for using the DatabaseProject class with Room.
 */
@Dao
interface ProjectDAO {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(vararg projects: DatabaseProject?)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll( projects: List<DatabaseProject>)

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param Projects new value to write
     */
    @Update
    fun update(vararg projects: DatabaseProject?)

    @Delete
    fun deleteProject(vararg projects: DatabaseProject?)

   /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
   // TODO: check why Query, not Delete?
    @Query("DELETE FROM PROJECT_TABLE")
    fun clear()

    /**
     * Selects and returns the row that matches the key.
     *
     * @param id key of row to match
     */
    @Query("SELECT * FROM PROJECT_TABLE WHERE Id = :id")
    fun getProjectById(id: Long): DatabaseProject?

    /**
     * Selects and returns all rows in the table,
     * Room查询返回 LiveData，查询将自动在后台线程上异步运行。
     *
     * sorted by priority in ascending order.
     */
    @Query("SELECT * FROM PROJECT_TABLE ORDER BY priority")
    fun getAllProjectsLive(): LiveData<List<DatabaseProject>>
}