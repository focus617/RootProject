package com.focus617.bookreader.framework.database.book

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.focus617.core.domain.Book

@Entity(tableName = "book_table")
data class BookEntity(
    @PrimaryKey val uri: String,
    @ColumnInfo(name = "title") val title: String,
    @ColumnInfo(name = "author") val author: String,
    @ColumnInfo(name = "size") val size: Int,
    @ColumnInfo(name = "thumbnail_uri") val thumbnailUri: String,
    @ColumnInfo(name = "description") val description: String,
    @ColumnInfo(name = "image_src_uri") val imgSrcUrl: String
) {
    companion object {
        fun from(book: Book): BookEntity = book.run {
            BookEntity(
                this.url,
                this.title,
                this.author,
                this.size,
                this.thumbnail,
                this.description,
                this.imgSrcUrl
            )
        }

    }
}

fun List<BookEntity>.asDomainModel(): List<Book> {
    return map {
        Book(
            it.uri,
            it.title,
            it.author,
            it.size,
            it.thumbnailUri,
            it.description,
            it.imgSrcUrl
        )
    }
}

