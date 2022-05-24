package com.focus617.core.engine.ecs.mine.system

import com.focus617.core.engine.ecs.fleks.*
import com.focus617.core.engine.ecs.mine.component.Animation
import com.focus617.core.engine.ecs.mine.component.Health
import com.focus617.core.engine.ecs.mine.component.Position

@AllOf([Position::class])
@NoneOf([Health::class])
@AnyOf([Animation::class])
class AnimationSystem(
    private val positions: ComponentMapper<Position>,
    private val animations: ComponentMapper<Animation>
) : IteratingSystem() {

    override fun onTickEntity(entity: Entity) {
        val entityPosition: Position = positions[entity]

        animations.getOrNull(entity)?.let { animation ->
            // entity has animation component which can be modified inside this block
        }
    }

}