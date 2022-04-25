package com.focus617.core.engine.math

import com.focus617.mylib.logging.WithLogging
import com.google.common.truth.Truth.assertThat
import org.junit.Test

class Point3DTest : WithLogging() {
    @Test
    fun test_plus_point() {
        // Given
        val pointA = Point3D(0F, 1F, 2F)
        val pointB = Point3D(2F, -1F, 5F)
        // When
        val result = pointA + pointB
        LOG.info(result.toString())
        // Then
        assertThat(result).isEqualTo(Point3D(1F, 0F, 3.5F))
    }

    @Test
    fun test_plus_vector() {
        // Given
        val point = Point3D(0F, 1F, 2F)
        val translation = Vector3(1F, 0F, 5F)
        // When
        val result = point + translation
        // Then
        assertThat(result).isEqualTo(Point3D(1F, 1F, 7F))
    }

    @Test
    fun test_minus() {
        // Given
        val pointA = Point3D(0F, 1F, 2F)
        val pointB = Point3D(2F, -1F, 5F)
        // When
        val result = pointA - pointB
        // Then
        assertThat(result).isEqualTo(Vector3(-2.0F, 2.0F, -3.0F))
    }

}