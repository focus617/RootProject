package com.focus617.core.usecases

import com.focus617.core.data.dataSourceInterface.IfBookmarkRepository
import com.focus617.core.domain.Bookmark
import com.focus617.core.domain.Book
import javax.inject.Inject

class RemoveBookmark @Inject constructor(private val bookmarksRepository: IfBookmarkRepository) {
    suspend operator fun invoke(book: Book, bookmark: Bookmark) = bookmarksRepository
        .removeBookmark(book, bookmark)
}