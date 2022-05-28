package com.focus617.core.engine.renderer.vertex

import com.focus617.core.engine.renderer.api.IfBuffer
import com.focus617.mylib.logging.WithLogging

abstract class VertexBufferBuilder : WithLogging() {
    abstract fun createVertexArray(): IfBuffer?
    abstract fun createVertexBuffer(vertices: FloatArray, size: Int): IfBuffer?
    abstract fun createVertexBuffer(size: Int): IfBuffer?
    abstract fun createIndexBuffer(indices: ShortArray, count: Int): IfBuffer?
}
