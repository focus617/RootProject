package com.focus617.core.data

import com.focus617.core.data.dataSourceInterface.IfBookDataSource
import com.focus617.core.domain.Book
import com.focus617.core.platform.functional.Result


class FakeBookDataSource(var books: MutableList<Book>? = mutableListOf()) : IfBookDataSource {
    override suspend fun add(book: Book) {
        books?.add(book)
    }

    override suspend fun readAll(): Result<List<Book>> {
        books?.let { return Result.Success(it) }
        return Result.Error(
            Exception("Tasks not found")
        )
    }

    override suspend fun remove(book: Book) {
        books?.remove(book)
    }

    override suspend fun clear() {
        books?.clear()
    }

}