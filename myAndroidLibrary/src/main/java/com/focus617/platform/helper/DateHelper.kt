package com.focus617.platform.helper

import java.text.SimpleDateFormat
import java.util.*

object DateHelper {
    object DateAndTimeFormat {
        const val timeFormat = "yyyy-MM-dd HH:mm:ss"
        const val dateFormat = "yyyy-MM-dd"
    }

    fun timeStampAsStr(timeInMillis: Long):String =
        SimpleDateFormat(DateAndTimeFormat.timeFormat, Locale.CHINA)
            .format(timeInMillis)

    fun nowAsStr() = timeStampAsStr(System.currentTimeMillis())

    fun todayAsStr():String =
        SimpleDateFormat(DateAndTimeFormat.dateFormat, Locale.CHINA)
            .format(System.currentTimeMillis())

    fun calendarToTimeStamp(calendar: Calendar): Long = calendar.timeInMillis

    fun timeStampToCalendar(value: Long): Calendar =
        Calendar.getInstance().apply { timeInMillis = value }
}