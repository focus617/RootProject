package com.focus617.core.engine.math

import com.focus617.mylib.logging.WithLogging
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

/**
 * Matrix math utilities, import from android.opengl package.
 * 因为 Core 无法使用 Android Library.
 * These methods operate on OpenGL ES format
 * matrices and vectors stored in float arrays.
 *
 *
 * Matrices are 4 x 4 column-vector matrices stored in column-major
 * order:
 * <pre>
 * m[offset +  0] m[offset +  4] m[offset +  8] m[offset + 12]
 * m[offset +  1] m[offset +  5] m[offset +  9] m[offset + 13]
 * m[offset +  2] m[offset +  6] m[offset + 10] m[offset + 14]
 * m[offset +  3] m[offset +  7] m[offset + 11] m[offset + 15]</pre>
 *
 * Vectors are 4 x 1 column vectors stored in order:
 * <pre>
 * v[offset + 0]
 * v[offset + 1]
 * v[offset + 2]
 * v[offset + 3]</pre>
 */
object XMatrix : WithLogging() {
    /** Temporary memory for operations that need temporary Matrix data.  */
    private val sTemp = FloatArray(32)

    // i: row, j:column
    private fun I(i: Int, j: Int) = i + (j * 4)

    /**
     * Multiplies two 4x4 matrices together and stores the result in a third 4x4
     * Matrix. In Matrix notation: result = lhs x rhs. Due to the way
     * Matrix multiplication works, the result Matrix will have the same
     * effect as first multiplying by the rhs Matrix, then multiplying by
     * the lhs Matrix. This is the opposite of what you might expect.
     *
     *
     * The same float array may be passed for result, lhs, and/or rhs. However,
     * the result element values are undefined if the result elements overlap
     * either the lhs or rhs elements.
     *
     * @param result The float array that holds the result.
     * @param resultOffset The offset into the result array where the result is
     * stored.
     * @param lhs The float array that holds the left-hand-side Matrix.
     * @param lhsOffset The offset into the lhs array where the lhs is stored
     * @param rhs The float array that holds the right-hand-side Matrix.
     * @param rhsOffset The offset into the rhs array where the rhs is stored.
     *
     * @throws IllegalArgumentException if result, lhs, or rhs are null, or if
     * resultOffset + 16 > result.length or lhsOffset + 16 > lhs.length or
     * rhsOffset + 16 > rhs.length.
     */
//    external fun multiplyMM(
//        result: FloatArray, resultOffset: Int,
//        lhs: FloatArray, lhsOffset: Int, rhs: FloatArray, rhsOffset: Int
//    )

    fun xMultiplyMM(
        result: FloatArray, resultOffset: Int,
        lhs: FloatArray, lhsOffset: Int, rhs: FloatArray, rhsOffset: Int
    ) {
        synchronized(result) {
            for (i in 0..3) {
                val rhs_i0: Float = rhs[rhsOffset + I(i, 0)]
                var ri0: Float = lhs[lhsOffset + I(0, 0)] * rhs_i0
                var ri1: Float = lhs[lhsOffset + I(0, 1)] * rhs_i0
                var ri2: Float = lhs[lhsOffset + I(0, 2)] * rhs_i0
                var ri3: Float = lhs[lhsOffset + I(0, 3)] * rhs_i0
                for (j in 1..3) {
                    val rhs_ij: Float = rhs[rhsOffset + I(i, j)]
                    ri0 += lhs[lhsOffset + I(j, 0)] * rhs_ij
                    ri1 += lhs[lhsOffset + I(j, 1)] * rhs_ij
                    ri2 += lhs[lhsOffset + I(j, 2)] * rhs_ij
                    ri3 += lhs[lhsOffset + I(j, 3)] * rhs_ij
                }
                result[I(i, 0)] = ri0
                result[I(i, 1)] = ri1
                result[I(i, 2)] = ri2
                result[I(i, 3)] = ri3
            }
        }
    }

