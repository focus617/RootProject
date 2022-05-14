package com.focus617.core.engine.renderer.framebuffer

interface IfFrameBufferAttachment {
    abstract var mHandle: Int

    abstract fun bind()
    abstract fun unbind()
}