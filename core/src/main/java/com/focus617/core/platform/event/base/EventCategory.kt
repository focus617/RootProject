package com.focus617.core.platform.event.base

// Using for event filter
enum class EventCategory(val category: Int) {
    None(0),
    EventCategoryApplication(1 shl 0),
    EventCategoryInput      (1 shl 1),
    EventCategoryKeyboard   (1 shl 2),
    EventCategoryMouse      (1 shl 3),
    EventCategoryMouseButton(1 shl 4);

    fun getCategoryFlags(): Int = this.category

    infix fun or(that: EventCategory): Int = this.category or that.category
}

typealias EventCategorySet<T> = Set<@JvmSuppressWildcards T>

fun EventCategorySet<EventCategory>.category(): Int {
    var result: Int = 0
    this.forEach(){
        result += it.category
    }
    return result
}

