package com.focus617.core.platform.event.base

import com.focus617.core.platform.base.BaseEntity
import com.focus617.core.platform.event.base.Event

// 如果事件被消耗，则返回true，否则false
typealias XEventHandler<Event> = ((Event) -> Boolean)

class LayerEventDispatcher : BaseEntity() {
    private val handlers = HashMap<EventType, XEventHandler<Event>>()

    fun sizeForTest() = handlers.size

    fun register(evType: EventType, handler: XEventHandler<Event>) {
        handlers[evType] = handler
    }

    fun unRegister(evType: EventType) {
        handlers.remove(evType)
    }

    // 如果事件可以被本地处理，则返回true，否则false
    fun dispatch(event: Event): Boolean {
        var result: Boolean = false

        val fn: XEventHandler<Event>? = handlers[event.eventType]

        fn?.apply {
            result = true
            event.hasBeenHandled = fn(event)
        }

        return result
    }
}