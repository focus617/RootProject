package com.focus617.bookreader.framework.network

import com.focus617.bookreader.framework.database.book.BookEntity
import com.focus617.core.domain.Book
import com.squareup.moshi.JsonClass


/**
 * DataTransferObjects go in this file. These are responsible for parsing responses from the server
 * or formatting objects to send to the server. You should convert these to domain objects before
 * using them.
 */

/**
 * BookHolder holds a list of Books.
 *
 * This is to parse first level of our network result which looks like
 *
 * {
 *   "books": []
 * }
 */
@JsonClass(generateAdapter = true)
data class NetworkBookContainer(val books: List<NetworkBook>)

@JsonClass(generateAdapter = true)
data class NetworkBook(
    var url: String,
    val title: String,
    val author: String,
    val size: Int,
    val thumbnail: String,
    val description: String,
    val imgSrcUrl: String
)

/**
 * Convert Network results to domain objects
 */
fun NetworkBookContainer.asDomainModel(): List<Book> {
    return books.map {
        Book(
            url = it.url,
            title = it.title,
            author = it.author,
            size= it.size,
            thumbnail = it.thumbnail,
            description = it.description,
            imgSrcUrl = it.imgSrcUrl
        )
    }
}

/**
 * Convert Network results to database objects
 */
fun NetworkBookContainer.asDatabaseModel(): Array<BookEntity> {
    return books.map {
        BookEntity(
            uri = it.url,
            title = it.title,
            author = it.author,
            size = it.size,
            thumbnailUri = it.thumbnail,
            description = it.description,
            imgSrcUrl = it.imgSrcUrl
        )
    }.toTypedArray()
}