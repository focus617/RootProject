package com.focus617.core.data

import com.focus617.core.data.dataSourceInterface.IfBookmarkDataSource
import com.focus617.core.data.dataSourceInterface.IfBookmarkRepository
import com.focus617.core.domain.Book
import com.focus617.core.domain.Bookmark
import com.focus617.core.platform.base.BaseEntity
import javax.inject.Inject

class BookmarkRepository @Inject constructor(
    private val dataSource: IfBookmarkDataSource
) : BaseEntity(), IfBookmarkRepository {

    override suspend fun addBookmark(book: Book, bookmark: Bookmark) =
        dataSource.add(book, bookmark)

    override suspend fun getBookmarks(book: Book) = dataSource.read(book)

    override suspend fun removeBookmark(book: Book, bookmark: Bookmark) =
        dataSource.remove(book, bookmark)
}