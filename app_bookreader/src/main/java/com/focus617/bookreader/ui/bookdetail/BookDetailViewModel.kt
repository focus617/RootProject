package com.focus617.bookreader.ui.bookdetail

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.focus617.bookreader.framework.interactors.Interactors
import com.focus617.bookreader.ui.util.formatBooks
import com.focus617.core.domain.Book
import com.focus617.core.domain.Bookmark
import com.focus617.platform.uicontroller.BaseViewModel
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class BookDetailViewModel(application: Application, val interactors: Interactors) :
    BaseViewModel(application) {

    private val _selectedBook = MutableLiveData<Book>()

    // The external LiveData for the selectedBook
    val selectedBook: LiveData<Book>
        get() = _selectedBook

    /**
     * Converted book to Spanned for displaying.
     */
    val bookString = Transformations.map(selectedBook) { book ->
        formatBooks(book, application.resources)
    }

    fun loadBook(bookFromArgs: Book) {
        val lastOpenBook = interactors.getOpenBook()

        _selectedBook.value = when {
            bookFromArgs != Book.EMPTY -> bookFromArgs
            bookFromArgs == Book.EMPTY && lastOpenBook != Book.EMPTY -> lastOpenBook
            else -> Book.ExampleBook
        }

        selectedBook.value?.let { interactors.setOpenBook(it) }
    }


    val bookmarks = MediatorLiveData<List<Bookmark>>().apply {
        addSource(selectedBook) { book ->
            GlobalScope.launch {
                postValue(interactors.getBookmarks(book))
            }
        }
    }
}