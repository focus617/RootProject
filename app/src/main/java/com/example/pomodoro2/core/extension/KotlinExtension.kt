package com.example.pomodoro2.core.extension

import androidx.databinding.ObservableField

fun ObservableField<String>.getOrEmpty(): String = this.get() ?: ""