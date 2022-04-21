package com.focus617.core.engine.renderer

import com.focus617.mylib.logging.WithLogging

abstract class BufferBuilder : WithLogging() {
    abstract fun createVertexArray(): IfBuffer?
    abstract fun createVertexBuffer(vertices: FloatArray, size: Int): IfBuffer?
    abstract fun createIndexBuffer(indices: ShortArray, count: Int): IfBuffer?
}

data class BufferElement(
    val name: String,
    val type: ShaderDataType,
    val normalized: Boolean
){
    val size: Int = type.sizeInByte
    var offset: Int = 0
}

class BufferLayout(elements: List<BufferElement>) {
    private val mElements: List<BufferElement> = elements
    private var mStride = 0

    init {
        calculateOffsetsAndStride()
    }

    fun getElements() = mElements
    fun getStride() = mStride

    private fun calculateOffsetsAndStride() {
        var offset = 0
        mStride = 0

        mElements.forEach {
            it.offset = offset
            offset += it.size
            mStride += it.size
        }
    }
}