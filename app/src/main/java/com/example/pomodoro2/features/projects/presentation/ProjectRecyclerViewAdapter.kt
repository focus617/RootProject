package com.example.pomodoro2.features.projects.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.pomodoro2.R
import com.example.pomodoro2.features.infra.database.Project

class ProjectRecyclerViewAdapter :
    ListAdapter<Project, ProjectRecyclerViewAdapter.ViewHolder>(ProjectDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val itemView: View = itemView
        val itemTitle: TextView = itemView.findViewById(R.id.item_title)
        val itemImage: ImageView = itemView.findViewById(R.id.item_image)

        fun bind(item: Project) {
            itemTitle.text = item.title
            itemImage.setImageResource(item.imageId)
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val view = layoutInflater
                    .inflate(R.layout.list_item_project, parent, false)

                return ViewHolder(view)
            }
        }
    }
}

class ProjectDiffCallback : DiffUtil.ItemCallback<Project>() {

    override fun areItemsTheSame(oldItem: Project, newItem: Project): Boolean {
        return oldItem.id == newItem.id
    }


    override fun areContentsTheSame(oldItem: Project, newItem: Project): Boolean {
        return oldItem == newItem
    }
}