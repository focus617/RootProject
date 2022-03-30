package com.focus617.core.domain

import com.focus617.core.domain.Book
import com.focus617.core.platform.base.BaseSpecification

class OfIdBookSpec(private val url: String): BaseSpecification<Book>() {
    override fun isSatisfiedBy(t: Book) = (t.url == url)
}

