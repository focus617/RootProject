package com.focus617.core.data.dataSourceInterface

import com.focus617.core.domain.Book
import com.focus617.core.domain.Bookmark

interface IfBookmarkRepository {

    suspend fun addBookmark(book: Book, bookmark: Bookmark)

    suspend fun getBookmarks(book: Book): List<Bookmark>

    suspend fun removeBookmark(book: Book, bookmark: Bookmark)

}