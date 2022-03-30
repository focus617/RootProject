package com.focus617.bookreader.di

import com.focus617.bookreader.framework.datasource.InMemoryOpenBookDataSource
import com.focus617.bookreader.framework.datasource.RoomBookDataSource
import com.focus617.bookreader.framework.datasource.RoomBookmarkDataSource
import com.focus617.core.data.BookRepository
import com.focus617.core.data.BookmarkRepository
import com.focus617.core.data.dataSourceInterface.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module
@InstallIn(SingletonComponent::class)
interface UseCaseModule {
    @Binds
    fun bookDataSource(dataSource: RoomBookDataSource): IfBookDataSource

    @Binds
    fun bookmarkDataSource(dataSource: RoomBookmarkDataSource): IfBookmarkDataSource

    @Binds
    fun openBookDataSource(dataSource: InMemoryOpenBookDataSource): IfOpenBookDataSource

    @Binds
    fun bookRepository(repository: BookRepository): IfBookRepository

    @Binds
    fun bookmarkRepository(repository: BookmarkRepository): IfBookmarkRepository

}
