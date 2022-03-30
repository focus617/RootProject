package com.focus617.bookreader.framework.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.focus617.bookreader.framework.database.bookmark.BookmarkDao
import com.focus617.bookreader.framework.database.bookmark.BookmarkEntity
import com.focus617.bookreader.framework.database.book.BookDao
import com.focus617.bookreader.framework.database.book.BookEntity
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Database(
    entities = [BookEntity::class, BookmarkEntity::class],
    version = 1,
    exportSchema = false
)

abstract class BookReaderDatabase : RoomDatabase() {

    abstract val bookDao: BookDao
    abstract val bookmarkDao: BookmarkDao

//    companion object {
//
//        private const val DATABASE_NAME = "reader.db"
//
//        @Volatile
//        private var instance: BookReaderDatabase? = null
//
//        private fun create(context: Context): BookReaderDatabase =
//            Room.databaseBuilder(
//                context.applicationContext,
//                BookReaderDatabase::class.java,
//                DATABASE_NAME
//            )
//                .fallbackToDestructiveMigration()
//                .build()
//
//        fun getInstance(context: Context): BookReaderDatabase =
//            synchronized(this) {
//                (instance ?: create(context)).also { instance = it }
//            }
//    }
}