package com.focus617.bookreader.framework.database.book

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.focus617.bookreader.framework.database.BookReaderDatabase
import kotlinx.coroutines.runBlocking
import org.hamcrest.Matchers
import org.junit.*
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BookDaoTest {
    private lateinit var database: BookReaderDatabase
    private lateinit var bookDao: BookDao

    private val bookA = BookEntity(
        "A", "1", "张三", 100, "a", "b", "c"
    )
    private val bookB = BookEntity(
        "B", "2", "李四", 200, "h", "i", "j"
    )
    private val bookC = BookEntity(
        "C", "3", "王五", 300, "o", "p", "q"
    )

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Before
    fun createDb() = runBlocking {
        val context = InstrumentationRegistry.getInstrumentation().targetContext
        database = Room.inMemoryDatabaseBuilder(context, BookReaderDatabase::class.java).build()
        bookDao = database.bookDao

        // Insert plants in non-alphabetical order to test that results are sorted by name
        bookDao.insertAll(bookB, bookC, bookA)
    }

    @After
    fun closeDb() {
        database.close()
    }

    @Test
    fun testGetBook() = runBlocking {
        Assert.assertThat(bookDao.get(bookA.uri), Matchers.equalTo(bookA))
    }

    @Test
    fun testGetBooks() = runBlocking {
        val bookList = bookDao.getAll()
        Assert.assertThat(bookList.size, Matchers.equalTo(3))

        // Ensure book list is sorted by uri
        Assert.assertThat(bookList[0], Matchers.equalTo(bookC))
        Assert.assertThat(bookList[1], Matchers.equalTo(bookB))
        Assert.assertThat(bookList[2], Matchers.equalTo(bookA))
    }

    @Test
    fun testRemove() = runBlocking {
        bookDao.remove(bookB)
        val bookList = bookDao.getAll()
        Assert.assertThat(bookList.size, Matchers.equalTo(2))
        Assert.assertThat(bookList[0], Matchers.equalTo(bookC))
        Assert.assertThat(bookList[1], Matchers.equalTo(bookA))
    }

    @Test
    fun testClear() = runBlocking {
        bookDao.clear()
        val bookList = bookDao.getAll()
        Assert.assertThat(bookList.size, Matchers.equalTo(0))
    }

    @Test
    fun testUpdate() = runBlocking {
        val newBookC = BookEntity(
            "C", "4", "赵六", 400, "u", "v", "w"
        )
        bookDao.update(newBookC)
        val bookList = bookDao.getAll()
        Assert.assertThat(bookList.size, Matchers.equalTo(3))
        Assert.assertThat(bookList[0], Matchers.equalTo(newBookC))
    }
}