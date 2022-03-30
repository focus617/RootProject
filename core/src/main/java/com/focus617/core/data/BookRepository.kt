package com.focus617.core.data

import com.focus617.core.coroutine.IoDispatcher
import com.focus617.core.data.dataSourceInterface.IfBookDataSource
import com.focus617.core.data.dataSourceInterface.IfBookRepository
import com.focus617.core.data.dataSourceInterface.IfOpenBookDataSource
import com.focus617.core.domain.Book
import com.focus617.core.platform.base.BaseSpecification
import com.focus617.core.platform.functional.Result
import com.focus617.core.platform.functional.Result.Error
import com.focus617.core.platform.functional.Result.Success
import kotlinx.coroutines.*
import java.util.concurrent.ConcurrentHashMap
import javax.inject.Inject


class BookRepository @Inject constructor(
    private val bookDataSource: IfBookDataSource,
    private val openBookDataSource: IfOpenBookDataSource,
    @IoDispatcher var ioDispatcher: CoroutineDispatcher
) : IfBookRepository {
    // Domain Aggregate Root data in memory
    var cachedBooks: ConcurrentHashMap<String, Book> = ConcurrentHashMap()

    override suspend fun addBook(book: Book) {
        cachedBooks[book.url] = book

        coroutineScope {
            launch { bookDataSource.add(book) }
        }
    }

    override suspend fun getBooks(): Result<List<Book>> {
        return withContext(ioDispatcher) {
            cachedBooks.clear()

            val retrievedBooks = bookDataSource.readAll()
            (retrievedBooks as? Success)?.let { success ->
                if (success.data.isEmpty()) {
                    return@withContext Success(success.data)
                } else{
                    refreshBookCache(success.data)
                    return@withContext Success(success.data.sortedBy { it.title })
                }
            }
            return@withContext Error(Exception("Illegal state"))
        }
    }

    override suspend fun removeBook(book: Book) {
        cachedBooks.remove(book.url)

        coroutineScope {
            launch { bookDataSource.remove(book) }
        }
    }

    override suspend fun clearBooks() {
        cachedBooks.clear()

        coroutineScope {
            launch { bookDataSource.clear() }
        }
    }


    fun refreshBookCache(list: List<Book>) {
        cachedBooks.clear()

        for (book in list)
            cachedBooks[book.url] = book
    }

    /**
     * 使用 Specification 模式对领域对象进行查询或校验，可以帮助分离领域层的逻辑与规则校验的逻辑
     * 我们在Specifications里面定义更加复杂的查询条件
     * 此处举例：基于url批量查询
     * @param specification Specification or its composite
     * @return List<Book>
     */
    fun selectBy(specification: BaseSpecification<Book>?): List<Book> {
        // 如果没有过滤条件，直接返回cache
        if (specification == null) return cachedBooks.values.sortedBy { it.url }

        val foundBooks = arrayListOf<Book>()

        // 遍历每个Book
        val books = cachedBooks.iterator()
        while (books.hasNext()) {
            val book = books.next().value

            // 如果满足全部Specification
            if (specification.isSatisfiedBy(book))
                foundBooks.add(book)
        }
        return foundBooks.sortedBy { it.url }
    }


    override fun setOpenBook(book: Book) = openBookDataSource.setOpenBook(book)

    override fun getOpenBook() = openBookDataSource.getOpenBook()
}