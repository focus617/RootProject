package com.focus617.core.data.dataSourceInterface

import com.focus617.core.domain.Book
import com.focus617.core.platform.functional.Result
import kotlinx.coroutines.flow.Flow


interface IfBookDataSourceFlow {
    suspend fun getAllByFlow(): Flow<Result<List<Book>>>
}