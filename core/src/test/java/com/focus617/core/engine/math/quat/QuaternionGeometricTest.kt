package com.focus617.core.engine.math.quat

import com.focus617.core.engine.math.Vector3
import org.junit.Assert.assertEquals
import org.junit.Test

class QuaternionGeometricTest {

    @Test
    fun `Test Quat size`() {
        assertEquals(4, Quat.length)
        assertEquals(16, Quat.size)
    }

    @Test
    fun `Test Quat length`() {
        assertEquals(1f, Quat(1, 0, 0, 0).length())
        assertEquals(1f, Quat(1f, Vector3(0)).length())
    }

    @Test
    fun `Test Quat normalize`() {
        assertEquals(Quat(1, 0, 0, 0), Quat(1, 0, 0, 0).normalize())
        assertEquals(Quat(1, 0, 0, 0), Quat(1f, Vector3(0)).normalize())
    }

    @Test
    fun `Test Quat dot`() {
        val A = Quat(1, 0, 0, 0)
        val B = Quat(1, 0, 0, 0)
        val C = A dot B
        assertEquals(1f, C)
    }




}