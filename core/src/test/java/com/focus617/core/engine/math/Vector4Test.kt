package com.focus617.core.engine.math

import com.focus617.mylib.logging.WithLogging
import com.google.common.truth.Truth
import org.junit.Test

class Vector4Test : WithLogging() {

    @Test
    fun test_normalize() {
        // Given
        val vector = Vector4(1f, 2f, 3f, 4f)
        // When
        val result = vector.normalize()
        LOG.info(result.toString())
        //Then
        Truth.assertThat(result).isEqualTo(Vector4(0.25f, 0.5f, 0.75f, 1f))
    }

    @Test
    fun test_plus() {
        // Given
        val v1 = Vector4(1f, 2f, 3f, 2f)
        val v2 = Vector4(2f, 3f, 4f, 3f)
        // When
        val result = v1 + v2
        LOG.info(result.toString())
        //Then
        Truth.assertThat(result).isEqualTo(Vector4(0.6f, 1f, 1.4f, 1f))
    }
}