    /**
     * Multiplies a 4 element vector by a 4x4 Matrix and stores the result in a
     * 4-element column vector. In Matrix notation: result = lhs x rhs
     *
     *
     * The same float array may be passed for resultVec, lhsMat, and/or rhsVec.
     * However, the resultVec element values are undefined if the resultVec
     * elements overlap either the lhsMat or rhsVec elements.
     *
     * @param resultVec The float array that holds the result vector.
     * @param resultVecOffset The offset into the result array where the result
     * vector is stored.
     * @param lhsMat The float array that holds the left-hand-side Matrix.
     * @param lhsMatOffset The offset into the lhs array where the lhs is stored
     * @param rhsVec The float array that holds the right-hand-side vector.
     * @param rhsVecOffset The offset into the rhs vector where the rhs vector
     * is stored.
     *
     * @throws IllegalArgumentException if resultVec, lhsMat,
     * or rhsVec are null, or if resultVecOffset + 4 > resultVec.length
     * or lhsMatOffset + 16 > lhsMat.length or
     * rhsVecOffset + 4 > rhsVec.length.
     */
    external fun multiplyMV(
        resultVec: FloatArray,
        resultVecOffset: Int, lhsMat: FloatArray, lhsMatOffset: Int,
        rhsVec: FloatArray, rhsVecOffset: Int
    )

    fun xMultiplyMV(
        resultVec: FloatArray,
        resultVecOffset: Int, lhsMat: FloatArray, lhsMatOffset: Int,
        rhsVec: FloatArray, rhsVecOffset: Int
    ) {
        for (i in 0..3) {
            resultVec[resultVecOffset + i] = lhsMat[lhsMatOffset + I(i, 0)] * rhsVec[rhsVecOffset + 0]
            for (j in 1..3)
                resultVec[resultVecOffset + i] +=
                    lhsMat[lhsMatOffset + I(i, j)] * rhsVec[rhsVecOffset + j]
        }
    }

    /**
     * Transposes a 4 x 4 Matrix.
     * 矩阵的转置操作
     *
     * mTrans and m must not overlap.
     *
     * @param mTrans the array that holds the output transposed Matrix(转置矩阵)
     * @param mTransOffset an offset into mTrans where the transposed Matrix is
     * stored.
     * @param m the input array
     * @param mOffset an offset into m where the input Matrix is stored.
     */
    fun transposeM(
        mTrans: FloatArray, mTransOffset: Int, m: FloatArray,
        mOffset: Int
    ) {
        for (i in 0..3) {
            val mBase = i * 4 + mOffset
            mTrans[i + mTransOffset] = m[mBase]
            mTrans[i + 4 + mTransOffset] = m[mBase + 1]
            mTrans[i + 8 + mTransOffset] = m[mBase + 2]
            mTrans[i + 12 + mTransOffset] = m[mBase + 3]
        }
    }

