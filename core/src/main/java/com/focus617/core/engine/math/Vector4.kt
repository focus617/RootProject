package com.focus617.core.engine.math

data class Vector4(var x: Float, var y: Float, var z: Float, var w: Float) {

    constructor(a: FloatArray) : this(a[0], a[1], a[2], a[3])

    constructor(a: Vector4) : this(a.x, a.y, a.z, a.w)

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

    fun setValue(aX: Float, aY: Float, aZ: Float, aW: Float = 1.0f) {
        x = aX
        y = aY
        z = aZ
        w = aW
    }

    fun setValue(value: Vector4) {
        x = value.x
        y = value.y
        z = value.z
        w = value.w
    }

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

    //求向量的模的方法
    fun length(): Float {
        this.normalize()
        return kotlin.math.sqrt((x * x + y * y + z * z).toDouble()).toFloat()
    }

    /**
     * This method transforms a 3D vertex position onto a 2D pixel position.
     */
    fun makePixelCoords(mat: Mat4, viewportWidth: Int, viewportHeight: Int): Point2D {
        // Transform the vector into screen coordinates we assumes mat is
        // ModelViewProjection matrix, transform method multiplies this vector
        // by the matrix
        this.setValue(mat * this)

        // Make coordinates as homogenous
        normalize()

        // Now the vector is normalized to the range [-1.0, 1.0]
        // Normalize values into NDC.
        x = 0.5f + x * 0.5f
        y = 0.5f + y * 0.5f
        z = 0.5f + z * 0.5f
        w = 1.0f

        // Currently the values are clipped to the [0.0, 1.0] range
        // Move coordinates into window space (in pixels)
        return Point2D((x * viewportWidth), (y * viewportHeight))
    }

}