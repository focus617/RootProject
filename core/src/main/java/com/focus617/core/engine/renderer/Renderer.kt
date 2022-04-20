package com.focus617.core.engine.renderer

object Renderer {
    private val sRendererAPI: RendererAPI = RendererAPI.OpenGLES

    fun getAPI(): RendererAPI = sRendererAPI
}

enum class RendererAPI
{
    None,
    OpenGLES
}