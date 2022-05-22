package com.focus617.core.ecs.mine.system

import com.focus617.core.ecs.fleks.AllOf
import com.focus617.core.ecs.fleks.ComponentMapper
import com.focus617.core.ecs.fleks.Entity
import com.focus617.core.ecs.fleks.IteratingSystem
import com.focus617.core.ecs.fleks.collection.compareEntity
import com.focus617.core.ecs.mine.component.Position

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