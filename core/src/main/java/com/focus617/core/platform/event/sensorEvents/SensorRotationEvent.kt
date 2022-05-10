package com.focus617.core.platform.event.sensorEvents

import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventCategory
import com.focus617.core.platform.event.base.EventType

/**
 * This event happens when view on screen is being touched
 */
class SensorRotationEvent(
    val pitchXInDegree: Float,
    val yawYInDegree: Float,
    val rollZInDegree: Float,
    override val source: Any
) : Event() {

    override val name: String = SensorRotationEvent::class.java.simpleName
    override val eventType = EventType.SensorRotation
    override val category = setOf(
        EventCategory.EventCategorySensor,
        EventCategory.EventCategoryInput
    )

}