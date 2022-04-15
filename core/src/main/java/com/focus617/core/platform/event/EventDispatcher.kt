package com.focus617.core.platform.event

import com.focus617.core.platform.base.BaseEntity
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventHandler
import javax.inject.Inject
import javax.inject.Provider

typealias EventHandlers<Event> = Set<@JvmSuppressWildcards EventHandler<Event>>

class EventDispatcher<E : Event> @Inject constructor(
    private val handlers: Provider<EventHandlers<E>>
    // 使用Provider是为了避免循环依赖
): BaseEntity() {

    suspend fun dispatch(event: E) {
        handlers.get().forEach {
            if(it.handle(event)) return
        }
    }
}
