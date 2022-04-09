package com.focus617.bookreader.ui.home

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import com.focus617.bookreader.R
import com.focus617.bookreader.framework.datasource.WebBookRemoteDataSource
import com.focus617.bookreader.framework.interactors.Interactors
import com.focus617.core.coroutine.ApplicationScope
import com.focus617.core.domain.Book
import com.focus617.core.platform.functional.Result.Success
import com.focus617.platform.event.Event
import com.focus617.platform.uicontroller.BaseViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import javax.inject.Inject

class HomeViewModel(application: Application, private val interactors: Interactors) :
    BaseViewModel(application) {

    // Declare Job() and cancel jobs in onCleared().
    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    // Define uiScope for coroutines.
    private val uiScope = CoroutineScope(Dispatchers.Main + viewModelJob)

    private val _books = MutableLiveData<List<Book>>().apply { value = emptyList() }
    val books: LiveData<List<Book>> = _books

    /**
     * init{} is called immediately when this ViewModel is created.
     */
    init {
        loadBooks()
    }

    fun loadBooks() {
        uiScope.launch {
            val bookResult = interactors.getBooks()
            if (bookResult is Success) {
                _books.value = bookResult.data
            } else {
                _books.value = emptyList()
                showSnackbarMessage(R.string.loading_books_error)
            }

        }
    }

    fun addDocument(uri: Uri) {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                interactors.addBook(
                    Book(
                        uri.toString(), "", "", 0, "", "", ""
                    )
                )
            }
            loadBooks()
        }
    }

    /**
     * Executes when the CLEAR button is clicked.
     */
    fun onClear() {
        uiScope.launch {
            withContext(Dispatchers.IO) {
                // Clear the database table.
                interactors.clearBooks()
            }
        }
        loadBooks()
    }

    /**
     * Executes when the fab button is clicked.
     */
    fun onMenuAdd() {
        uiScope.launch {
            // Create a new book and insert it into the database.
            interactors.addBook(Book.ExampleBook)
        }
    }


    /**
     * If there are any books in the database, show the CLEAR button.
     */
    val clearButtonVisible = Transformations.map(books) {
        it?.isNotEmpty()
    }

    // To handle navigation to the selected book
    private val _navigateToSelectedBookEvent = MutableLiveData<Event<Book>>()
    val navigateToSelectedBookEvent: LiveData<Event<Book>> = _navigateToSelectedBookEvent

    /**
     * Executes when the book item in recyclerView is clicked.
     * It will trigger the navigation to BookDetail Fragment
     */
    fun displayBookDetail(book: Book) {
        _navigateToSelectedBookEvent.value = Event(book)
    }

    /**
     * Following functions is used for Flow testing
     */
    /** 在Fragment中使用Flow直接更新UI */
    suspend fun loadBooksByFlow(): Flow<List<Book>> = interactors.getBooksByFlow()

    @Inject
    @ApplicationScope
    lateinit var booksWebDataSource: WebBookRemoteDataSource

    /**
     * 先将Flow转换为ViewModel的LiveData，在Fragment和ViewModel之间仍然保持LiveData
      */
    fun loadBooksByWeb() {
        uiScope.launch {
            // Trigger the flow and consume its elements using collect
            booksWebDataSource.latestBooks
                // Intermediate catch operator. If an exception is thrown,
                // catch and update the UI
                .catch { exception -> notifyError(exception) }
                .collect {
                    // Update View with the latest books
                    _books.value = it
                }
        }
    }

    fun notifyError(exception: Throwable) {
        exception.printStackTrace()
    }
}