package com.example.pomodoro2.features.projects.presentation

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.pomodoro2.R
import com.example.pomodoro2.databinding.ListItemProjectBinding
import com.example.pomodoro2.features.infra.database.Project

class ProjectRecyclerViewAdapter : RecyclerView.Adapter<ProjectRecyclerViewAdapter.ViewHolder>() {

    var data = listOf<Project>()
        set(value) {
            field = value
            notifyDataSetChanged()
        }

    override fun getItemCount() = data.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = data[position]
        holder.bind(item)
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder.from(parent)
    }

    class ViewHolder private constructor(binding: ListItemProjectBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val binding: ListItemProjectBinding = binding

        fun bind(item: Project) {
            binding.project = item
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): ViewHolder {
                val layoutInflater = LayoutInflater.from(parent.context)

                return ViewHolder(
                    ListItemProjectBinding.inflate(
                        layoutInflater, parent, false
                    )
                )
            }
        }
    }
}