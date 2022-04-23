package com.focus617.app_demo.engine

import android.content.Context
import com.focus617.app_demo.renderer.XGLIndexBuffer
import com.focus617.app_demo.renderer.XGLShader
import com.focus617.app_demo.renderer.XGLVertexArray
import com.focus617.app_demo.renderer.XGLVertexBuffer
import com.focus617.core.engine.renderer.IfBuffer
import com.focus617.core.engine.renderer.RendererAPI
import com.focus617.core.engine.renderer.Shader
import com.focus617.core.engine.renderer.ShaderBuilder

object XGLShaderBuilder : ShaderBuilder() {
    override fun createVertexArray(): IfBuffer? {
        return when (RendererAPI.getAPI()) {
            RendererAPI.API.None -> {
                LOG.error("RendererAPI::None is currently not supported!")
                null
            }
            RendererAPI.API.OpenGLES -> XGLVertexArray()
        }
    }

    override fun createVertexBuffer(vertices: FloatArray, size: Int): IfBuffer? {
        return when (RendererAPI.getAPI()) {
            RendererAPI.API.None -> {
                LOG.error("RendererAPI::None is currently not supported!")
                null
            }
            RendererAPI.API.OpenGLES -> XGLVertexBuffer(vertices, size)
        }
    }

    override fun createIndexBuffer(indices: ShortArray, count: Int): IfBuffer? {
        return when (RendererAPI.getAPI()) {
            RendererAPI.API.None -> {
                LOG.error("RendererAPI::None is currently not supported!")
                null
            }
            RendererAPI.API.OpenGLES -> XGLIndexBuffer(indices, count)
        }
    }

    override fun createShader(vertexShaderSrc: String, fragmentShaderSrc: String): Shader? {
        return when (RendererAPI.getAPI()) {
            RendererAPI.API.None -> {
                LOG.error("RendererAPI::None is currently not supported!")
                null
            }
            RendererAPI.API.OpenGLES -> XGLShader(vertexShaderSrc, fragmentShaderSrc)
        }
    }

    fun createShader(
        context: Context,
        vertexShaderResourceId: Int,
        fragmentShaderResourceId: Int
    ): Shader? {
        return when (RendererAPI.getAPI()) {
            RendererAPI.API.None -> {
                LOG.error("RendererAPI::None is currently not supported!")
                null
            }
            RendererAPI.API.OpenGLES -> XGLShader(
                context,
                vertexShaderResourceId,
                fragmentShaderResourceId
            )
        }
    }

    fun createShader(
        context: Context,
        path: String,
        vertexShaderFileName: String,
        fragmentShaderFileName: String
    ): Shader? {
        return when (RendererAPI.getAPI()) {
            RendererAPI.API.None -> {
                LOG.error("RendererAPI::None is currently not supported!")
                null
            }
            RendererAPI.API.OpenGLES -> XGLShader(
                context,
                path,
                vertexShaderFileName,
                fragmentShaderFileName
            )
        }
    }
}