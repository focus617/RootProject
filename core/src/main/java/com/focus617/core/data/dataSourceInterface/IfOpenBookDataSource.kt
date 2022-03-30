package com.focus617.core.data.dataSourceInterface

import com.focus617.core.domain.Book


interface IfOpenBookDataSource {

    fun setOpenBook(book: Book)

    fun getOpenBook(): Book
}