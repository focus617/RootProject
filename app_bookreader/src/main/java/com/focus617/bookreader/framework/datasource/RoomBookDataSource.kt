package com.focus617.bookreader.framework.datasource

import android.content.Context
import com.focus617.bookreader.di.DatabaseModule
import com.focus617.bookreader.framework.database.book.BookEntity
import com.focus617.bookreader.framework.database.book.asDomainModel
import com.focus617.platform.helper.FileUtil
import com.focus617.core.coroutine.IoDispatcher
import com.focus617.core.data.dataSourceInterface.IfBookDataSource
import com.focus617.core.domain.Book
import com.focus617.core.platform.functional.Result
import com.focus617.core.platform.functional.Result.Error
import com.focus617.core.platform.functional.Result.Success
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class RoomBookDataSource @Inject constructor(
    @ApplicationContext val context: Context,
    @IoDispatcher var ioDispatcher: CoroutineDispatcher
) : IfBookDataSource {

    //    private val bookDao = BookReaderDatabase.getInstance(context).bookDao
    var database = DatabaseModule.provideDatabase(context)
    private val bookDao = database.bookDao

    override suspend fun add(book: Book) {
        val details = FileUtil.getDocumentDetails(context, book.url)

        return bookDao.insert(
            BookEntity(
                book.url, book.title, book.author,
                details.size, details.thumbnail, details.thumbnail, book.imgSrcUrl
            )
        )
    }

    override suspend fun readAll(): Result<List<Book>> = withContext(ioDispatcher) {
        return@withContext try {
            Success(bookDao.getAll().asDomainModel())
        } catch (e: Exception) {
            Error(e)
        }
    }

    override suspend fun remove(book: Book) =
        bookDao.remove(BookEntity.from(book))

    override suspend fun clear() {
        bookDao.clear()
    }
}