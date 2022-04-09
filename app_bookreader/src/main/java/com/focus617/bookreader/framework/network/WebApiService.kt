/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.focus617.bookreader.framework.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import kotlinx.coroutines.Deferred
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query
import javax.inject.Singleton

private const val BASE_URL = "http://192.168.5.10/"

enum class BooksApiFilter(val value: String) {
    SHOW_RENT("rent"),
    SHOW_BUY("buy"),
    SHOW_ALL("all")
}

/**
 * Build the Moshi object that Retrofit will be using, making sure to add the Kotlin adapter for
 * full Kotlin compatibility.
 */
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()


/**
 * A public retrofit interface that exposes the [loadBooksAsync] method
 */
interface WebApiService {
    /**
     * Returns a Coroutine [List] of [NetworkBook] which can be fetched with await() if in a Coroutine scope.
     * The @GET annotation indicates that the "books" endpoint will be requested with the GET
     * HTTP method
     */
    @GET("test.json")
    suspend fun loadBooksAsync(): Deferred<NetworkBookContainer>

    // Using for test Flow
    suspend fun loadBooks(@Query("filter") type: String): NetworkBookContainer
}

/**
 * A public Api object that exposes the lazy-initialized Retrofit service
 */

@Module
@InstallIn(SingletonComponent::class)
object BooksWebApiModule {

    // Configure retrofit to parse JSON and use coroutines
    @Provides
    @Singleton
    fun provideRetrofit() : Retrofit{
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .baseUrl(BASE_URL)
            .build()
    }

    @Provides
    @Singleton
    fun provideWebApiService(retrofit: Retrofit): Lazy<WebApiService> =
        lazy { retrofit.create(WebApiService::class.java) }
}
