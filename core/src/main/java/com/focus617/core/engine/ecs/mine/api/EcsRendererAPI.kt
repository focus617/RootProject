package com.focus617.core.engine.ecs.mine.api

import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.resource.baseDataType.Color
import com.focus617.mylib.logging.WithLogging

abstract class EcsRendererAPI : WithLogging() {
    enum class API {
        None,
        OpenGLES
    }

    abstract fun DrawQuad(transform: Mat4, color: Color)

    companion object {
        private val sEcsAPI: API = API.OpenGLES
        fun getAPI(): API = sEcsAPI
    }
}