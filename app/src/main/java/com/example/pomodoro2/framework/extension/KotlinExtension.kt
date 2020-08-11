package com.example.pomodoro2.framework.extension

import androidx.databinding.ObservableField

fun ObservableField<String>.getOrEmpty(): String = this.get() ?: ""