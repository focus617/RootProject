package com.focus617.core.engine.renderer.vertex

import com.focus617.mylib.logging.ILoggable

enum class ShaderDataType(val sizeInByte: Int) : ILoggable {
    Float1(4 * 1),
    Float2(4 * 2),
    Float3(4 * 3),
    Float4(4 * 4),
    Mat3(4 * 3 * 3),
    Mat4(4 * 4 * 4),
    Int1(4 * 1),
    Int2(4 * 2),
    Int3(4 * 3),
    Int4(4 * 4),
    Bool(1);

    val LOG = logger()

    fun getComponentCount(): Int = when (this) {
        Float1 -> 1
        Float2 -> 2
        Float3 -> 3
        Float4 -> 4
        Mat3 -> 3 * 3
        Mat4 -> 4 * 4
        Int1 -> 1
        Int2 -> 2
        Int3 -> 3
        Int4 -> 4
        Bool -> 1

        else -> {
            LOG.error("Unknown ShaderDataType!")
            0
        }
    }
}