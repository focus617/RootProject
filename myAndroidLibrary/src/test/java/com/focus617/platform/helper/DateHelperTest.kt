package com.focus617.platform.helper

import org.junit.Assert.*

import org.junit.Test
import java.util.*

class DateHelperTest {

    private val cal = Calendar.getInstance().apply {
        set(Calendar.YEAR, 1998)
        set(Calendar.MONTH, Calendar.SEPTEMBER)
        set(Calendar.DAY_OF_MONTH, 4)
        set(Calendar.AM_PM, Calendar.PM)
        set(Calendar.HOUR, 5)
        set(Calendar.MINUTE, 23)
        set(Calendar.SECOND, 59)
    }

    @Test
    fun timeStampAsStr(){
        assertEquals("1998-09-04 17:23:59", DateHelper.timeStampAsStr(cal.timeInMillis))
    }

    @Test
    fun calendarToTimeStamp_convert_OK() {
        assertEquals(cal.timeInMillis, DateHelper.calendarToTimeStamp(cal))
    }

    @Test
    fun timeStampToCalendar_convert_OK() {
        assertEquals(DateHelper.timeStampToCalendar(cal.timeInMillis), cal)
    }
}