package com.focus617.core.ecs.mine.component

import com.focus617.core.ecs.fleks.ComponentListener
import com.focus617.core.ecs.fleks.Entity
import com.focus617.core.engine.math.Vector3
import com.focus617.mylib.logging.WithLogging

data class Position(val position: Vector3 = Vector3(0f, 0f, 0f))


class PositionComponentListener : WithLogging(), ComponentListener<Position> {

    override fun onComponentAdded(entity: Entity, component: Position) {
        LOG.info("Component 'position' added, value=${component.position}")
    }

    override fun onComponentRemoved(entity: Entity, component: Position) {
        LOG.info("Component 'position' removed")
    }
}