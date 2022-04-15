package com.focus617.core.platform.event.applicationEvent

import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventCategory

/**
 * This event happens in onCreate of Application when all initializations completed
 */
class AppUpdateEvent(
    val variant: AppVariant,
    override val source: Any
) : Event() {

    override val name: String = AppUpdateEvent::class.java.simpleName
    override val eventType = Companion.EventType.AppUpdate
    override val category = setOf(
        EventCategory.EventCategoryApplication,
        EventCategory.EventCategoryInput
    )

}
