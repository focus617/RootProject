package com.example.pomodoro2.features.projects.presentation

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.pomodoro2.R
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