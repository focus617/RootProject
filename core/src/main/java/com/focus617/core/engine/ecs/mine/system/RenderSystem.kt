package com.focus617.core.engine.ecs.mine.system

import com.focus617.core.engine.ecs.fleks.AllOf
import com.focus617.core.engine.ecs.fleks.Entity
import com.focus617.core.engine.ecs.fleks.IteratingSystem
import com.focus617.core.engine.ecs.mine.component.CameraMatrix
import com.focus617.core.engine.math.Mat4
import com.focus617.mylib.logging.ILoggable

@AllOf([CameraMatrix::class])
class RenderSystem : IteratingSystem(), ILoggable {
    private val LOG = logger()

    private val matrixMapper = world.mapper<CameraMatrix>()

    init{
        LOG.info("RenderSystem launched.")
    }

    override fun onTickEntity(entity: Entity) {
        val matrix = matrixMapper[entity]
        sProjectionMatrix.setValue(matrix.projectionMatrix)
        sViewMatrix.setValue(matrix.viewMatrix)
    }

    companion object SceneData {
        var sProjectionMatrix: Mat4 = Mat4()
        var sViewMatrix: Mat4 = Mat4()
    }
}