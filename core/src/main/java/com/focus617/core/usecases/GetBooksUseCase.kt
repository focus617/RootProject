package com.focus617.core.usecases

import com.focus617.core.data.dataSourceInterface.IfBookRepository
import com.focus617.core.domain.Book
import com.focus617.core.platform.functional.Result
import com.focus617.core.platform.usecase.FlowUseCase
import com.focus617.mylib.coroutine.di.IoDispatcher
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetBooksUseCase @Inject constructor(
    private val bookRepository: IfBookRepository,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : FlowUseCase<Unit, List<Book>>(ioDispatcher) {

    override suspend fun execute(parameters: Unit): Flow<Result<List<Book>>> =
        bookRepository.getBooksByFlow()

}
