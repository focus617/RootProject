package com.focus617.bookreader.ui.gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.focus617.bookreader.databinding.ListItemNoteBinding
import com.focus617.core.domain.Note
import timber.log.Timber
import java.util.*

class NoteListAdapter(val clickListener: NoteListener) :
    ListAdapter<Note, NoteListAdapter.ViewHolder>(NoteListDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(private val binding: ListItemNoteBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Note, clickListener: NoteListener) {
            binding.note = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemNoteBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
            }
        }
    }

}

class NoteListDiffCallback : DiffUtil.ItemCallback<Note>() {
    override fun areItemsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Note, newItem: Note): Boolean {
        return oldItem == newItem
    }
}

class NoteListener(val clickListener: (note: Note) -> Unit) {
    fun onClick(note: Note) = clickListener(note)
}