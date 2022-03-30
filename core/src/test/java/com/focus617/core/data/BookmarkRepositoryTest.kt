package com.focus617.core.data

import com.focus617.core.data.dataSourceInterface.IfBookmarkDataSource
import com.focus617.core.domain.Book
import com.focus617.core.domain.Bookmark
import com.google.common.truth.Truth.assertThat
import io.mockk.*
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class BookmarkRepositoryTest {
    companion object {
        val FAKE_BOOK = Book(
            "test", "FakeBook", "", 0, "", "", ""
        )
        val FAKE_BOOKMARK1 = Bookmark(id = 1, page = 10)
        val FAKE_BOOKMARK2 = Bookmark(id = 2, page = 100)
        val fakeBookmarks = listOf<Bookmark>(FAKE_BOOKMARK1, FAKE_BOOKMARK2)
    }

    var dataSource: IfBookmarkDataSource = mockk() {
        coEvery { read(FAKE_BOOK) } returns fakeBookmarks
        coEvery { add(FAKE_BOOK, FAKE_BOOKMARK1) } just Runs
        coEvery { remove(FAKE_BOOK, FAKE_BOOKMARK1) } just Runs
    }

    lateinit var repository: BookmarkRepository

    @Before
    fun setup(){
        repository = BookmarkRepository(dataSource)
    }

    @Test
    fun addBookmark() = runBlocking {

        repository.addBookmark(FAKE_BOOK, FAKE_BOOKMARK1)

        coVerify(exactly = 1){ dataSource.add(FAKE_BOOK, FAKE_BOOKMARK1)}
    }

    @Test
    fun `getBookmarks_it should fetch value for current bookmarks`() = runBlocking {

        val bookmarks = repository.getBookmarks(FAKE_BOOK)

        coVerify(exactly = 1){ dataSource.read(FAKE_BOOK)}

        assertThat(bookmarks.size).isEqualTo(2)
        assertThat(bookmarks).contains(FAKE_BOOKMARK1)
        assertThat(bookmarks).contains(FAKE_BOOKMARK2)
    }

    @Test
    fun removeBookmark() = runBlocking {

        repository.removeBookmark(FAKE_BOOK, FAKE_BOOKMARK1)

        coVerify(exactly = 1){ dataSource.remove(FAKE_BOOK, FAKE_BOOKMARK1)}
    }
}