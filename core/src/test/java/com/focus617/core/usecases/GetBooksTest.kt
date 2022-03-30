package com.focus617.core.usecases

import com.focus617.core.domain.Book
import com.focus617.core.platform.functional.Result
import com.focus617.core.platform.functional.succeeded
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Unit tests for [GetBooks] UseCase.
 */
@ExperimentalCoroutinesApi
class GetBooksTest {

    private val fakeBookRepository = FakeBookRepository()

    private lateinit var useCase: GetBooks

    @Before
    fun createUseCase() {
        // Get a reference to the class under test
        useCase = GetBooks(fakeBookRepository)
    }

    @Test
    fun loadBooks_initState_IsEmpty() = runBlocking {
        // Given an empty repository

        // When calling the use case
        val result = useCase()

        // Verify the result is a success and empty
        assertTrue(result is Result.Success)
        assertTrue((result as Result.Success).data.isEmpty())
    }

    @Test
    fun loadBooks_error() = runBlocking {
        // Make the repository return errors
        fakeBookRepository.setReturnError(true)

        // Load tasks
        val result = useCase()

        // Verify the result is an error
        assertTrue(result is Result.Error)
    }

    @Test
    fun loadTasks_normalCase() = runBlocking {
        // Given a repository with 3 books:
        fakeBookRepository.addBooks(
            Book("Book_first", "", "", 0, "", "", ""),
            Book("Book_second", "", "", 0, "", "", ""),
            Book("Book_third", "", "", 0, "", "", "")
        )

        // When load tasks
        val result = useCase()

        // Then verify the result is filtered correctly
        assertTrue(result is Result.Success)
        assertTrue(result.succeeded)
        assertEquals((result as Result.Success).data.size, 3)
    }
}