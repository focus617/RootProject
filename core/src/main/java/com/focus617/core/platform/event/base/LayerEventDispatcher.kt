package com.focus617.core.platform.event.base

import com.focus617.core.platform.base.BaseEntity
import com.focus617.core.platform.event.base.Event

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

    fun dispatch(event: Event): Boolean {
        var result: Boolean = false

        val fn: XEventHandler<Event>? = handlers[event.eventType]

        fn?.apply {
            result = true
            fn(event)
        }

        return result
    }
}