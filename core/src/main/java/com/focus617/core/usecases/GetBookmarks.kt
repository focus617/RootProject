package com.focus617.core.usecases

import com.focus617.core.data.BookmarkRepository
import com.focus617.core.domain.Book
import javax.inject.Inject


class GetBookmarks @Inject constructor(private val bookmarkRepository: BookmarkRepository) {

    suspend operator fun invoke(book: Book) = bookmarkRepository.getBookmarks(book)
}