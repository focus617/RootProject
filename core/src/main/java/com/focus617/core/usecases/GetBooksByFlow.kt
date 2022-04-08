package com.focus617.core.usecases

import com.focus617.core.data.dataSourceInterface.IfBookRepository
import javax.inject.Inject

class GetBooksByFlow @Inject constructor(private val bookRepository: IfBookRepository) {
    suspend operator fun invoke() = bookRepository.getBooksByFlow()
}
