package com.focus617.platform.helper

import java.text.SimpleDateFormat
import java.util.*

object DateHelper {

    fun timeStampAsStr(timeInMillis: Long) =
        SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(timeInMillis)

    fun nowAsStr() = timeStampAsStr(System.currentTimeMillis())

    fun calendarToTimeStamp(calendar: Calendar): Long = calendar.timeInMillis

    fun timeStampToCalendar(value: Long): Calendar =
        Calendar.getInstance().apply { timeInMillis = value }
}