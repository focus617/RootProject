package com.focus617.core.engine.renderer.framebuffer

import com.focus617.mylib.logging.WithLogging

abstract class FrameBufferBuilder : WithLogging() {
    abstract fun createFrameBuffer(fbSpec: FrameBufferSpecification): FrameBuffer?
}