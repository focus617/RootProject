package com.focus617.core.engine.resource.baseDataType

import com.focus617.core.engine.math.Vector4

data class Color(var r: Float, var g: Float, var b: Float, var a: Float) {

    fun toVector4() = Vector4(r, g, b, a)

    companion object {
        val WHITE = Color(1.0f, 1.0f, 1.0f, 1.0f)
        val BLACK = Color(0.0f, 0.0f, 0.0f, 1.0f)
        val RED = Color(0.8f, 0.3f, 0.2f, 1.0f)
        val BLUE = Color(0.2f, 0.3f, 0.8f, 1.0f)

        val GOLD = Color(0.8314f, 0.6863f, 0.2157f, 1.0f)     //金色
        val CYAN = Color(0.04f, 0.28f, 0.26f, 1.0f)           //青色
    }
}