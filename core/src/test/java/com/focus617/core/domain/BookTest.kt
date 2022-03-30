package com.focus617.core.domain

import org.junit.Assert
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test

class BookTest {
    lateinit var book: Book

    @Before
    fun setUp() {
        book = Book.ExampleBook
    }

    @Test
    fun book_titleIsSet(){
        assertEquals("Book's title not equal", book.title, "星门")
    }
}