package com.focus617.core.engine.ecs.mine.system

import com.focus617.core.engine.ecs.fleks.AllOf
import com.focus617.core.engine.ecs.fleks.Entity
import com.focus617.core.engine.ecs.fleks.IteratingSystem
import com.focus617.core.engine.ecs.mine.component.Sprite
import com.focus617.core.engine.ecs.mine.component.TransformMatrix
import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.math.Vector2
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.yawClamp
import com.focus617.mylib.logging.ILoggable

/**
 * Care about position, rotation, mass etc.
 */
@AllOf([TransformMatrix::class, Sprite::class])
class PhysicsSystem : IteratingSystem(), ILoggable {
    private val LOG = logger()

    private val transforms = world.mapper<TransformMatrix>()

    private var rotationInDegree: Float = 0f

    init {
        LOG.info("PhysicsSystem launched.")
    }

    override fun onTickEntity(entity: Entity) {
        rotationInDegree++
        val rotationInternal = yawClamp(rotationInDegree, 0f, 360f)
        val treeTransform = Mat4().transform2D(
            Vector3(0f, 0f, 0f), Vector2(1.0f, 1.0f), rotationInternal
        )

        configureEntity(entity) {
            transforms.addOrUpdate(it) { transform.setValue(treeTransform) }
        }
    }

}