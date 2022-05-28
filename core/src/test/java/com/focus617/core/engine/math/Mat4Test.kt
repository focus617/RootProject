package com.focus617.core.engine.math

import com.focus617.mylib.logging.WithLogging
import com.google.common.truth.Truth
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class Mat4Test : WithLogging() {
    val testMat4 = Mat4()

    @Before
    fun setUp() {
        testMat4.setValue(
            floatArrayOf(
                0f, 1f, 2f, 3f,
                4f, 5f, 6f, 7f,
                8f, 9f, 10f, 11f,
                12f, 13f, 14f, 15f
            )
        )
    }

    @Test
    fun test_toString() {
        //When
        val str: String = testMat4.toString()

        //Then
        LOG.info(str)

        Truth.assertThat(str).contains("(  0.00,  4.00,  8.00, 12.00 )")
        Truth.assertThat(str).contains("(  1.00,  5.00,  9.00, 13.00 )")
        Truth.assertThat(str).contains("(  2.00,  6.00, 10.00, 14.00 )")
        Truth.assertThat(str).contains("(  3.00,  7.00, 11.00, 15.00 )")
    }

    @Test
    fun `test_setIdentity`() {
        // When
        testMat4.setIdentity()

        //Then
        LOG.info(testMat4.toString())

        val refArray: FloatArray = FloatArray(16)
        XMatrix.setIdentityM(refArray, 0)
        assertTrue(refArray.contentEquals(testMat4.toFloatArray()))
    }

    @Test
    fun `test_get`() {
        assertEquals(4.00f, testMat4[1, 0])
        assertEquals(11.00f, testMat4[2, 3])
    }

    @Test
    fun `test_set`() {
        testMat4[1, 2] = 100f
        //Then
        LOG.info(testMat4.toString())
        assertEquals(100f, testMat4[1, 2])
    }
}