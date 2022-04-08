package com.focus617.core.data.dataSourceInterface

import com.focus617.core.domain.Book
import kotlinx.coroutines.flow.Flow


interface IfBookDataSourceFlow {
    suspend fun getAllByFlow(): Flow<List<Book>>
}