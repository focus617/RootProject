package com.focus617.core.engine.ecs.system

import com.focus617.core.fleks.IntervalSystem
import com.focus617.core.platform.base.BaseEntity

class DayNightSystem(
    private val eventMgr: EventManager
) : IntervalSystem() {
    private var currentTime = 0f
    private var isDay = false

    override fun onTick() {
        // deltaTime is not needed in every system that's why it is not a parameter of "onTick".
        // However, if you need it, you can still access it via the IteratingSystem's deltaTime property
        currentTime += deltaTime
        if (currentTime >= 1000 && !isDay) {
            isDay = true
            eventMgr.publishEvent("Day")
        } else if (currentTime >= 2000 && isDay) {
            isDay = false
            currentTime = 0f
            eventMgr.publishEvent("Night")
        }
    }

    class EventManager: BaseEntity(){
        fun publishEvent(event: String){
            LOG.info("Publish event $event")
        }
    }
}