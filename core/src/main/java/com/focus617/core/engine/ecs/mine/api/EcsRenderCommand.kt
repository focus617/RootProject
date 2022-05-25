package com.focus617.core.engine.ecs.mine.api

import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.resource.baseDataType.Color
import com.focus617.core.platform.base.BaseEntity

object EcsRenderCommand : BaseEntity() {
    var sEcsRendererAPI: EcsRendererAPI? = null

    fun DrawQuad(transform: Mat4, color: Color) {
        sEcsRendererAPI?.DrawQuad(transform, color)
    }
}