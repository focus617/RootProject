package com.focus617.opengles.renderer.shader

import android.content.Context
import com.focus617.core.engine.renderer.api.RendererAPI
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.renderer.shader.ShaderBuilder
import timber.log.Timber

object XGLShaderBuilder : ShaderBuilder() {

    override fun createShader(
        name: String,
        vertexShaderSrc: String,
        fragmentShaderSrc: String
    ): Shader? {
        return when (RendererAPI.getAPI()) {
            RendererAPI.API.None -> {
                Timber.e("RendererAPI::None is currently not supported!")
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
                Timber.e("RendererAPI::None is currently not supported!")
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
                Timber.e("RendererAPI::None is currently not supported!")
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
                Timber.e("RendererAPI::None is currently not supported!")
                null
            }
            RendererAPI.API.OpenGLES -> {
                XGLShaderHelper.parseShaderSource(context, filePath)

                val vertexShaderSrc =
                    XGLShaderHelper.shaderSources[XGLShaderHelper.ShaderType.VERTEX_SHADER]!!
                val fragmentShaderSrc =
                    XGLShaderHelper.shaderSources[XGLShaderHelper.ShaderType.FRAGMENT_SHADER]!!

                return XGLShader(
                    filePath,
                    vertexShaderSrc,
                    fragmentShaderSrc
                )
            }
        }
    }
}