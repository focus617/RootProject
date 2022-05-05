package com.focus617.core.engine.math

import com.focus617.core.engine.math.XMatrix.setIdentityM
import com.focus617.mylib.logging.WithLogging
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class XMatrixTest : WithLogging() {

    @Test
    fun test_toString() {
        //Given
        val matrix: FloatArray = FloatArray(16)
        setIdentityM(matrix, 0)

        //When
        val str: String = XMatrix.toString(matrix, 0)

        //Then
        LOG.info(str)

        assertThat(str).contains("1.00,  0.00,  0.00,  0.00")
        assertThat(str).contains("0.00,  1.00,  0.00,  0.00")
        assertThat(str).contains("0.00,  0.00,  1.00,  0.00")
        assertThat(str).contains("0.00,  0.00,  0.00,  1.00")
    }

    @Test
    fun `test xMultiplyMM using identity Matrix`() {
        //Given
        val lhs: FloatArray = FloatArray(16)
        setIdentityM(lhs, 0)
        val rhs: FloatArray = FloatArray(16)
        setIdentityM(rhs, 0)

        //When
        val result = FloatArray(16)
        XMatrix.xMultiplyMM(result, 0, lhs, 0, rhs, 0)

        //Then
        val str = XMatrix.toString(result, 0)
        LOG.info(str)

        val identityMatrix: FloatArray = FloatArray(16)
        setIdentityM(identityMatrix, 0)
        assertThat(result).isEqualTo(identityMatrix)
    }

    @Test
    fun `test xMultiplyMM using random Matrix`() {
        //Given
        val lhs: FloatArray = listOf(
            1F, 2F, 3F, 4F,
            5F, 6F, 7F, 8F,
            9F, 10F, 11F, 12F,
            13F, 14F, 15F, 16F
        ).toFloatArray()

        val rhs: FloatArray = FloatArray(16)
        setIdentityM(rhs, 0)

        //When
        val result = FloatArray(16)
        XMatrix.xMultiplyMM(result, 0, lhs, 0, rhs, 0)

        //Then
        val str = XMatrix.toString(result, 0)
        LOG.info(str)

        assertThat(result).isEqualTo(lhs)
    }

    @Test
    fun `test xMultiplyMV using identity Matrix`() {
        //Given
        val lhsMat = FloatArray(16)
        setIdentityM(lhsMat, 0)
        val rhsVec = Vector4(1.0f, 1.0f, 1.0f, 1.0f)


        //When
        val result = FloatArray(4)
        XMatrix.xMultiplyMV(result, 0, lhsMat, 0, rhsVec.toFloatArray(), 0)

        //Then
        val str = result.toString()
        LOG.info(str)

        val resultVector4 = Vector4(result[0], result[1], result[2], result[3])
        assertThat(resultVector4).isEqualTo(rhsVec)
    }

    @Test
    fun `test xMultiplyMV using random Matrix`() {
        //Given
        val lhsMat = listOf(
            1F, 2F, 3F, 4F,
            5F, 6F, 7F, 8F,
            9F, 10F, 11F, 12F,
            13F, 14F, 15F, 16F
        ).toFloatArray()
        val rhsVec = Vector4(1.0f, 1.0f, 1.0f, 1.0f)

        //When
        val result = FloatArray(4)
        XMatrix.xMultiplyMV(result, 0, lhsMat, 0, rhsVec.toFloatArray(), 0)

        //Then
        val str = Vector4(result).toString()
        LOG.info(str)

        val resultVector4 = Vector4(result[0], result[1], result[2], result[3])
        val expectedVec = Vector4(28f, 32f, 36f, 40f)
        assertThat(resultVector4).isEqualTo(expectedVec)
    }

    @Test
    fun `test invertM`() {
        //Given
        val origin: FloatArray = listOf(
            1.0F, 0.0F, 0.0F, 0.0F,
            0.0F, 1.0F, 0.0F, 0.0F,
            0.0F, 0.0F, 1.0F, 0.0F,
            0.5F, 0.5F, 0.0F, 1.0F
        ).toFloatArray()
        var str = XMatrix.toString(origin, 0)
        LOG.info(str)

        val result: FloatArray = FloatArray(16)
        //When
        val v: Boolean = XMatrix.invertM(result, 0, origin, 0)
        //Then
        assertThat(v).isTrue()
        str = XMatrix.toString(result, 0)
        LOG.info(str)

        val verification: FloatArray = FloatArray(16)
        XMatrix.xMultiplyMM(verification, 0, origin, 0, result, 0)

        val identityMatrix: FloatArray = FloatArray(16)
        setIdentityM(identityMatrix, 0)
        assertThat(verification).isEqualTo(identityMatrix)
    }

    @Test
    fun template() {
        //Given
        //When
        //Then
    }

}