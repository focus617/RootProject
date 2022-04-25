package com.focus617.core.engine.math

import com.focus617.core.engine.math.Vector3.Companion.angleBetween
import com.focus617.mylib.logging.WithLogging
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class Vector3Test : WithLogging() {
    @Test
    fun test_equals(){
        // Given
        val v1 = Vector3(1f, 2f, 3f)
        val v2 = Vector3(2f, 3f, 4f)
        val v3 = Vector3(2f, 3f, 4f)
        val p = Point3D(2f, 3f, 4f)
        // When

        //Then
        assertThat(v1.equals(p)).isFalse()
        assertThat(v1==v2).isFalse()
        assertThat(v3==v2).isTrue()
    }

    @Test
    fun test_plus() {
        // Given
        val v1 = Vector3(1f, 2f, 3f)
        val v2 = Vector3(2f, 3f, 4f)
        // When
        val result = v1 + v2
        //Then
        assertThat(result).isEqualTo(Vector3(3f, 5f, 7f))
    }

    @Test
    fun test_minus() {
        // Given
        val v1 = Vector3(1f, 2f, 3f)
        val v2 = Vector3(2f, 3f, 4f)
        // When
        val result = v2 - v1
        //Then
        assertThat(result).isEqualTo(Vector3(1f, 1f, 1f))
    }

    @Test
    fun test_dotProduct(){
        // Given
        val v1 = Vector3(1f, 2f, 3f)
        val v2 = Vector3(2f, 3f, 4f)
        // When
        val result = v1.dotProduct(v2)
        //Then
        assertThat(result).isEqualTo(20F)
    }

    @Test
    fun test_angleBetween(){
        // Given
        val v1 = Vector3(1f, 0f, 0f)
        val v2 = Vector3(0f, 1f, 0f)
        // When
        val result = angleBetween(v1, v2)
        //Then
        assertThat(result).isEqualTo(90F)
    }
}