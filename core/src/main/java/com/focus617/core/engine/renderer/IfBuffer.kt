package com.focus617.core.engine.renderer

import com.focus617.mylib.logging.WithLogging

interface IfBuffer{
    abstract fun bind()
    abstract fun unbind()
}

abstract class BufferBuilder: WithLogging() {

    abstract fun createVertexBuffer(vertices: FloatArray, size:Int): IfBuffer?

    abstract fun createIndexBuffer(indices: ShortArray, size:Int): IfBuffer?
}