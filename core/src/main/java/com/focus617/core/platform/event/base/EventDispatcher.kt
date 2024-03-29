package com.focus617.core.platform.event.base

import com.focus617.core.platform.base.BaseEntity
import java.io.Closeable

// 如果事件被消耗，则返回true，否则false
typealias EventHandler<Event> = ((Event) -> Boolean)

class EventDispatcher : BaseEntity(), Closeable {
    private val handlers = HashMap<EventType, EventHandler<Event>>()

    override fun close() {
        handlers.clear()
    }

    fun sizeForTest() = handlers.size

    fun register(evType: EventType, handler: EventHandler<Event>) {
        handlers[evType] = handler
    }

    fun unRegister(evType: EventType) {
        handlers.remove(evType)
    }

    // 如果事件可以被本地处理，则返回true，否则false
    fun dispatch(event: Event): Boolean {
        var result: Boolean = false

        val fn: EventHandler<Event>? = handlers[event.eventType]

        fn?.apply {
            result = fn(event)
        }

        return result
    }

}