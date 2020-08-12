package com.example.pomodoro2.features.tasks.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.pomodoro2.domain.Task


@BindingAdapter("taskTitle")
fun TextView.setTaskTitle(item: Task?) {
    item?.let {
        text = item.title
    }
}

@BindingAdapter("taskImage")
fun ImageView.setTaskImage(item: Task?) {
    item?.let {
        setImageResource(item.imageId)
    }
}
