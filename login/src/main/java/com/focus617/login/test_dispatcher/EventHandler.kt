package com.focus617.login.test_dispatcher

fun interface EventHandler<Event> {
    suspend fun handle(event: Event): Boolean
}
