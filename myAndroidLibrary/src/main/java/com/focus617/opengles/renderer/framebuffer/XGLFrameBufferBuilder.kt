package com.focus617.opengles.renderer.framebuffer

import com.focus617.core.engine.renderer.RendererAPI
import com.focus617.core.engine.renderer.framebuffer.FrameBuffer
import com.focus617.core.engine.renderer.framebuffer.FrameBufferBuilder
import com.focus617.core.engine.renderer.framebuffer.FrameBufferSpecification
import timber.log.Timber

object XGLFrameBufferBuilder : FrameBufferBuilder() {

    override fun createFrameBuffer(fbSpec: FrameBufferSpecification): FrameBuffer? {
        return when (RendererAPI.getAPI()) {
            RendererAPI.API.None -> {
                Timber.e("RendererAPI::None is currently not supported!")
                null
            }
            RendererAPI.API.OpenGLES -> XGLFrameBuffer(fbSpec)
        }
    }

}