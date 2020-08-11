package com.example.pomodoro2.features.projects.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.pomodoro2.domain.Project


@BindingAdapter("projectTitle")
fun TextView.setProjectTitle(item: Project?) {
    item?.let {
        text = item.title
    }
}

@BindingAdapter("projectImage")
fun ImageView.setProjectImage(item: Project?) {
    item?.let {
        setImageResource(item.imageId)
    }
}
