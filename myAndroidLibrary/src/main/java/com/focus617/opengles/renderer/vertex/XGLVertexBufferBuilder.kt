package com.focus617.opengles.renderer.vertex

import com.focus617.core.engine.renderer.IfBuffer
import com.focus617.core.engine.renderer.RendererAPI
import com.focus617.core.engine.renderer.vertex.VertexBufferBuilder
import timber.log.Timber

object XGLVertexBufferBuilder : VertexBufferBuilder() {
    override fun createVertexArray(): IfBuffer? {
        return when (RendererAPI.getAPI()) {
            RendererAPI.API.None -> {
                Timber.e("RendererAPI::None is currently not supported!")
                null
            }
            RendererAPI.API.OpenGLES -> XGLVertexArray()
        }
    }

    override fun createVertexBuffer(vertices: FloatArray, size: Int): IfBuffer? {
        return when (RendererAPI.getAPI()) {
            RendererAPI.API.None -> {
                Timber.e("RendererAPI::None is currently not supported!")
                null
            }
            RendererAPI.API.OpenGLES -> XGLVertexBuffer(vertices, size)
        }
    }

    override fun createVertexBuffer(size: Int): IfBuffer? {
        return when (RendererAPI.getAPI()) {
            RendererAPI.API.None -> {
                Timber.e("RendererAPI::None is currently not supported!")
                null
            }
            RendererAPI.API.OpenGLES -> XGLVertexBuffer(size)
        }
    }

    override fun createIndexBuffer(indices: ShortArray, count: Int): IfBuffer? {
        return when (RendererAPI.getAPI()) {
            RendererAPI.API.None -> {
                Timber.e("RendererAPI::None is currently not supported!")
                null
            }
            RendererAPI.API.OpenGLES -> XGLIndexBuffer(indices, count)
        }
    }
}