    /**
     * Inverts a 4 x 4 Matrix.
     * 矩阵的逆操作
     *
     * mInv and m must not overlap.
     *
     * @param mInv the array that holds the output inverted Matrix(逆矩阵)
     * @param mInvOffset an offset into mInv where the inverted Matrix is
     * stored.
     * @param m the input array
     * @param mOffset an offset into m where the input Matrix is stored.
     * @return true if the Matrix could be inverted, false if it could not.
     */
    fun invertM(
        mInv: FloatArray, mInvOffset: Int, m: FloatArray, mOffset: Int
    ): Boolean {
        // Invert a 4 x 4 Matrix using Cramer's Rule

        // transpose Matrix
        val src0 = m[mOffset + 0]
        val src4 = m[mOffset + 1]
        val src8 = m[mOffset + 2]
        val src12 = m[mOffset + 3]
        val src1 = m[mOffset + 4]
        val src5 = m[mOffset + 5]
        val src9 = m[mOffset + 6]
        val src13 = m[mOffset + 7]
        val src2 = m[mOffset + 8]
        val src6 = m[mOffset + 9]
        val src10 = m[mOffset + 10]
        val src14 = m[mOffset + 11]
        val src3 = m[mOffset + 12]
        val src7 = m[mOffset + 13]
        val src11 = m[mOffset + 14]
        val src15 = m[mOffset + 15]

        // calculate pairs for first 8 elements (cofactors)
        val atmp0 = src10 * src15
        val atmp1 = src11 * src14
        val atmp2 = src9 * src15
        val atmp3 = src11 * src13
        val atmp4 = src9 * src14
        val atmp5 = src10 * src13
        val atmp6 = src8 * src15
        val atmp7 = src11 * src12
        val atmp8 = src8 * src14
        val atmp9 = src10 * src12
        val atmp10 = src8 * src13
        val atmp11 = src9 * src12

        // calculate first 8 elements (cofactors)
        val dst0 = (atmp0 * src5 + atmp3 * src6 + atmp4 * src7
                - (atmp1 * src5 + atmp2 * src6 + atmp5 * src7))
        val dst1 = (atmp1 * src4 + atmp6 * src6 + atmp9 * src7
                - (atmp0 * src4 + atmp7 * src6 + atmp8 * src7))
        val dst2 = (atmp2 * src4 + atmp7 * src5 + atmp10 * src7
                - (atmp3 * src4 + atmp6 * src5 + atmp11 * src7))
        val dst3 = (atmp5 * src4 + atmp8 * src5 + atmp11 * src6
                - (atmp4 * src4 + atmp9 * src5 + atmp10 * src6))
        val dst4 = (atmp1 * src1 + atmp2 * src2 + atmp5 * src3
                - (atmp0 * src1 + atmp3 * src2 + atmp4 * src3))
        val dst5 = (atmp0 * src0 + atmp7 * src2 + atmp8 * src3
                - (atmp1 * src0 + atmp6 * src2 + atmp9 * src3))
        val dst6 = (atmp3 * src0 + atmp6 * src1 + atmp11 * src3
                - (atmp2 * src0 + atmp7 * src1 + atmp10 * src3))
        val dst7 = (atmp4 * src0 + atmp9 * src1 + atmp10 * src2
                - (atmp5 * src0 + atmp8 * src1 + atmp11 * src2))

        // calculate pairs for second 8 elements (cofactors)
        val btmp0 = src2 * src7
        val btmp1 = src3 * src6
        val btmp2 = src1 * src7
        val btmp3 = src3 * src5
        val btmp4 = src1 * src6
        val btmp5 = src2 * src5
        val btmp6 = src0 * src7
        val btmp7 = src3 * src4
        val btmp8 = src0 * src6
        val btmp9 = src2 * src4
        val btmp10 = src0 * src5
        val btmp11 = src1 * src4

        // calculate second 8 elements (cofactors)
        val dst8 = (btmp0 * src13 + btmp3 * src14 + btmp4 * src15
                - (btmp1 * src13 + btmp2 * src14 + btmp5 * src15))
        val dst9 = (btmp1 * src12 + btmp6 * src14 + btmp9 * src15
                - (btmp0 * src12 + btmp7 * src14 + btmp8 * src15))
        val dst10 = (btmp2 * src12 + btmp7 * src13 + btmp10 * src15
                - (btmp3 * src12 + btmp6 * src13 + btmp11 * src15))
        val dst11 = (btmp5 * src12 + btmp8 * src13 + btmp11 * src14
                - (btmp4 * src12 + btmp9 * src13 + btmp10 * src14))
        val dst12 = (btmp2 * src10 + btmp5 * src11 + btmp1 * src9
                - (btmp4 * src11 + btmp0 * src9 + btmp3 * src10))
        val dst13 = (btmp8 * src11 + btmp0 * src8 + btmp7 * src10
                - (btmp6 * src10 + btmp9 * src11 + btmp1 * src8))
        val dst14 = (btmp6 * src9 + btmp11 * src11 + btmp3 * src8
                - (btmp10 * src11 + btmp2 * src8 + btmp7 * src9))
        val dst15 = (btmp10 * src10 + btmp4 * src8 + btmp9 * src9
                - (btmp8 * src9 + btmp11 * src10 + btmp5 * src8))

        // calculate determinant
        val det = src0 * dst0 + src1 * dst1 + src2 * dst2 + src3 * dst3
        if (det == 0.0f) {
            return false
        }

        // calculate Matrix inverse
        val invdet = 1.0f / det
        mInv[mInvOffset] = dst0 * invdet
        mInv[1 + mInvOffset] = dst1 * invdet
        mInv[2 + mInvOffset] = dst2 * invdet
        mInv[3 + mInvOffset] = dst3 * invdet
        mInv[4 + mInvOffset] = dst4 * invdet
        mInv[5 + mInvOffset] = dst5 * invdet
        mInv[6 + mInvOffset] = dst6 * invdet
        mInv[7 + mInvOffset] = dst7 * invdet
        mInv[8 + mInvOffset] = dst8 * invdet
        mInv[9 + mInvOffset] = dst9 * invdet
        mInv[10 + mInvOffset] = dst10 * invdet
        mInv[11 + mInvOffset] = dst11 * invdet
        mInv[12 + mInvOffset] = dst12 * invdet
        mInv[13 + mInvOffset] = dst13 * invdet
        mInv[14 + mInvOffset] = dst14 * invdet
        mInv[15 + mInvOffset] = dst15 * invdet
        return true
    }

