package com.focus617.mylib.designpattern.platform

abstract class Event(
    val source: BaseObject,
    val timestamp: Long,
    val loc: String
)

class WakeupEvent(source: BaseObject, timestamp: Long, loc: String) : Event(source, timestamp, loc)
class AppStartEvent(source: BaseObject, timestamp: Long, loc: String) : Event(source, timestamp, loc)
