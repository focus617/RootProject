package com.focus617.bookreader.ui.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.focus617.bookreader.R
import com.focus617.bookreader.databinding.ListItemBookBinding
import com.focus617.core.domain.Book
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

private val ITEM_VIEW_TYPE_HEADER = 0
private val ITEM_VIEW_TYPE_ITEM = 1

class BookListAdapter (val clickListener: BookListener):
    ListAdapter<DataItem, RecyclerView.ViewHolder>(BookListDiffCallback()) {

    private val adapterScope = CoroutineScope(Dispatchers.Default)

    // convert a List<Book> to a List<DataItem>
    fun addHeaderAndSubmitList(list: List<Book>?) {
        adapterScope.launch {
            val items = when (list) {
                null -> listOf(DataItem.Header)
                else -> listOf(DataItem.Header) + list.map { DataItem.BookItem(it) }
            }
            withContext(Dispatchers.Main) {
                submitList(items)
            }
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ItemViewHolder -> {
                val book = getItem(position) as DataItem.BookItem
                holder.bind(book.book, clickListener)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ItemViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.BookItem -> ITEM_VIEW_TYPE_ITEM
        }
    }

    class ItemViewHolder private constructor(private val binding: ListItemBookBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Book, clickListener: BookListener) {
            binding.book = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ItemViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemBookBinding.inflate(layoutInflater, parent, false)
                return ItemViewHolder(binding)
            }
        }
    }

    class HeaderViewHolder(view: View): RecyclerView.ViewHolder(view) {
        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater.inflate(R.layout.list_header, parent, false)
                return HeaderViewHolder(view)
            }
        }
    }

}

class BookListDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.url == newItem.url
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem == newItem
    }
}

class BookListener(val clickListener: (book: Book) -> Unit) {
    fun onClick(book: Book) = clickListener(book)
}

sealed class DataItem {
    abstract val url: String

    data class BookItem(val book: Book): DataItem() {
        override val url = book.url
    }

    object Header: DataItem() {
        override val url = Long.MIN_VALUE.toString()
    }
}