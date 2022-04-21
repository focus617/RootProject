package com.focus617.core.engine.renderer

import com.focus617.core.platform.base.BaseEntity

open class XRenderer: BaseEntity() {

    fun getAPI(): RendererAPI.API = RendererAPI.getAPI()

    fun beginScene() {

    }

    fun endScene() {

    }

    fun submit(vertexArray: VertexArray) {
        vertexArray.bind()
        RenderCommand.drawIndexed(vertexArray)
    }

}

