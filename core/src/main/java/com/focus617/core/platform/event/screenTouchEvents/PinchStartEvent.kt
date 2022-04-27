package com.focus617.core.platform.event.screenTouchEvents

import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventCategory
import com.focus617.core.platform.event.base.EventType

/**
 * This event happens when view on screen is being touched
 */
class PinchStartEvent(
    val focusX: Float,      // 两指之间中心点的坐标
    val focusY: Float,
    val span: Float,        // 两指之间的距离
    override val source: Any
) : Event() {

    override val name: String = PinchStartEvent::class.java.simpleName
    override val eventType = EventType.PinchStart
    override val category = setOf(
        EventCategory.EventCategoryScreenTouch,
        EventCategory.EventCategoryInput
    )

}