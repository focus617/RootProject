package com.focus617.core.platform.event.screenTouchEvents

import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventCategory
import com.focus617.core.platform.event.base.EventType

/**
 * This event happens when view on screen is being touched
 */
class TouchDragEvent(
    val x: Float,
    val y: Float,
    val normalizedX: Float,
    val normalizedY: Float,
    override val source: Any
) : Event() {

    override val name: String = TouchDragEvent::class.java.simpleName
    override val eventType = EventType.TouchDrag
    override val category = setOf(
        EventCategory.EventCategoryScreenTouch,
        EventCategory.EventCategoryInput
    )

}