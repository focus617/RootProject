package com.focus617.app_demo.renderer

import android.content.Context
import com.focus617.core.engine.renderer.RendererAPI
import com.focus617.core.engine.renderer.Shader
import com.focus617.core.engine.renderer.ShaderBuilder

object XGLShaderBuilder : ShaderBuilder() {

    override fun createShader(
        name: String,
        vertexShaderSrc: String,
        fragmentShaderSrc: String
    ): Shader? {
        return when (RendererAPI.getAPI()) {
            RendererAPI.API.None -> {
                LOG.error("RendererAPI::None is currently not supported!")
                null
            }
            RendererAPI.API.OpenGLES -> XGLShader(name, vertexShaderSrc, fragmentShaderSrc)
        }
    }

    fun createShader(
        context: Context,
        name: String,
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
                name,
                vertexShaderResourceId,
                fragmentShaderResourceId
            )
        }
    }

    fun createShader(
        context: Context,
        name: String,
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
                name,
                path,
                vertexShaderFileName,
                fragmentShaderFileName
            )
        }
    }
}