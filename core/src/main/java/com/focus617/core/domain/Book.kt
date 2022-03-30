package com.focus617.core.domain

import com.focus617.core.platform.base.BaseEntity
import java.io.Serializable

data class Book(
    val url: String,
    val title: String,
    val author: String,
    val size: Int,
    val thumbnail: String,
    val description: String,
    val imgSrcUrl: String
): BaseEntity(), Serializable {
    companion object {
        val EMPTY = Book(
            "", "", "",0, "", "","")

        val ExampleBook = Book(
            url = "example",
            title = "星门",
            author = "老鹰吃小鸡",
            size= 10000,
            thumbnail = "传说，在那古老的星空深处，伫立着一道血与火侵染的红色之门。",
            description = "传说，在那古老的星空深处，伫立着一道血与火侵染的红色之门。传奇与神话，黑暗与光明，无尽传说皆在这古老的门户中流淌。俯瞰星门，热血照耀天地，黑暗终将离去！",
            imgSrcUrl = "http://192.168.5.5/image/book_cover_image.jpg"
        )
    }
}