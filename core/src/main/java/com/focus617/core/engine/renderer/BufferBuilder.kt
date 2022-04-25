package com.focus617.core.engine.renderer

import com.focus617.mylib.logging.WithLogging

abstract class BufferBuilder : WithLogging() {
    abstract fun createVertexArray(): IfBuffer?
    abstract fun createVertexBuffer(vertices: FloatArray, size: Int): IfBuffer?
    abstract fun createIndexBuffer(indices: ShortArray, count: Int): IfBuffer?
}