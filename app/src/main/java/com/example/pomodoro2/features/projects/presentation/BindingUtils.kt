package com.example.pomodoro2.features.projects.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.pomodoro2.features.infra.database.Project


@BindingAdapter("projectTitle")
fun TextView.setProjectTitle(item: Project) {
    text = item.title
}

@BindingAdapter("projectImage")
fun ImageView.setProjectImage(item: Project) {
    setImageResource(item.imageId)
}