    /**
     * Computes an orthographic projection Matrix(正交投影矩阵).
     *
     * @param m returns the result
     * @param mOffset
     * @param left
     * @param right
     * @param bottom
     * @param top
     * @param near
     * @param far
     */
    fun orthoM(
        m: FloatArray, mOffset: Int,
        left: Float, right: Float, bottom: Float, top: Float,
        near: Float, far: Float
    ) {
        require(left != right) { "left == right" }
        require(bottom != top) { "bottom == top" }
        require(near != far) { "near == far" }
        val rWidth = 1.0f / (right - left)
        val rHeight = 1.0f / (top - bottom)
        val rDepth = 1.0f / (far - near)
        val x = 2.0f * rWidth
        val y = 2.0f * rHeight
        val z = -2.0f * rDepth
        val tx = -(right + left) * rWidth
        val ty = -(top + bottom) * rHeight
        val tz = -(far + near) * rDepth
        m[mOffset + 0] = x
        m[mOffset + 5] = y
        m[mOffset + 10] = z
        m[mOffset + 12] = tx
        m[mOffset + 13] = ty
        m[mOffset + 14] = tz
        m[mOffset + 15] = 1.0f
        m[mOffset + 1] = 0.0f
        m[mOffset + 2] = 0.0f
        m[mOffset + 3] = 0.0f
        m[mOffset + 4] = 0.0f
        m[mOffset + 6] = 0.0f
        m[mOffset + 7] = 0.0f
        m[mOffset + 8] = 0.0f
        m[mOffset + 9] = 0.0f
        m[mOffset + 11] = 0.0f
    }

    /**
     * Defines a projection Matrix in terms of six clip planes.
     *
     * @param m the float array that holds the output perspective Matrix
     * @param offset the offset into float array m where the perspective
     * Matrix data is written
     * @param left
     * @param right
     * @param bottom
     * @param top
     * @param near
     * @param far
     */
    fun frustumM(
        m: FloatArray, offset: Int,
        left: Float, right: Float, bottom: Float, top: Float,
        near: Float, far: Float
    ) {
        require(left != right) { "left == right" }
        require(top != bottom) { "top == bottom" }
        require(near != far) { "near == far" }
        require(near > 0.0f) { "near <= 0.0f" }
        require(far > 0.0f) { "far <= 0.0f" }
        val rWidth = 1.0f / (right - left)
        val rHeight = 1.0f / (top - bottom)
        val rDepth = 1.0f / (near - far)
        val x = 2.0f * (near * rWidth)
        val y = 2.0f * (near * rHeight)
        val A = (right + left) * rWidth
        val B = (top + bottom) * rHeight
        val C = (far + near) * rDepth
        val D = 2.0f * (far * near * rDepth)
        m[offset + 0] = x
        m[offset + 5] = y
        m[offset + 8] = A
        m[offset + 9] = B
        m[offset + 10] = C
        m[offset + 14] = D
        m[offset + 11] = -1.0f
        m[offset + 1] = 0.0f
        m[offset + 2] = 0.0f
        m[offset + 3] = 0.0f
        m[offset + 4] = 0.0f
        m[offset + 6] = 0.0f
        m[offset + 7] = 0.0f
        m[offset + 12] = 0.0f
        m[offset + 13] = 0.0f
        m[offset + 15] = 0.0f
    }

