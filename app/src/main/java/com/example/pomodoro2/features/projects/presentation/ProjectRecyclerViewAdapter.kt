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
import com.example.pomodoro2.databinding.ListItemProjectBinding
import com.example.pomodoro2.features.infra.database.Project

class ProjectRecyclerViewAdapter(val clickListener: ProjectListener) :
    ListAdapter<Project, ProjectRecyclerViewAdapter.ViewHolder>(ProjectDiffCallback()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position)!!, clickListener)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(val binding: ListItemProjectBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: Project, clickListener: ProjectListener) {
            binding.project = item
            binding.clickListener = clickListener
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)
                val binding = ListItemProjectBinding.inflate(layoutInflater, parent, false)
                return ViewHolder(binding)
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

class ProjectListener(val clickListener: (projectId: Long) -> Unit) {
    fun onClick(project: Project) = clickListener(project.id)
}