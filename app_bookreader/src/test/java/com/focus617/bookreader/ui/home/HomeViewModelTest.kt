package com.focus617.bookreader.ui.home

import androidx.test.core.app.ApplicationProvider
import com.focus617.bookreader.MainCoroutineRule
import com.focus617.bookreader.data.FakeBookRepository
import com.focus617.bookreader.framework.interactors.Interactors
import com.focus617.core.data.BookmarkRepository
import com.focus617.core.data.dataSourceInterface.IfBookmarkDataSource
import com.focus617.core.domain.Book
import com.focus617.core.domain.Bookmark
import com.focus617.core.usecases.*
import com.nhaarman.mockito_kotlin.mock
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule


/**
 * Unit tests for the implementation of [HomeViewModel]
 */
@ExperimentalCoroutinesApi
class HomeViewModelTest {

    // Subject under test
    private lateinit var viewModel: HomeViewModel

    // Use a fake repository to be injected into the viewmodel
    private lateinit var bookRepository: FakeBookRepository

    private val bookmarkDataSource: IfBookmarkDataSource = mock()
    private val bookmarkRepository = BookmarkRepository(bookmarkDataSource)

    // Set the main coroutines dispatcher for unit testing.
    @ExperimentalCoroutinesApi
    @get:Rule
    var mainCoroutineRule = MainCoroutineRule()

    companion object {
        const val FAKE_ID = 1
        val fakeBookmark = Bookmark(id = FAKE_ID, page = 100)
    }

    @Before
    fun setupViewModel() {

        // We initialise the tasks to 3, with one active and two completed
        bookRepository = FakeBookRepository()

        // Given a repository with 3 books:
        bookRepository.addBooks(
            Book("Book_first", "", "", 0, "", "", ""),
            Book("Book_second", "", "", 0, "", "", ""),
            Book("Book_third", "", "", 0, "", "", "")
        )

        viewModel = HomeViewModel(
            // Given a application context from Androidx Test Library
            ApplicationProvider.getApplicationContext(),
            Interactors(
                AddBook(bookRepository),
                GetBooks(bookRepository),
                RemoveBook(bookRepository),
                ClearBooks(bookRepository),
                GetOpenBook(bookRepository),
                SetOpenBook(bookRepository),
                AddBookmark(bookmarkRepository),
                GetBookmarks(bookmarkRepository),
                RemoveBookmark(bookmarkRepository),
                GetBooksUseCase(bookRepository, Dispatchers.IO)
            )
        )
    }

//    @Test
//    fun addNewTask_setsNewTaskEvent() {
//        // When adding a new task
//        tasksViewModel.addNewTask()
//
//        // Then the new task event is triggered
//        val value = tasksViewModel.newTaskEvent.getOrAwaitValue()
//
//        MatcherAssert.assertThat(
//            value.getContentIfNotHandled(),
//            CoreMatchers.not(CoreMatchers.nullValue())
//        )
//    }
}