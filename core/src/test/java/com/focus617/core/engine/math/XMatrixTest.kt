package com.focus617.core.engine.math

import com.focus617.core.engine.math.XMatrix.setIdentityM
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class XMatrixTest {

    @Test
    fun test_toString() {
        //Given
        val matrix: FloatArray = FloatArray(16)
        setIdentityM(matrix, 0)

        //When
        val str: String = XMatrix.toString(matrix, 0)

        //Then
        println(str)

        val expectedString = """
        (1.0,0.0,0.0,0.0)
        (0.0,1.0,0.0,0.0)
        (0.0,0.0,1.0,0.0)
        (0.0,0.0,0.0,1.0)""".trimIndent() + "\n"
        assertThat(str).isEqualTo(expectedString)
    }

    @Test
    fun `test xmultiplyMM using identity Matrix`() {
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
        println(str)

        val identityMatrix: FloatArray = FloatArray(16)
        setIdentityM(identityMatrix, 0)
        assertThat(result).isEqualTo(identityMatrix)
    }

    @Test
    fun `test xmultiplyMM using random Matrix`() {
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
        println(str)

        assertThat(result).isEqualTo(lhs)
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
        println(str)

        val result: FloatArray = FloatArray(16)
        //When
        val v:Boolean = XMatrix.invertM(result,0,origin,0)
        //Then
        assertThat(v).isTrue()
        str = XMatrix.toString(result, 0)
        println(str)

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