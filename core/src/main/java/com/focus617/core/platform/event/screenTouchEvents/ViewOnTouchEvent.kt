package com.focus617.core.platform.event.screenTouchEvents

import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventCategory
import com.focus617.core.platform.event.base.EventType

/**
 * This event happens when view on screen is being touched
 */
class ViewOnTouchEvent(
    val x: Float,
    val y: Float,
    override val source: Any
) : Event() {

    override val name: String = ViewOnTouchEvent::class.java.simpleName
    override val eventType = EventType.ViewOnTouch
    override val category = setOf(
        EventCategory.EventCategoryScreenTouch,
        EventCategory.EventCategoryInput
    )

}