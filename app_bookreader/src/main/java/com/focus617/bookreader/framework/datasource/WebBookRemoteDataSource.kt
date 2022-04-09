package com.focus617.bookreader.framework.datasource

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.focus617.bookreader.framework.network.BooksApiFilter
import com.focus617.bookreader.framework.network.WebApiService
import com.focus617.bookreader.framework.network.asDomainModel
import com.focus617.core.domain.Book
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.launch
import javax.inject.Inject


enum class WebApiStatus { LOADING, ERROR, DONE }

class WebBookRemoteDataSource @Inject constructor(
    private val booksApi: WebApiService,
    private val refreshIntervalMs: Long = 5000
) {
    // Testing Flow
    // Data source fetches the latest books automatically at a fixed interval.
    val latestBooks: Flow<List<Book>> = flow {
        while (true) {
            val latestBooks = booksApi.loadBooks("").asDomainModel()
            emit(latestBooks)           // Emits the result of the request to the flow
            delay(refreshIntervalMs)    // Suspends the coroutine for some time
        }
    }

    // The internal MutableLiveData that stores the status of the most recent request
    private val _status = MutableLiveData<WebApiStatus>()

    // The external immutable LiveData for the request status
    val status: LiveData<WebApiStatus>
        get() = _status

    /**
     * Gets filtered Mars real estate property information from the Mars API Retrofit service and
     * updates the [Book] [List] and [WebApiStatus] [LiveData]. The Retrofit service
     * returns a coroutine Deferred, which we await to get the result of the transaction.
     * @param filter the [BooksApiFilter] that is sent as part of the web server request
     */
    private fun loadBooksFromWebSite(filter: BooksApiFilter = BooksApiFilter.SHOW_ALL) {
        GlobalScope.launch {
            _status.value = WebApiStatus.LOADING
            try {
//                _selectedBook.value = BooksWebApi.retrofitService.loadBook(filter.value)
                _status.value = WebApiStatus.DONE
            } catch (e: Exception) {
                _status.value = WebApiStatus.ERROR
//                _selectedBook.value = ArrayList()
            }
        }
    }

}