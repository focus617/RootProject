package com.focus617.app_demo.renderer

import android.content.Context
import com.focus617.core.engine.renderer.RendererAPI
import com.focus617.core.engine.renderer.Shader
import com.focus617.core.engine.renderer.ShaderBuilder

object XGLShaderBuilder : ShaderBuilder() {

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