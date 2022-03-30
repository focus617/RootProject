package com.focus617.bookreader.framework.datasource

import com.focus617.core.data.dataSourceInterface.IfOpenBookDataSource
import com.focus617.core.domain.Book
import javax.inject.Inject

class InMemoryOpenBookDataSource @Inject constructor(): IfOpenBookDataSource {

    private var openBook: Book = Book.EMPTY

    override fun setOpenBook(book: Book) {
        openBook = book
    }

    override fun getOpenBook() = openBook
}