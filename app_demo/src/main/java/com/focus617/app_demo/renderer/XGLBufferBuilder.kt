package com.focus617.app_demo.renderer

import com.focus617.core.engine.renderer.BufferBuilder
import com.focus617.core.engine.renderer.IfBuffer
import com.focus617.core.engine.renderer.Renderer
import com.focus617.core.engine.renderer.RendererAPI

object XGLBufferBuilder : BufferBuilder() {

    override fun createVertexBuffer(vertices: FloatArray, size: Int): IfBuffer? {
        return when (Renderer.getAPI()) {
            RendererAPI.None -> {
                LOG.error("RendererAPI::None is currently not supported!")
                null
            }
            RendererAPI.OpenGLES -> XGLVertexBuffer(vertices, size)
        }
    }

    override fun createIndexBuffer(indices: ShortArray, size: Int): IfBuffer? {
        return when (Renderer.getAPI()) {
            RendererAPI.None -> {
                LOG.error("RendererAPI::None is currently not supported!")
                null
            }
            RendererAPI.OpenGLES -> XGLIndexBuffer(indices, size)
        }
    }
}