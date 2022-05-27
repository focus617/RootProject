package com.focus617.core.engine.ecs.mine.api

import com.focus617.core.engine.ecs.mine.component.Sprite
import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.resource.baseDataType.Color
import com.focus617.mylib.logging.WithLogging

abstract class EcsRendererAPI : WithLogging() {
    enum class API {
        None,
        OpenGLES
    }

    abstract fun drawQuad(transform: Mat4, color: Color, entityId: Int = -1)
    abstract fun drawSprite(transform: Mat4, src: Sprite, entityId: Int)

    companion object {
        private val sEcsAPI: API = API.OpenGLES
        fun getAPI(): API = sEcsAPI
    }
}