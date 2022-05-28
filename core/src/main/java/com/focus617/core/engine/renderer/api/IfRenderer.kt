package com.focus617.core.engine.renderer.api

import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.renderer.vertex.VertexArray

interface IfRenderer {

    fun getAPI(): RendererAPI.API = RendererAPI.getAPI()

    open fun beginScene() {}

    open fun endScene() {}

    open fun submit(shader: Shader, vertexArray: VertexArray, transform: FloatArray) {}

}