    /**
     * Defines a projection Matrix in terms of a field of view angle, an
     * aspect ratio, and z clip planes.
     *
     * @param m the float array that holds the perspective Matrix
     * @param offset the offset into float array m where the perspective
     * Matrix data is written
     * @param fovy field of view in y direction, in degrees
     * @param aspect width to height aspect ratio of the viewport
     * @param zNear
     * @param zFar
     */
    fun perspectiveM(
        m: FloatArray, offset: Int,
        fovy: Float, aspect: Float, zNear: Float, zFar: Float
    ) {
        val f = 1.0f / Math.tan(fovy * (Math.PI / 360.0)).toFloat()
        val rangeReciprocal = 1.0f / (zNear - zFar)
        m[offset + 0] = f / aspect
        m[offset + 1] = 0.0f
        m[offset + 2] = 0.0f
        m[offset + 3] = 0.0f
        m[offset + 4] = 0.0f
        m[offset + 5] = f
        m[offset + 6] = 0.0f
        m[offset + 7] = 0.0f
        m[offset + 8] = 0.0f
        m[offset + 9] = 0.0f
        m[offset + 10] = (zFar + zNear) * rangeReciprocal
        m[offset + 11] = -1.0f
        m[offset + 12] = 0.0f
        m[offset + 13] = 0.0f
        m[offset + 14] = 2.0f * zFar * zNear * rangeReciprocal
        m[offset + 15] = 0.0f
    }

    /**
     * Sets Matrix m to the identity Matrix.
     *
     * @param sm returns the result
     * @param smOffset index into sm where the result Matrix starts
     */
    fun setIdentityM(sm: FloatArray, smOffset: Int) {
        for (i in 0..15) {
            sm[smOffset + i] = 0f
        }
        var i = 0
        while (i < 16) {
            sm[smOffset + i] = 1.0f
            i += 5
        }
    }

    /**
     * Scales Matrix m by x, y, and z, putting the result in sm.
     *
     *
     * m and sm must not overlap.
     *
     * @param sm returns the result
     * @param smOffset index into sm where the result Matrix starts
     * @param m source Matrix
     * @param mOffset index into m where the source Matrix starts
     * @param x scale factor x
     * @param y scale factor y
     * @param z scale factor z
     */
    fun scaleM(
        sm: FloatArray, smOffset: Int,
        m: FloatArray, mOffset: Int,
        x: Float, y: Float, z: Float
    ) {
        for (i in 0..3) {
            val smi = smOffset + i
            val mi = mOffset + i
            sm[smi] = m[mi] * x
            sm[4 + smi] = m[4 + mi] * y
            sm[8 + smi] = m[8 + mi] * z
            sm[12 + smi] = m[12 + mi]
        }
    }

    /**
     * Scales Matrix m in place by sx, sy, and sz.
     *
     * @param m Matrix to scale
     * @param mOffset index into m where the Matrix starts
     * @param x scale factor x
     * @param y scale factor y
     * @param z scale factor z
     */
    fun scaleM(
        m: FloatArray, mOffset: Int,
        x: Float, y: Float, z: Float
    ) {
        for (i in 0..3) {
            val mi = mOffset + i
            m[mi] *= x
            m[4 + mi] *= y
            m[8 + mi] *= z
        }
    }

    /**
     * Translates Matrix m by x, y, and z, putting the result in tm.
     *
     *
     * m and tm must not overlap.
     *
     * @param tm returns the result
     * @param tmOffset index into sm where the result Matrix starts
     * @param m source Matrix
     * @param mOffset index into m where the source Matrix starts
     * @param x translation factor x
     * @param y translation factor y
     * @param z translation factor z
     */
    fun translateM(
        tm: FloatArray, tmOffset: Int,
        m: FloatArray, mOffset: Int,
        x: Float, y: Float, z: Float
    ) {
        for (i in 0..11) {
            tm[tmOffset + i] = m[mOffset + i]
        }
        for (i in 0..3) {
            val tmi = tmOffset + i
            val mi = mOffset + i
            tm[12 + tmi] = m[mi] * x + m[4 + mi] * y + m[8 + mi] * z +
                    m[12 + mi]
        }
    }

