package com.focus617.core.platform.event.screenTouchEvents

import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventCategory
import com.focus617.core.platform.event.base.EventType

/**
 * This event happens when view on screen is being touched
 */
class PinchStartEvent(
    val length: Float,
    override val source: Any
) : Event() {

    override val name: String = PinchStartEvent::class.java.simpleName
    override val eventType = EventType.PinchStart
    override val category = setOf(
        EventCategory.EventCategoryScreenTouch,
        EventCategory.EventCategoryInput
    )

}