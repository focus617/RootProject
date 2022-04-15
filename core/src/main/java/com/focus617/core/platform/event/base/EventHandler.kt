package com.focus617.core.platform.event.base

fun interface EventHandler<Event> {
    suspend fun handle(event: Event): Boolean
}