    fun translateM(
        tm: FloatArray, tmOffset: Int,
        m: FloatArray, mOffset: Int,
        delta: Vector3
    ) {
        translateM(tm, tmOffset, m, mOffset, delta.x, delta.y, delta.z)
    }

    /**
     * Translates Matrix m by x, y, and z in place.
     *
     * @param m Matrix
     * @param mOffset index into m where the Matrix starts
     * @param x translation factor x
     * @param y translation factor y
     * @param z translation factor z
     */
    fun translateM(
        m: FloatArray, mOffset: Int,
        x: Float, y: Float, z: Float
    ) {
        for (i in 0..3) {
            val mi = mOffset + i
            m[12 + mi] += m[mi] * x + m[4 + mi] * y + m[8 + mi] * z
        }
    }

    fun translateM(
        m: FloatArray, mOffset: Int,
        delta: Vector3
    ) {
        translateM(m, mOffset, delta.x, delta.y, delta.z)
    }

    /**
     * Rotates Matrix m by angle a (in degrees) around the axis (x, y, z).
     *
     *
     * m and rm must not overlap.
     *
     * @param rm returns the result
     * @param rmOffset index into rm where the result Matrix starts
     * @param m source Matrix
     * @param mOffset index into m where the source Matrix starts
     * @param a angle to rotate in degrees
     * @param x X axis component
     * @param y Y axis component
     * @param z Z axis component
     */
    fun rotateM(
        rm: FloatArray, rmOffset: Int,
        m: FloatArray, mOffset: Int,
        a: Float, x: Float, y: Float, z: Float
    ) {
        synchronized(sTemp) {
            XMatrix.setRotateM(sTemp, 0, a, x, y, z)
            xMultiplyMM(rm, rmOffset, m, mOffset, sTemp, 0)
        }
    }

    /**
     * Rotates Matrix m in place by angle a (in degrees)
     * around the axis (x, y, z).
     *
     * @param m source Matrix
     * @param mOffset index into m where the Matrix starts
     * @param a angle to rotate in degrees
     * @param x X axis component
     * @param y Y axis component
     * @param z Z axis component
     */
    fun rotateM(
        m: FloatArray, mOffset: Int,
        a: Float, x: Float, y: Float, z: Float
    ) {
        synchronized(sTemp) {
            XMatrix.setRotateM(sTemp, 0, a, x, y, z)
            xMultiplyMM(sTemp, 16, m, mOffset, sTemp, 0)
            System.arraycopy(sTemp, 0, m, mOffset, 16)
        }
    }

