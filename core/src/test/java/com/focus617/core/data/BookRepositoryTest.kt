package com.focus617.core.data

import com.focus617.core.data.dataSourceInterface.IfBookDataSource
import com.focus617.core.data.dataSourceInterface.IfOpenBookDataSource
import com.focus617.core.domain.Book
import com.focus617.core.domain.OfIdBookSpec
import com.focus617.core.platform.base.AndSpecification
import com.focus617.core.platform.base.NotSpecification
import com.focus617.core.platform.base.OrSpecification
import com.focus617.core.platform.functional.Result
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class BookRepositoryTest {
    // Class under test
    private lateinit var bookRepository: BookRepository

    private lateinit var bookDataSource: IfBookDataSource
    private lateinit var openBookDataSource: IfOpenBookDataSource

    private val book1 = Book(
        "Book_first", "", "", 0, "", "", ""
    )
    private val book2 = Book(
        "Book_second", "", "", 0, "", "", ""
    )
    private val newBook = Book(
        "Book_new", "", "", 0, "", "", ""
    )

    private val initialBookList = listOf(book1, book2).sortedBy { it.url }

    @Before
    fun createRepository() {
        bookDataSource = FakeBookDataSource(initialBookList.toMutableList())
        openBookDataSource = FakeInMemoryOpenBookDataSource()

        // Get a reference to the class under test
        bookRepository = BookRepository(bookDataSource, openBookDataSource, Dispatchers.Unconfined)
    }

    @After
    fun tearDown() {
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getBooks_emptyBookDataSource_returnSuccess() = runBlocking {
        // Given book list is empty
        val emptySource = FakeBookDataSource()
        val repositoryWithEmptyBook = BookRepository(
            emptySource, openBookDataSource, Dispatchers.Unconfined
        )

        // When books are requested from the books repository
        val result = repositoryWithEmptyBook.getBooks()

        // Then getBooks should return Success
        assertThat(result is Result.Success).isTrue()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getBooks_emptyBookDataSource_returnEmptyBookList() = runBlocking {
        // Given book list is empty
        val emptySource = FakeBookDataSource()
        val repositoryWithEmptyBook = BookRepository(
            emptySource, openBookDataSource, Dispatchers.Unconfined
        )

        // When books are requested from the books repository
        val result = repositoryWithEmptyBook.getBooks() as Result.Success

        // Then getBooks should return Success with empty list
        assertThat(result.data.isEmpty()).isTrue()
    }

    @ExperimentalCoroutinesApi
    @Test
    fun getBooks_requestsAllBooksFromDataSource_returnRightBookList() = runBlocking {
        // When books are requested from the books repository
        val books = bookRepository.getBooks() as Result.Success

        // Then books are loaded from the remote data source
        assertThat(books.data).isEqualTo(initialBookList)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun addBook_InsertNewBook_NewItemInBookList() = runBlocking {
        // Make sure newBook is not in the remote data sources
        assertThat((bookDataSource as FakeBookDataSource).books).doesNotContain(newBook)
        assertThat((bookRepository.getBooks() as? Result.Success)?.data).doesNotContain(newBook)

        // When a book is saved to the book repository
        bookRepository.addBook(newBook)

        // Then books are loaded from the remote data source
        assertThat((bookDataSource as FakeBookDataSource).books).contains(newBook)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun addBook_InsertNewBook_WithCorrectReturnBookList() = runBlocking {
        // Make sure newBook is not in the remote data sources
        assertThat((bookDataSource as FakeBookDataSource).books).doesNotContain(newBook)
        assertThat((bookRepository.getBooks() as? Result.Success)?.data).doesNotContain(newBook)

        // When a book is saved to the book repository
        bookRepository.addBook(newBook)

        // When books are requested from the books repository
        val result = bookRepository.getBooks()

        // Then getBooks should return Success
        assertThat(result is Result.Success).isTrue()

        // When books are requested from the books repository
        assertThat((bookDataSource as FakeBookDataSource).books).contains(newBook)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun removeBook_BookInList_WithCorrectReturnBookList() = runBlocking {
        // Make sure Book1 is in the remote data sources
        assertThat((bookRepository.getBooks() as? Result.Success)?.data).contains(book1)

        // When a book1 is request to remove from the book repository
        bookRepository.removeBook(book1)

        // Then book list of repository should not contain book1
        assertThat((bookDataSource as FakeBookDataSource).books).doesNotContain(book1)
        assertThat((bookRepository.getBooks() as? Result.Success)?.data).doesNotContain(book1)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun removeBook_BookNotInList_WithCorrectReturnBookList() = runBlocking {
        // Make sure Book1 and Book2 is in the remote data sources
        val originBooks = (bookDataSource as FakeBookDataSource).books
        assertThat(originBooks).contains(book1)
        assertThat(originBooks).contains(book2)
        assertThat(originBooks).doesNotContain(newBook)

        // When a book1 is request to remove from the book repository
        bookRepository.removeBook(newBook)

        // Then book list of repository should not contain book1
        val currentBooks = (bookDataSource as FakeBookDataSource).books
        assertThat(currentBooks).contains(book1)
        assertThat(currentBooks).contains(book2)
        assertThat(currentBooks).doesNotContain(newBook)
    }

    @ExperimentalCoroutinesApi
    @Test
    fun clearBooks_ResultWithEmptyBookList() = runBlocking {
        // Given current book list is not empty
        assertThat((bookRepository.getBooks() as? Result.Success)?.data?.isNotEmpty()).isTrue()

        // When clearBooks are requested from the books repository
        bookRepository.clearBooks()

        // Then getBooks should return Success with empty list
        val result = bookRepository.getBooks() as Result.Success
        assertTrue(result.data.isEmpty())
    }

    @Test
    fun `refreshBookCache_list with correct item`() {
        //Given initialBookList contains book1 and book2
        //When cache initialBookList
        bookRepository.refreshBookCache(initialBookList)

        //Then
        assertThat(bookRepository.cachedBooks.size).isEqualTo(2)
        assertEquals(book1, bookRepository.cachedBooks[book1.url])
        assertEquals(book2, bookRepository.cachedBooks[book2.url])
    }

    @Test
    fun `selectBy_OfIdBookSpec return correct item`(){
        //Given initialBookList contains book1 and book2
        bookRepository.refreshBookCache(initialBookList)

        //When select by OfIdBookSpec
        val spec = OfIdBookSpec(book1.url)
        val result = bookRepository.selectBy(spec)

        //Then
        assertThat(result.size).isEqualTo(1)
        assertEquals(book1, result[0])
    }

    @Test
    fun `selectBy_OrSpecification return correct items`(){
        //Given initialBookList contains book1 and book2
        bookRepository.refreshBookCache(initialBookList)

        //When select by OfIdBookSpec
        val spec1 = OfIdBookSpec(book1.url)
        val spec2 = OfIdBookSpec(book2.url)
        val orSpecs = OrSpecification<Book>()
        orSpecs.add(spec1)
        orSpecs.add(spec2)
        val result = bookRepository.selectBy(orSpecs)

        //Then
        assertThat(result.size).isEqualTo(2)
        assertThat(result).contains(book1)
        assertThat(result).contains(book2)
    }

    @Test
    fun `selectBy_AndSpecification return correct items`(){
        //Given initialBookList contains book1 and book2
        bookRepository.refreshBookCache(initialBookList)

        //When select by OfIdBookSpec
        val spec1 = OfIdBookSpec(book1.url)
        val spec2 = OfIdBookSpec(book2.url)
        val andSpecs = AndSpecification<Book>()
        andSpecs.add(spec1)
        andSpecs.add(spec2)
        val result = bookRepository.selectBy(andSpecs)

        //Then
        assertThat(result.size).isEqualTo(0)
    }

    @Test
    fun `selectBy_NotSpecification return correct item`(){
        //Given initialBookList contains book1 and book2
        bookRepository.refreshBookCache(initialBookList)

        //When select by OfIdBookSpec
        val spec = OfIdBookSpec(book1.url)
        val notSpec = NotSpecification(spec)
        val result = bookRepository.selectBy(notSpec)

        //Then
        assertThat(result.size).isEqualTo(1)
        assertEquals(book2, result[0])
    }
}