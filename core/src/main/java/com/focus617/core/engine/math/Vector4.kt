package com.focus617.core.engine.math

data class Vector4(var x: Float, var y: Float, var z: Float, var w: Float) {

    fun normalize(): Vector4 =
        if (w == 0F) Vector4(x, y, z, 0F)
        else Vector4(x / w, y / w, z / w, 1F)
}