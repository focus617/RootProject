package com.focus617.core.engine.ecs.mine.system

import com.focus617.core.engine.ecs.fleks.AllOf
import com.focus617.core.engine.ecs.fleks.Entity
import com.focus617.core.engine.ecs.fleks.IteratingSystem
import com.focus617.core.engine.ecs.mine.api.EcsRenderCommand
import com.focus617.core.engine.ecs.mine.component.Sprite
import com.focus617.core.engine.ecs.mine.component.TransformMatrix
import com.focus617.mylib.logging.ILoggable

@AllOf([TransformMatrix::class, Sprite::class])
class Render2DSystem : IteratingSystem(), ILoggable {
    private val LOG = logger()

    private val transformMapper = world.mapper<TransformMatrix>()
    private val spriteMapper = world.mapper<Sprite>()

    init {
        LOG.info("Render2DSystem launched.")
    }

    override fun onTickEntity(entity: Entity) {
        LOG.info("Render2DSystem onTickEntity: entity.id=${entity.id}")

        val transform = transformMapper[entity]
        val sprite = spriteMapper[entity]

        // 绘制
        EcsRenderCommand.drawQuad(transform.transform, sprite.color)
    }

}