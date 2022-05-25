package com.focus617.core.engine.ecs.mine.system

import com.focus617.core.engine.ecs.fleks.AllOf
import com.focus617.core.engine.ecs.fleks.Entity
import com.focus617.core.engine.ecs.fleks.IteratingSystem
import com.focus617.core.engine.ecs.mine.component.Sprite
import com.focus617.core.engine.ecs.mine.component.TransformMatrix
import com.focus617.mylib.logging.ILoggable

@AllOf([TransformMatrix::class, Sprite::class])
class Render2DSystem : IteratingSystem(), ILoggable {
    private val LOG = logger()

    private val matrixMapper = world.mapper<TransformMatrix>()

    init {
        LOG.info("RenderSystem launched.")
    }

    override fun onTickEntity(entity: Entity) {
        val matrix = matrixMapper[entity]
        // 使用SubTexture绘制

    }


}