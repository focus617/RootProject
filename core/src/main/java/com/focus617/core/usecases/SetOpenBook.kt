package com.focus617.core.usecases

import com.focus617.core.data.dataSourceInterface.IfBookRepository
import com.focus617.core.domain.Book
import javax.inject.Inject

class SetOpenBook @Inject constructor(private val bookRepository: IfBookRepository) {
    operator fun invoke(book: Book) = bookRepository.setOpenBook(book)
}