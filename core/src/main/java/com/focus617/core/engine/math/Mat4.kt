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

    fun setValue(mat: Mat4): Mat4 {
        System.arraycopy(mat.floatArray, 0, floatArray, 0, 16)
        return this
    }

    fun setValue(value: FloatArray): Mat4 {
        System.arraycopy(value, 0, floatArray, 0, 16)
        return this
    }

    operator fun times(vector4: Vector4): Vector4{
        val result = FloatArray(4)
        XMatrix.xMultiplyMV(result, 0, floatArray, 0, vector4)
        return Vector4(result)
    }

    operator fun times(mat: Mat4): Mat4{
        val result = FloatArray(16)
        XMatrix.xMultiplyMM(result, 0, floatArray, 0, mat.toFloatArray(), 0)
        return Mat4().setValue(result)
    }

    fun setIdentity(): Mat4 {
        XMatrix.setIdentityM(floatArray, 0)
        return this
    }

    fun invert(): Mat4 {
        val result = FloatArray(16)
        XMatrix.invertM(result, 0, floatArray, 0)
        return Mat4().setValue(result)
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

    // 绕Z轴旋转
    fun rotate2D(rotationInDegree: Float): Mat4 {
        val rotationInternal = yawClamp(rotationInDegree, 0f, 360f)
        XMatrix.rotateM(floatArray, 0, rotationInternal, 0.0f, 0.0f, 1.0f)
        return this
    }

    fun rotate3D(
        rotationInDegree: Float,
        xAxis: Float,
        yAxis: Float,
        zAxis: Float
    ): Mat4 {
        val rotationInternal = yawClamp(rotationInDegree, 0f, 360f)
        XMatrix.rotateM(floatArray, 0, rotationInternal, xAxis, yAxis, zAxis)
        return this
    }

    fun transform2D(
        position: Vector3,
        size: Vector2,
        rotationInDegree: Float = 0.0f      // 绕Z轴旋转
    ): Mat4 {
        val translate = Mat4().translate(position).toFloatArray()
        val scale = Mat4().scale(size).toFloatArray()

        val rotationInternal = yawClamp(rotationInDegree, 0f, 360f)
        val rotation = Mat4().rotate2D(rotationInternal).toFloatArray()

        /** 由于采用了列主序的转置矩阵，所以乘法的顺序是反的 */
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

        val rotationInternal = yawClamp(rotationInDegree, 0f, 360f)
        val rotation = Mat4().rotate3D(rotationInternal, xAxis, yAxis, zAxis).toFloatArray()

        /** 由于采用了列主序的转置矩阵，所以乘法的顺序是反的 */
        setIdentity()
        XMatrix.xMultiplyMM(floatArray, 0, translate, 0, floatArray, 0)
        XMatrix.xMultiplyMM(floatArray, 0, rotation, 0, floatArray, 0)
        XMatrix.xMultiplyMM(floatArray, 0, scale, 0, floatArray, 0)
        return this
    }

    fun transform3D(
        position: Vector3,
        size: Vector3,
        eulerRotationInDegree: Vector3
    ): Mat4 {
        val translate = Mat4().translate(position).toFloatArray()
        val scale = Mat4().scale(size).toFloatArray()

        val rotationX = Mat4().rotate3D(eulerRotationInDegree.x, 1.0f, 0.0f, 0.0f)
        val rotationY = Mat4().rotate3D(eulerRotationInDegree.y, 0.0f, 1.0f, 0.0f)
        val rotationZ = Mat4().rotate3D(eulerRotationInDegree.z, 0.0f, 0.0f, 1.0f)
        val rotation = (rotationY * rotationX * rotationZ).toFloatArray()

        /** 由于采用了列主序的转置矩阵，所以乘法的顺序是反的 */
        setIdentity()
        XMatrix.xMultiplyMM(floatArray, 0, translate, 0, floatArray, 0)
        XMatrix.xMultiplyMM(floatArray, 0, rotation, 0, floatArray, 0)
        XMatrix.xMultiplyMM(floatArray, 0, scale, 0, floatArray, 0)
        return this
    }
}