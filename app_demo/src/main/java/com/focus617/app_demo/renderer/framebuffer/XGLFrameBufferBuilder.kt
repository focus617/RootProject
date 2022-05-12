package com.focus617.app_demo.renderer.framebuffer

import com.focus617.core.engine.renderer.RendererAPI
import com.focus617.core.engine.renderer.framebuffer.FrameBuffer
import com.focus617.core.engine.renderer.framebuffer.FrameBufferBuilder

object XGLFrameBufferBuilder : FrameBufferBuilder() {

    override fun createFrameBuffer(width: Int, height: Int): FrameBuffer? {
        return when (RendererAPI.getAPI()) {
            RendererAPI.API.None -> {
                LOG.error("RendererAPI::None is currently not supported!")
                null
            }
            RendererAPI.API.OpenGLES -> XGLFrameBuffer(width, height)
        }
    }

}