    /**
     * Creates a Matrix for rotation by angle a (in degrees)
     * around the axis (x, y, z).
     *
     *
     * An optimized path will be used for rotation about a major axis
     * (e.g. x=1.0f y=0.0f z=0.0f).
     *
     * @param rm returns the result
     * @param rmOffset index into rm where the result Matrix starts
     * @param angle angle to rotate in degrees
     * @param xAxis X axis component
     * @param yAxis Y axis component
     * @param zAxis Z axis component
     */
    fun setRotateM(
        rm: FloatArray, rmOffset: Int,
        angle: Float, xAxis: Float, yAxis: Float, zAxis: Float
    ) {
        var a = angle
        var x = xAxis
        var y = yAxis
        var z = zAxis
        rm[rmOffset + 3] = 0f
        rm[rmOffset + 7] = 0f
        rm[rmOffset + 11] = 0f
        rm[rmOffset + 12] = 0f
        rm[rmOffset + 13] = 0f
        rm[rmOffset + 14] = 0f
        rm[rmOffset + 15] = 1f
        a *= (Math.PI / 180.0f).toFloat()
        val s = sin(a.toDouble()).toFloat()
        val c = cos(a.toDouble()).toFloat()
        if (1.0f == x && 0.0f == y && 0.0f == z) {
            rm[rmOffset + 5] = c
            rm[rmOffset + 10] = c
            rm[rmOffset + 6] = s
            rm[rmOffset + 9] = -s
            rm[rmOffset + 1] = 0f
            rm[rmOffset + 2] = 0f
            rm[rmOffset + 4] = 0f
            rm[rmOffset + 8] = 0f
            rm[rmOffset + 0] = 1f
        } else if (0.0f == x && 1.0f == y && 0.0f == z) {
            rm[rmOffset + 0] = c
            rm[rmOffset + 10] = c
            rm[rmOffset + 8] = s
            rm[rmOffset + 2] = -s
            rm[rmOffset + 1] = 0f
            rm[rmOffset + 4] = 0f
            rm[rmOffset + 6] = 0f
            rm[rmOffset + 9] = 0f
            rm[rmOffset + 5] = 1f
        } else if (0.0f == x && 0.0f == y && 1.0f == z) {
            rm[rmOffset + 0] = c
            rm[rmOffset + 5] = c
            rm[rmOffset + 1] = s
            rm[rmOffset + 4] = -s
            rm[rmOffset + 2] = 0f
            rm[rmOffset + 6] = 0f
            rm[rmOffset + 8] = 0f
            rm[rmOffset + 9] = 0f
            rm[rmOffset + 10] = 1f
        } else {
            val len: Float = XMatrix.length(x, y, z)
            if (1.0f != len) {
                val recipLen = 1.0f / len
                x *= recipLen
                y *= recipLen
                z *= recipLen
            }
            val nc = 1.0f - c
            val xy = x * y
            val yz = y * z
            val zx = z * x
            val xs = x * s
            val ys = y * s
            val zs = z * s
            rm[rmOffset + 0] = x * x * nc + c
            rm[rmOffset + 4] = xy * nc - zs
            rm[rmOffset + 8] = zx * nc + ys
            rm[rmOffset + 1] = xy * nc + zs
            rm[rmOffset + 5] = y * y * nc + c
            rm[rmOffset + 9] = yz * nc - xs
            rm[rmOffset + 2] = zx * nc - ys
            rm[rmOffset + 6] = yz * nc + xs
            rm[rmOffset + 10] = z * z * nc + c
        }
    }

    /**
     * Converts Euler angles to a rotation Matrix.
     *
     * @param rm returns the result
     * @param rmOffset index into rm where the result Matrix starts
     * @param pitch angle of rotation in x axis, in degrees
     * @param yaw angle of rotation in y axis, in degrees
     * @param roll angle of rotation in z axis, in degrees
     */
    fun setRotateEulerM(
        rm: FloatArray, rmOffset: Int,
        pitch: Float, yaw: Float, roll: Float
    ) {
        var x = pitch
        var y = yaw
        var z = roll
        x *= (Math.PI / 180.0f).toFloat()
        y *= (Math.PI / 180.0f).toFloat()
        z *= (Math.PI / 180.0f).toFloat()
        val cx = cos(x.toDouble()).toFloat()
        val sx = sin(x.toDouble()).toFloat()
        val cy = cos(y.toDouble()).toFloat()
        val sy = sin(y.toDouble()).toFloat()
        val cz = cos(z.toDouble()).toFloat()
        val sz = sin(z.toDouble()).toFloat()
        val cxsy = cx * sy
        val sxsy = sx * sy
        rm[rmOffset + 0] = cy * cz
        rm[rmOffset + 1] = -cy * sz
        rm[rmOffset + 2] = sy
        rm[rmOffset + 3] = 0.0f
        rm[rmOffset + 4] = cxsy * cz + cx * sz
        rm[rmOffset + 5] = -cxsy * sz + cx * cz
        rm[rmOffset + 6] = -sx * cy
        rm[rmOffset + 7] = 0.0f
        rm[rmOffset + 8] = -sxsy * cz + sx * sz
        rm[rmOffset + 9] = sxsy * sz + sx * cz
        rm[rmOffset + 10] = cx * cy
        rm[rmOffset + 11] = 0.0f
        rm[rmOffset + 12] = 0.0f
        rm[rmOffset + 13] = 0.0f
        rm[rmOffset + 14] = 0.0f
        rm[rmOffset + 15] = 1.0f
    }

