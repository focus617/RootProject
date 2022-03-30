package com.focus617.bookreader.framework.database.bookmark

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.focus617.core.domain.Bookmark

@Entity(tableName = "bookmark_table")
data class BookmarkEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,

    @ColumnInfo(name = "bookUri")
    val bookUri: String,

    @ColumnInfo(name = "page")
    val page: Int
)

fun List<BookmarkEntity>.asDomainModel(): List<Bookmark> {
    return map {
        Bookmark(it.id, it.page)
    }
}