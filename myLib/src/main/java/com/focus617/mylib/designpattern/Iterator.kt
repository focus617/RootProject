package com.focus617.mylib.designpattern

import com.focus617.mylib.logging.WithLogging

interface Iterator<out T> {
    operator fun hasNext(): Boolean
    operator fun next(): T
}

interface Aggregate<out T> {
    operator fun iterator(): Iterator<T>
}

/** ConcreteIterator */
class BookshelfIterator(val shelf: Bookshelf) : Iterator<Book> {
    private var index: Int = 0

    override fun hasNext(): Boolean {
        return if (index < shelf.length() - 1) {
            index++
            true
        } else {
            false
        }
    }

    override fun next(): Book {
        return shelf.get(index)
    }
}

/** ConcreteAggregate */
class Bookshelf : Aggregate<Book> {
    private val books = mutableListOf<Book>()

    fun append(book: Book) {
        books.add(book)
    }

    fun remove(index: Int) {
        books.removeAt(index)
    }

    fun get(index: Int): Book = books[index]

    fun length(): Int = books.size

    override fun iterator(): Iterator<Book> = BookshelfIterator(this)

}

data class Book(var title: String = "标题", val price: Int = 10)

/**
 * @description Client 客户端
 */
class ClientIterator {
    companion object : WithLogging() {
        @JvmStatic
        fun main(args: Array<String>) {
            val bookshelf = Bookshelf()
            for (i in 0..10) {
                bookshelf.append(Book("title$i", i))
            }

            for (book in bookshelf) {
                println("${book.title}======${book.price}")
            }

        }
    }
}