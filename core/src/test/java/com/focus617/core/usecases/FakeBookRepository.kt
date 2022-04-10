/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.focus617.core.usecases

import androidx.annotation.VisibleForTesting
import com.focus617.core.data.dataSourceInterface.IfBookRepository
import com.focus617.core.domain.Book
import com.focus617.core.platform.functional.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
class FakeBookRepository : IfBookRepository {

    var booksServiceData: LinkedHashMap<String, Book> = LinkedHashMap()

    private var shouldReturnError = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    override suspend fun getBooks(): Result<List<Book>> {
        if (shouldReturnError) {
            return Result.Error(Exception("Test exception"))
        }
        return Result.Success(booksServiceData.values.toList())
    }

    suspend fun getBook(bookUrl: String): Result<Book> {
        if (shouldReturnError) {
            return Result.Error(Exception("Test exception"))
        }
        booksServiceData[bookUrl]?.let {
            return Result.Success(it)
        }
        return Result.Error(Exception("Could not find book"))
    }

    override suspend fun addBook(book: Book) {
        booksServiceData[book.url] = book
    }

    @VisibleForTesting
    fun addBooks(vararg books: Book) {
        for (book in books) {
            booksServiceData[book.url] = book
        }
    }

    override suspend fun removeBook(book: Book) {
        booksServiceData.remove(book.url)
    }

    override suspend fun clearBooks() {
        booksServiceData.clear()
    }

    private var openBook: Book = Book.EMPTY

    override fun setOpenBook(book: Book) {
        openBook = book
    }

    override fun getOpenBook(): Book = openBook

    override suspend fun getBooksByFlow(): Flow<Result<List<Book>>> =
        flowOf(Result.Success(booksServiceData.values.toList()))

}
