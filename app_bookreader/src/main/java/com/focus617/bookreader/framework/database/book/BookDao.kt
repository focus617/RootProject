package com.focus617.bookreader.framework.database.book

import androidx.room.*
import androidx.room.OnConflictStrategy.REPLACE
import kotlinx.coroutines.flow.Flow

@Dao
interface BookDao {

    @Insert(onConflict = REPLACE)
    suspend fun insert(book: BookEntity)

    @Insert(onConflict = REPLACE)
    fun insertAll(vararg books: BookEntity)

    @Delete
    suspend fun remove(book: BookEntity)

    /**
     * Deletes all values from the table.
     *
     * This does not delete the table, only its contents.
     */
    @Query("DELETE FROM book_table")
    suspend fun clear()

    /**
     * When updating a row with a value already set in a column,
     * replaces the old value with the new one.
     *
     * @param book new value to write
     */
    @Update
    suspend fun update(book: BookEntity)

    /**
     * Selects and returns the row that matches the supplied book key.
     *
     * @param key startTimeMilli to match
     */
    @Query("SELECT * from book_table WHERE uri = :key")
    suspend fun get(key: String): BookEntity?

    /**
     * Selects and returns all rows in the table,
     *
     * sorted by start time in descending order.
     */
    @Query("SELECT * FROM book_table ORDER BY uri DESC")
    suspend fun getAll(): List<BookEntity>

    @Query("SELECT * FROM book_table ORDER BY uri DESC")
    fun getAllByFlow(): Flow<List<BookEntity>>

}
