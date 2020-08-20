package com.example.pomodoro2.features.tasks.presentation

import android.graphics.Paint
import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.pomodoro2.domain.model.Task


@BindingAdapter("app:completedTask")
fun TextView.setStyle(item: Task?) {
    item?.let {
        paintFlags = if (item.isCompleted)  {
            paintFlags or Paint.STRIKE_THRU_TEXT_FLAG
        } else {
            paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv()
        }
    }
}

@BindingAdapter("taskImage")
fun ImageView.setTaskImage(item: Task?) {
    item?.let {
        setImageResource(item.imageId)
    }
}
