package com.focus617.core.engine.ecs.mine.system

import com.focus617.core.engine.ecs.fleks.AllOf
import com.focus617.core.engine.ecs.fleks.Entity
import com.focus617.core.engine.ecs.fleks.IteratingSystem
import com.focus617.core.engine.ecs.mine.component.Transform
import com.focus617.core.engine.ecs.mine.component.TransformMatrix
import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.math.yawClamp
import com.focus617.mylib.logging.ILoggable

/**
 * Care about position, rotation, mass etc.
 */
@AllOf([TransformMatrix::class, Transform::class])
class PhysicsSystem : IteratingSystem(), ILoggable {
    private val LOG = logger()

    private val transforms = world.mapper<Transform>()
    private val transformMatrix = world.mapper<TransformMatrix>()

    private var rotationInDegree: Float = 0f

    init {
        LOG.info("PhysicsSystem launched.")
    }

    override fun onTickEntity(entity: Entity) {
        // 获取Entity的transform属性
        val position = transforms[entity].position
        val scale = transforms[entity].scale
        val rotation = transforms[entity].rotationInDegree

        // 按照移动规律修改transform属性
        rotationInDegree++
        val rotationInternal = yawClamp(rotationInDegree, 0f, 360f)
        rotation.z = rotationInternal

        // 生成TransformMatrix
        val squareTransform = Mat4().transform3D(position.toVector3(), scale, rotation)
        configureEntity(entity) {
            transformMatrix.addOrUpdate(it) { transform.setValue(squareTransform) }
        }
    }

}