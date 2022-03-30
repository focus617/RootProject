package com.focus617.bookreader.framework.datasource

import android.content.Context
import com.focus617.bookreader.di.DatabaseModule
import com.focus617.bookreader.framework.database.bookmark.BookmarkEntity
import com.focus617.bookreader.framework.database.bookmark.asDomainModel
import com.focus617.core.data.dataSourceInterface.IfBookmarkDataSource
import com.focus617.core.domain.Book
import com.focus617.core.domain.Bookmark
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

class RoomBookmarkDataSource @Inject constructor(
    @ApplicationContext val context: Context
) : IfBookmarkDataSource {

    //    private val bookmarkDao = BookReaderDatabase.getInstance(context).bookmarkDao
    var database = DatabaseModule.provideDatabase(context)
    private val bookmarkDao = database.bookmarkDao

    override suspend fun add(book: Book, bookmark: Bookmark) =
        bookmarkDao.addBookmark(
            BookmarkEntity(bookUri = book.url, page = bookmark.page)
        )

    override suspend fun read(book: Book): List<Bookmark> =
        bookmarkDao.getBookmarks(book.url).asDomainModel()

    override suspend fun remove(book: Book, bookmark: Bookmark) =
        bookmarkDao.removeBookmark(
            BookmarkEntity(id = bookmark.id, bookUri = book.url, page = bookmark.page)
        )
}