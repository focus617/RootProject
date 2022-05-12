package com.focus617.app_demo.renderer.shader

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

    /** 基于Assets中的单一glsl文件构造 */
    fun createShader(context: Context, filePath: String): Shader? {
        return when (RendererAPI.getAPI()) {
            RendererAPI.API.None -> {
                LOG.error("RendererAPI::None is currently not supported!")
                null
            }
            RendererAPI.API.OpenGLES -> {
                XGLShader.parseShaderSource(context, filePath)

                val vertexShaderSrc =
                    XGLShader.shaderSources[XGLShader.Companion.ShaderType.VERTEX_SHADER]!!
                val fragmentShaderSrc =
                    XGLShader.shaderSources[XGLShader.Companion.ShaderType.FRAGMENT_SHADER]!!

                return XGLShader(
                    XGLShader.name,
                    vertexShaderSrc,
                    fragmentShaderSrc
                )
            }
        }
    }
}