package com.focus617.core.data

import com.focus617.core.data.dataSourceInterface.IfBookDataSourceFlow
import com.focus617.core.domain.Book
import com.focus617.core.platform.functional.Result
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class FakeBookDataSourceFlow : IfBookDataSourceFlow {

    override suspend fun getAllByFlow(): Flow<Result<List<Book>>> {
        return flowOf(Result.Success(emptyList()))
    }
}