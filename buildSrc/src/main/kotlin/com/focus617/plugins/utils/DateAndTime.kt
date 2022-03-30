package com.example.pomodoro2.plugins.utils

import java.text.SimpleDateFormat
import java.util.*

class DateAndTime {
    companion object DateAndTimeFormat {
        const val timeFormat = "YYYY-MM-dd HH:mm:ss.SSS"
        const val dateFormat = "yyyy-MM-dd"
    }

    // Get current time
    fun getSystemTime(): String{
        val mSimpleDateFormat = SimpleDateFormat(timeFormat, Locale.CHINA)
        return mSimpleDateFormat.format(System.currentTimeMillis())
    }

    // Get today
    fun today(): String{
        val mSimpleDateFormat = SimpleDateFormat(dateFormat, Locale.CHINA)
        return mSimpleDateFormat.format(System.currentTimeMillis())
    }
}