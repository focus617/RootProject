package com.focus617.core.platform.event.base

import com.focus617.core.platform.base.BaseEntity


abstract class Event : BaseEntity() {
    abstract val name: String
    abstract val eventType: EventType
    abstract val category: EventCategorySet<EventCategory>
    abstract val source: Any
    val timestamp: Long = System.currentTimeMillis()

    // 表示 事件是否被处理
    protected var hasBeenHandled: Boolean = false

    fun handleFinished(): Boolean {
        hasBeenHandled = true
        return hasBeenHandled
    }

    override fun toString(): String = name

    fun isInCategory(c: EventCategory): Boolean =
         (category.category() and c.category) != 0

    companion object {
        enum class EventType(val typeId: Int) {
            None(0),

            WindowClose(100), WindowResize(101),
            WindowFocus(102), WindowLostFocus(103),
            WindowMoved(104),

            AppTick(200), AppLaunched(201),
            AppUpdate(202), AppRender(203),

            KeyPressed(300), KeyReleased(301),

            MouseButtonPressed(400), MouseButtonReleased(401),
            MouseMoved(402), MouseScrolled(403)
        }
    }

}