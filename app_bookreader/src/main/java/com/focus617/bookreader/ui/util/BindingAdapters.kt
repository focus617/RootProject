package com.focus617.bookreader.ui.util

import android.os.Build
import android.text.Html
import android.widget.ImageView
import android.widget.TextView
import androidx.core.net.toUri
import androidx.core.text.HtmlCompat
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.focus617.bookreader.R
import com.focus617.core.domain.Book
import timber.log.Timber

@BindingAdapter("setBookTitleFormatted")
fun TextView.setBookTitleFormatted(book: Book?) {
    book?.let {
        text = String.format("《%s》", book.title)
    }
}

@BindingAdapter("setBookAuthorFormatted")
fun TextView.setBookAuthorFormatted(book: Book?) {
    book?.let {
        text = String.format("Author: %s", book.author)
    }
}

@BindingAdapter("setBookDetailFormatted")
fun TextView.setBookDetailFormatted(book: Book?) {
    book?.let {
        text = StringBuilder().apply {
            append("作者：\t${book.author}\n")
            append("内容简介：\t${book.thumbnail}\n")
            toString()
        }
    }
}

/**
 * Uses the Glide library to load an image by URL into an [ImageView]
 */
@BindingAdapter("imageUrl")
fun bindImage(imgView: ImageView, imgUrl: String?) {
    imgUrl?.let {
        val imgUri = imgUrl.toUri().buildUpon().scheme("http").build()
        Timber.d("bindImage(): imgUri=$imgUri")

        Glide.with(imgView.context)
            .load(imgUri)
            .apply(
                RequestOptions()
                    .placeholder(R.drawable.loading_animation)
                    .error(R.drawable.ic_broken_image)
            )
            .into(imgView)
    }
}