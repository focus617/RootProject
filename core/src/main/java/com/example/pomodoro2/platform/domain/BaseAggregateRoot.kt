package com.example.pomodoro2.platform.domain


abstract class BaseAggregateRoot: BaseEntity() {

    private var _events: MutableList<BaseDomainEvent>? = null

    protected fun raiseEvent(event: BaseDomainEvent) {
        get_events().add(event)
    }

    fun clearEvents() {
        get_events().clear()
    }

    fun get_events(): MutableList<BaseDomainEvent> = _events?:arrayListOf()

}