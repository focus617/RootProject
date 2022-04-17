package com.focus617.core.platform.event.base

import com.focus617.core.platform.base.BaseEntity


abstract class Event : BaseEntity() {
    abstract val name: String
    abstract val eventType: EventType
    abstract val category: EventCategorySet<EventCategory>
    abstract val source: Any
    val timestamp: Long = System.currentTimeMillis()

    // 表示 事件是否被处理
    var hasBeenHandled: Boolean = false

    fun handleFinished(): Boolean {
        hasBeenHandled = true
        return hasBeenHandled
    }

    override fun toString(): String = name

    fun isInCategory(c: EventCategory): Boolean =
         (category.category() and c.category) != 0

}