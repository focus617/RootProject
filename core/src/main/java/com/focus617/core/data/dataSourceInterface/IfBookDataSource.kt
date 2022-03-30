package com.focus617.core.data.dataSourceInterface

import com.focus617.core.domain.Book
import com.focus617.core.platform.functional.Result


interface IfBookDataSource {

    suspend fun add(book: Book)

    suspend fun readAll(): Result<List<Book>>

    suspend fun remove(book: Book)

    suspend fun clear()
}