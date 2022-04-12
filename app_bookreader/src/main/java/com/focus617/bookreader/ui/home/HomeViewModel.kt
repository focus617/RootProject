package com.focus617.bookreader.ui.home

import android.app.Application
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Transformations
import androidx.lifecycle.viewModelScope
import com.focus617.bookreader.R
import com.focus617.bookreader.framework.datasource.WebBookRemoteDataSource
import com.focus617.bookreader.framework.interactors.Interactors
import com.focus617.core.domain.Book
import com.focus617.core.platform.functional.Result
import com.focus617.core.platform.functional.Result.Success
import com.focus617.core.platform.functional.data
import com.focus617.mylib.coroutine.di.ApplicationScope
import com.focus617.platform.event.Event
import com.focus617.platform.uicontroller.BaseViewModel
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.Inject

class HomeViewModel(
    application: Application,
    private val interactors: Interactors
) : BaseViewModel(application) {

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
        loadBooksByFlow()
        initBooksState()
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
    val booksByFlow: Flow<List<Book>> = flow {
        interactors.getBooksUseCase(Unit).mapNotNull { it.data }
    }


    fun loadBooksByFlow() {
        uiScope.launch {
            booksByFlow.collect {
                if (it is Success<*>) {
                    _books.value = it.data as List<Book>
                } else {
                    _books.value = emptyList()
                    showSnackbarMessage(R.string.loading_books_error)
                }
            }
        }
    }


    /** 使用 StateFlow */
    // Backing property to avoid state updates from other classes
    private val _booksState = MutableStateFlow(Result.Success<List<Book>>(emptyList()))

    // The UI collects from this StateFlow to get its state updates
    val booksState: StateFlow<Result<List<Book>>> = _booksState

    private fun initBooksState() {
        viewModelScope.launch {
            interactors.getBooksUseCase(Unit)
                // Update View with the latest books
                // Writes to the value property of MutableStateFlow,
                // adding a new element to the flow and updating all
                // of its collectors
                .collect {
                    _booksState.value = it as Result.Success<List<Book>>
                }
        }
    }


    @Inject
    @ApplicationScope
    lateinit var booksWebDataSource: WebBookRemoteDataSource

    // 可以在UI中直接被collect的Flow
//     val flowList: Flow<List<Book>> = booksWebDataSource.latestBooks

    /**
     * 先将Flow转换为ViewModel的LiveData，在Fragment和ViewModel之间仍然保持LiveData
     */
    // 可以在UI中直接被Observe的LiveData
//     val liveDataList: LiveData<List<Book>> = booksWebDataSource.latestBooks.asLiveData()

    // 或者支持异常处理的函数方法
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