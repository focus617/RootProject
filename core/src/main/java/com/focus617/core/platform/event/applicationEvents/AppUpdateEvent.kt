package com.focus617.core.platform.event.applicationEvents

import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventCategory
import com.focus617.core.platform.event.base.EventType

/**
 * This event happens in onCreate of Application when all initializations completed
 */
class AppUpdateEvent(
    val variant: AppVariant,
    override val source: Any
) : Event() {

    override val name: String = AppUpdateEvent::class.java.simpleName
    override val eventType = EventType.AppUpdate
    override val category = setOf(
        EventCategory.EventCategoryApplication,
        EventCategory.EventCategoryInput
    )

}
