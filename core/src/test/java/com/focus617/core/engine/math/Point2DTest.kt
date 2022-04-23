package com.focus617.core.engine.math

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class Point2DTest {

    @Test
    fun test_isAtLeftOfVector() {
        // Given
        val pointLeft = Point2D(0F, 1F)
        val pointRight = Point2D(1F, 0F)
        val pointAtLine = Point2D(1F, 1F)
        // When
        val resultLeft = pointLeft.isAtLeftOfVector(Point2D(0F, 0F), Point2D(1F, 1F))
        val resultRight = pointRight.isAtLeftOfVector(Point2D(0F, 0F), Point2D(1F, 1F))
        val resultAtLine = pointAtLine.isAtLeftOfVector(Point2D(0F, 0F), Point2D(1F, 1F))
        // Then
        assertTrue(resultLeft)
        assertFalse(resultRight)
        assertFalse(resultAtLine)
    }

    @Test
    fun test_isInsideTriangle() {
        // Given
        val triangle2D = Triangle2D(Point2D(1f,0f), Point2D(0f,1f), Point2D(-1f, 0f))
        val pointInside = Point2D(0f, 0.5F)
        val pointOutside = Point2D(1f, 1f)
        val pointAtLine = Point2D(0f, 0f)
        // When
        val resultInside = pointInside.isInsideTriangle(triangle2D)
        val resultOutside = pointOutside.isInsideTriangle(triangle2D)
        val resultAtLine = pointAtLine.isInsideTriangle(triangle2D)
        // Then
        assertTrue(resultInside)
        assertFalse(resultOutside)
        assertTrue(resultAtLine)
    }
}