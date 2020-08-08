package com.example.pomodoro2.features.projects.presentation

import android.widget.ImageView
import android.widget.TextView
import androidx.databinding.BindingAdapter
import com.example.pomodoro2.features.infra.database.Project

object BindingUtils {
    @BindingAdapter("projectTitle")
    fun setProjectTitle(view: TextView, item: Project) {
        view.setText(item.title)
    }

    @BindingAdapter("projectImage")
    fun setProjectImage(view: ImageView, item: Project) {
        view.setImageResource(item.imageId)
    }
}