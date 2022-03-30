package com.focus617.core.usecases

import com.focus617.core.data.dataSourceInterface.IfBookRepository
import javax.inject.Inject

class GetOpenBook @Inject constructor(private val bookRepository: IfBookRepository) {
    operator fun invoke() = bookRepository.getOpenBook()
}
