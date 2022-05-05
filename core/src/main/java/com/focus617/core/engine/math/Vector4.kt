package com.focus617.core.engine.math

data class Vector4(var x: Float, var y: Float, var z: Float, var w: Float) {

    constructor(a: FloatArray) : this(a[0], a[1], a[2], a[3])

    fun normalize(): Vector4 =
        if (w == 0F) Vector4(x, y, z, 0F)       // 如果是向量
        else Vector4(x / w, y / w, z / w, 1F)

    fun toFloatArray(): FloatArray = floatArrayOf(x, y, z, w)

    override fun toString(): String = "Vector($x, $y, $z, $w)"

    override fun equals(other: Any?): Boolean =
        if (other !is Vector4) false
        else ((x == other.x) && (y == other.y) && (z == other.z) && (w == other.w))

    operator fun plus(other: Vector4) = Vector4(
        (x + other.x) / (w + other.w),
        (y + other.y) / (w + other.w),
        (z + other.z) / (w + other.w),
        1F
    )

    fun translate(vector: Vector4) = plus(vector)

    fun toPoint3D(): Point3D? {
        if (w == 0f) return null    // 如果是向量

        val normalizedVector4 = this.normalize()
        return Point3D(
            normalizedVector4.x,
            normalizedVector4.y,
            normalizedVector4.z
        )
    }

    fun toVector3(): Vector3 {
        val normalizedVector4 = this.normalize()
        return Vector3(
            normalizedVector4.x,
            normalizedVector4.y,
            normalizedVector4.z
        )
    }

}