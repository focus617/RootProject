package com.focus617.core.engine.math

data class Vector4(var x: Float, var y: Float, var z: Float, var w: Float) {

    fun normalize(): Vector4 =
        if (w == 0F) Vector4(x, y, z, 0F)
        else Vector4(x / w, y / w, z / w, 1F)

    override fun toString(): String = "Vector($x, $y, $z, $w)"

    override fun equals(other: Any?): Boolean =
        if(other !is Vector4) false
        else ((x==other.x)&&(y==other.y)&&(z==other.z)&&(w==other.w))

    operator fun plus(other: Vector4) = Vector4(
        (x + other.x) / (w + other.w),
        (y + other.y) / (w + other.w),
        (z + other.z) / (w + other.w),
        1F
    )

    fun translate(vector: Vector4) = plus(vector)
            
}