package com.focus617.core.engine.math

data class Mat4(private val floatArray: FloatArray = FloatArray(16)) {

    init {
        setIdentity()
    }

    fun toString(matrixName: String? = null): String {
        val name = matrixName ?: "Matrix"
        return StringBuilder("\n$name:\n").apply {
            for (i in 0..3)
                append(
                    "\t(" + String.format("%6.2f", floatArray[i]) + "," +
                            String.format("%6.2f", floatArray[i + 4]) + "," +
                            String.format("%6.2f", floatArray[i + 8]) + "," +
                            String.format("%6.2f", floatArray[i + 12]) + " )\n"
                )
        }.toString()
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as Mat4
        if (!floatArray.contentEquals(other.floatArray)) return false

        return true
    }

    override fun hashCode(): Int {
        return floatArray.contentHashCode()
    }

    fun toFloatArray() = floatArray

    operator fun times(vector4: Vector4): Vector4{
        val result = FloatArray(4)
        XMatrix.xMultiplyMV(result, 0, floatArray, 0, vector4)
        return Vector4(result)
    }

    fun setIdentity(): Mat4 {
        XMatrix.setIdentityM(floatArray, 0)
        return this
    }

    fun translate(position: Vector3): Mat4 {
        XMatrix.translateM(floatArray, 0, position)
        return this
    }

    fun scale(size: Vector3): Mat4 {
        XMatrix.scaleM(floatArray, 0, size.x, size.y, size.z)
        return this
    }

    fun scale(size: Vector2): Mat4 {
        return this.scale(Vector3(size.x, size.y, 1.0f))
    }

    fun rotate2D(rotationInDegree: Float): Mat4 {
        XMatrix.rotateM(floatArray, 0, rotationInDegree, 0.0f, 0.0f, 1.0f)
        return this
    }

    fun rotate3D(
        rotationInDegree: Float,
        xAxis: Float,
        yAxis: Float,
        zAxis: Float
    ): Mat4 {
        XMatrix.rotateM(floatArray, 0, rotationInDegree, xAxis, yAxis, zAxis)
        return this
    }

    fun transform2D(
        position: Vector3,
        size: Vector2,
        rotationInDegree: Float = 0.0f
    ): Mat4 {
        val translate = Mat4().translate(position).toFloatArray()
        val scale = Mat4().scale(size).toFloatArray()
        val rotation = Mat4().rotate2D(rotationInDegree).toFloatArray()

        setIdentity()
        XMatrix.xMultiplyMM(floatArray, 0, translate, 0, floatArray, 0)
        XMatrix.xMultiplyMM(floatArray, 0, rotation, 0, floatArray, 0)
        XMatrix.xMultiplyMM(floatArray, 0, scale, 0, floatArray, 0)
        return this
    }

    fun transform3D(
        position: Vector3,
        size: Vector3,
        rotationInDegree: Float = 0.0f,
        xAxis: Float,
        yAxis: Float,
        zAxis: Float
    ): Mat4 {
        val translate = Mat4().translate(position).toFloatArray()
        val scale = Mat4().scale(size).toFloatArray()
        val rotation = Mat4().rotate3D(rotationInDegree, xAxis, yAxis, zAxis).toFloatArray()

        setIdentity()
        XMatrix.xMultiplyMM(floatArray, 0, translate, 0, floatArray, 0)
        XMatrix.xMultiplyMM(floatArray, 0, rotation, 0, floatArray, 0)
        XMatrix.xMultiplyMM(floatArray, 0, scale, 0, floatArray, 0)
        return this
    }
}