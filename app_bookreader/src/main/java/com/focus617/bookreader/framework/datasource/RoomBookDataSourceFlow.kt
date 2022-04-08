package com.focus617.bookreader.framework.datasource

import android.content.Context
import com.focus617.bookreader.di.DatabaseModule
import com.focus617.bookreader.framework.database.book.asDomainModel
import com.focus617.core.data.dataSourceInterface.IfBookDataSourceFlow
import com.focus617.core.domain.Book
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.mapLatest
import javax.inject.Inject

@OptIn(ExperimentalCoroutinesApi::class)
class RoomBookDataSourceFlow @Inject constructor(
    @ApplicationContext val context: Context
) : IfBookDataSourceFlow {

    var database = DatabaseModule.provideDatabase(context)
    private val bookDao = database.bookDao

    override suspend fun getAllByFlow(): Flow<List<Book>> =
        bookDao.getAllByFlow().mapLatest { it.asDomainModel() }

}