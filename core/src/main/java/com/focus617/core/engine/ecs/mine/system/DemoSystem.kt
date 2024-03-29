package com.focus617.core.engine.ecs.mine.system

import com.focus617.core.engine.ecs.fleks.AllOf
import com.focus617.core.engine.ecs.fleks.ComponentMapper
import com.focus617.core.engine.ecs.fleks.Entity
import com.focus617.core.engine.ecs.fleks.IteratingSystem
import com.focus617.core.engine.ecs.fleks.collection.compareEntity
import com.focus617.core.engine.ecs.mine.component.Position

@AllOf([Position::class])
class DemoSystem(
    private val positions: ComponentMapper<Position>
) : IteratingSystem(compareEntity {
        entA, entB -> positions[entA].position.y.compareTo(positions[entB].position.y)
}) {

    override fun onTickEntity(entity: Entity) {
        // render entities: entities are sorted by their y-coordinate
    }
}