package com.focus617.bookreader.framework.interactors

import com.focus617.core.usecases.*
import javax.inject.Inject

// use it to access interactors from ViewModels.
data class Interactors @Inject constructor(
    val addBook: AddBook,
    val getBooks: GetBooks,
    val removeBook: RemoveBook,
    val clearBooks: ClearBooks,
    val getOpenBook: GetOpenBook,
    val setOpenBook: SetOpenBook,
    val addBookmark: AddBookmark,
    val getBookmarks: GetBookmarks,
    val deleteBookmark: RemoveBookmark,
    val getBooksUseCase: GetBooksUseCase
)
