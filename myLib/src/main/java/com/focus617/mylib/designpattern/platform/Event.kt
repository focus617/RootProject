package com.focus617.mylib.designpattern.platform

const val Event_AppStart = "APPLICATION_START"
const val Event_Wakeup = "WAKEUP"

abstract class Event(
    val name: String,
    val source: BaseObject,
    val loc: String = "",
    val timestamp: Long = System.currentTimeMillis()
)


class AppStartEvent(source: BaseObject, loc: String = "") :
    Event(Event_AppStart, source, loc)

class WakeupEvent(source: BaseObject, loc: String = "") :
    Event(Event_Wakeup, source, loc)