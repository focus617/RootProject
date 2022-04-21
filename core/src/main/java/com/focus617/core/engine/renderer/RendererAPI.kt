package com.focus617.core.engine.renderer

import com.focus617.core.engine.baseDataType.Color
import com.focus617.mylib.logging.WithLogging

abstract class RendererAPI {
    enum class API {
        None,
        OpenGLES
    }

    abstract fun clear()
    abstract fun setClearColor(color: Color)
    abstract fun drawIndexed(vertexArray: VertexArray)

    companion object {
        private val sRendererAPI: API = API.OpenGLES
        fun getAPI(): API = sRendererAPI
    }
}

abstract class VertexArray : WithLogging(), IfBuffer