/*
 * Copyright (C) 2020 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.focus617.bookreader.di

import android.content.Context
import androidx.room.Room
import com.focus617.bookreader.framework.database.BookReaderDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    private const val DATABASE_NAME = "reader.db"

    @Volatile
    private var instance: BookReaderDatabase? = null

    private fun create(context: Context): BookReaderDatabase =
        Room.databaseBuilder(
            context.applicationContext,
            BookReaderDatabase::class.java,
            DATABASE_NAME
        )
            .fallbackToDestructiveMigration()
            .build()

    @Provides
    @Singleton
    fun provideDatabase(@ApplicationContext context: Context): BookReaderDatabase =
        synchronized(this) {
            (instance ?: create(context)).also { instance = it }
        }

}
