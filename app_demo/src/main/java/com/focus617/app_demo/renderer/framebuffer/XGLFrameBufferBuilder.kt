package com.focus617.app_demo.renderer.framebuffer

import com.focus617.core.engine.renderer.FrameBufferBuilder
import com.focus617.core.engine.renderer.Framebuffer
import com.focus617.core.engine.renderer.RendererAPI

object XGLFrameBufferBuilder : FrameBufferBuilder() {

    override fun createFrameBuffer(width: Int, height: Int): Framebuffer? {
        return when (RendererAPI.getAPI()) {
            RendererAPI.API.None -> {
                LOG.error("RendererAPI::None is currently not supported!")
                null
            }
            RendererAPI.API.OpenGLES -> XGLFrameBuffer(width, height)
        }
    }

}