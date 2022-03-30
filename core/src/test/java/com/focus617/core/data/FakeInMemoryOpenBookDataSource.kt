package com.focus617.core.data

import com.focus617.core.data.dataSourceInterface.IfOpenBookDataSource
import com.focus617.core.domain.Book

class FakeInMemoryOpenBookDataSource : IfOpenBookDataSource {

    private var openBook: Book = Book.EMPTY

    override fun setOpenBook(book: Book) {
        openBook = book
    }

    override fun getOpenBook() = openBook
}