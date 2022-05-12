package com.focus617.core.engine.renderer

import com.focus617.mylib.logging.WithLogging

abstract class FrameBufferBuilder : WithLogging() {
    abstract fun createFrameBuffer(width: Int, height: Int): Framebuffer?
}