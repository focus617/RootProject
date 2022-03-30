package com.focus617.core.data.dataSourceInterface

import com.focus617.core.domain.Bookmark
import com.focus617.core.domain.Book

interface IfBookmarkDataSource {

    suspend fun add(book: Book, bookmark: Bookmark)

    suspend fun read(book: Book): List<Bookmark>

    suspend fun remove(book: Book, bookmark: Bookmark)
}