    /**
     * Defines a viewing transformation in terms of an eye point, a center of
     * view, and an up vector.
     *
     * @param rm returns the result
     * @param rmOffset index into rm where the result Matrix starts
     * @param eyeX eye point X
     * @param eyeY eye point Y
     * @param eyeZ eye point Z
     * @param centerX center of view X
     * @param centerY center of view Y
     * @param centerZ center of view Z
     * @param upX up vector X
     * @param upY up vector Y
     * @param upZ up vector Z
     */
    fun setLookAtM(
        rm: FloatArray, rmOffset: Int,
        eyeX: Float, eyeY: Float, eyeZ: Float,
        centerX: Float, centerY: Float, centerZ: Float, upX: Float, upY: Float,
        upZ: Float
    ) {

        // See the OpenGL GLUT documentation for gluLookAt for a description
        // of the algorithm. We implement it in a straightforward way:
        var fx = centerX - eyeX
        var fy = centerY - eyeY
        var fz = centerZ - eyeZ

        // Normalize f
        val rlf: Float = 1.0f / XMatrix.length(fx, fy, fz)
        fx *= rlf
        fy *= rlf
        fz *= rlf

        // compute s = f x up (x means "cross product")
        var sx = fy * upZ - fz * upY
        var sy = fz * upX - fx * upZ
        var sz = fx * upY - fy * upX

        // and normalize s
        val rls: Float = 1.0f / XMatrix.length(sx, sy, sz)
        sx *= rls
        sy *= rls
        sz *= rls

        // compute u = s x f
        val ux = sy * fz - sz * fy
        val uy = sz * fx - sx * fz
        val uz = sx * fy - sy * fx
        rm[rmOffset + 0] = sx
        rm[rmOffset + 1] = ux
        rm[rmOffset + 2] = -fx
        rm[rmOffset + 3] = 0.0f
        rm[rmOffset + 4] = sy
        rm[rmOffset + 5] = uy
        rm[rmOffset + 6] = -fy
        rm[rmOffset + 7] = 0.0f
        rm[rmOffset + 8] = sz
        rm[rmOffset + 9] = uz
        rm[rmOffset + 10] = -fz
        rm[rmOffset + 11] = 0.0f
        rm[rmOffset + 12] = 0.0f
        rm[rmOffset + 13] = 0.0f
        rm[rmOffset + 14] = 0.0f
        rm[rmOffset + 15] = 1.0f
        XMatrix.translateM(rm, rmOffset, -eyeX, -eyeY, -eyeZ)
    }

    /**
     * Computes the length of a vector.
     *
     * @param x x coordinate of a vector
     * @param y y coordinate of a vector
     * @param z z coordinate of a vector
     * @return the length of a vector
     */
    // TODO: reuse Vector module()
    fun length(x: Float, y: Float, z: Float): Float {
        return sqrt((x * x + y * y + z * z).toDouble()).toFloat()
    }

    fun toString(m: FloatArray, mOffset: Int = 0, matrixName: String? = null): String {
        val name = matrixName ?: "Matrix"
        return StringBuilder("\nDump $name:\n").apply {
            for (i in 0..3)
                append(
                    "\t(" +
                            String.format("%6.2f", m[mOffset + i]) + "," +
                            String.format("%6.2f", m[mOffset + i + 4]) + "," +
                            String.format("%6.2f", m[mOffset + i + 8]) + "," +
                            String.format("%6.2f", m[mOffset + i + 12]) + " )\n"
                )
        }.toString()
    }

    fun caculateInvertedViewProjectionMatrix(
        invertedViewProjectionMatrix: FloatArray,
        viewMatrix: FloatArray,
        projectionMatrix: FloatArray
    ) {
        synchronized(sTemp) {
            // 视图转换：Multiply the view and projection matrices together
            xMultiplyMM(sTemp, 0, projectionMatrix, 0, viewMatrix, 0)
            // Create an inverted matrix for touch picking.
            invertM(invertedViewProjectionMatrix, 0, sTemp, 0)
        }
    }
}
