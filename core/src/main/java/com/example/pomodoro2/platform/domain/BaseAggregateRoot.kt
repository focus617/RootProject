package com.example.pomodoro2.platform.domain


abstract class BaseAggregateRoot: BaseEntity() {

    private var _events: MutableList<BaseDomainEvent>? = null

    protected fun raiseEvent(event: BaseDomainEvent) {
        getEvents().add(event)
    }

    fun clearEvents() {
        getEvents().clear()
    }

    fun getEvents(): MutableList<BaseDomainEvent> = _events?:arrayListOf()

}