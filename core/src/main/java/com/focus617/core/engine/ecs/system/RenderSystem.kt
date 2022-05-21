package com.focus617.core.engine.ecs.system

import com.focus617.core.engine.ecs.component.Position
import com.focus617.core.fleks.AllOf
import com.focus617.core.fleks.ComponentMapper
import com.focus617.core.fleks.Entity
import com.focus617.core.fleks.IteratingSystem
import com.focus617.core.fleks.collection.compareEntity

@AllOf([Position::class])
class RenderSystem(
    private val positions: ComponentMapper<Position>
) : IteratingSystem(compareEntity {
        entA, entB -> positions[entA].position.y.compareTo(positions[entB].position.y)
}) {

    override fun onTickEntity(entity: Entity) {
        // render entities: entities are sorted by their y-coordinate
    }
}