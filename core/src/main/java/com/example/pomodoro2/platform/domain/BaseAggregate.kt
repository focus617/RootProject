package com.example.pomodoro2.platform.domain


abstract class BaseAggregate: BaseEntity() {

    private var _events: MutableList<DomainEvent>? = null

    protected fun raiseEvent(event: DomainEvent) {
        get_events().add(event)
    }

    fun clearEvents() {
        get_events().clear()
    }

    fun get_events(): MutableList<DomainEvent> = _events?:arrayListOf()

}