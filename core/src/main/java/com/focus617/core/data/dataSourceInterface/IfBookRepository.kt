package com.focus617.core.data.dataSourceInterface

import com.focus617.core.domain.Book
import com.focus617.core.platform.functional.Result

/**
 * Interface to the data layer.
 */
interface IfBookRepository {

    suspend fun getBooks(): Result<List<Book>>

    suspend fun addBook(book: Book)

    suspend fun removeBook(book: Book)

    suspend fun clearBooks()

    fun setOpenBook(book: Book)

    fun getOpenBook(): Book

}