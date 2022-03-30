package com.focus617.bookreader.framework.database.bookmark

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy.REPLACE
import androidx.room.Query

@Dao
interface BookmarkDao {

    @Insert(onConflict = REPLACE)
    suspend fun addBookmark(bookmark: BookmarkEntity)

    @Query("SELECT * FROM bookmark_table WHERE bookUri = :bookUri")
    suspend fun getBookmarks(bookUri: String): List<BookmarkEntity>

    @Delete
    suspend fun removeBookmark(bookmark: BookmarkEntity)
}