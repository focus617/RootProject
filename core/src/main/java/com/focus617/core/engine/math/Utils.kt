package com.focus617.core.engine.math

import kotlin.math.max
import kotlin.math.min

fun degreeToRadians(degree: Float): Float = (degree * Math.PI / 180f).toFloat()

fun radiansToDegree(radians: Float): Float = (radians * 180f / Math.PI).toFloat()

fun min(a: Char, b: Char): Char = if (a < b) a else b
fun min(a: Byte, b: Byte): Byte = min(a.toInt(), b.toInt()).toByte()
fun min(a: Short, b: Short): Short = min(a.toInt(), b.toInt()).toShort()

fun max(a: Char, b: Char): Char = if (a < b) b else a
fun max(a: Byte, b: Byte): Byte = max(a.toInt(), b.toInt()).toByte()
fun max(a: Short, b: Short): Short = max(a.toInt(), b.toInt()).toShort()

fun clamp(a: Float, min: Float, max: Float) = min(max(a, min), max)
fun clamp(a: Double, min: Double, max: Double) = min(max(a, min), max)
fun clamp(a: Byte, min: Byte, max: Byte) = min(max(a, min), max)
fun clamp(a: Int, min: Int, max: Int) = min(max(a, min), max)
fun clamp(a: Long, min: Long, max: Long) = min(max(a, min), max)
fun clamp(a: Short, min: Short, max: Short) = min(max(a, min), max)
fun clamp(a: Char, min: Char, max: Char) = min(max(a, min), max)

fun yawClamp(yaw: Float, min: Float = -180.0f, max: Float = 180.0f): Float {
    var degree: Float = yaw

    if (degree > max) degree -= 360.0f
    else if (degree < min) degree += 360.0f

    return degree
}

fun pitchClamp(pitch: Float, min: Float = -89.0f, max: Float = 89.0f): Float {
    var degree: Float = pitch

    if (degree > max) degree = max
    else if (degree < min) degree = min

    return degree
}

// 把被LongPress的正交空间中的点映射到三维空间的一条直线：直线的近端映射到
// 投影矩阵中定义的视椎体的近平面，直线的远端映射到视椎体的远平面。
fun convertNormalized2DPointToRayOld(
    invertedViewProjectionMatrix: FloatArray,
    normalizedX: Float,
    normalizedY: Float
): Ray {
    // We'll convert these normalized device coordinates into world-space
    // coordinates. We'll pick a point on the near and far planes, and draw a
    // line between them. To do this transform, we need to first multiply by
    // the inverse matrix, and then we need to undo the perspective divide.
    val nearPointNdc = floatArrayOf(normalizedX, normalizedY, -1f, 1f)
    val farPointNdc = floatArrayOf(normalizedX, normalizedY, 1f, 1f)

    val nearPointWorld = FloatArray(4)
    val farPointWorld = FloatArray(4)

    XMatrix.xMultiplyMV(
        nearPointWorld, 0, invertedViewProjectionMatrix, 0, nearPointNdc, 0
    )
    XMatrix.xMultiplyMV(
        farPointWorld, 0, invertedViewProjectionMatrix, 0, farPointNdc, 0
    )

    // Why are we dividing by W? We multiplied our vector by an inverse
    // matrix, so the W value that we end up is actually the *inverse* of
    // what the projection matrix would create. By dividing all 3 components
    // by W, we effectively undo the hardware perspective divide.
    divideByW(nearPointWorld)
    divideByW(farPointWorld)

    // We don't care about the W value anymore, because our points are now
    // in world coordinates.
    val nearPointRay = Point3D(nearPointWorld[0], nearPointWorld[1], nearPointWorld[2])
    val farPointRay = Point3D(farPointWorld[0], farPointWorld[1], farPointWorld[2])

    return Ray(
        nearPointRay,
        Vector3.vectorBetween(nearPointRay, farPointRay)
    )
}

private fun divideByW(vector: FloatArray) {
    vector[0] /= vector[3]
    vector[1] /= vector[3]
    vector[2] /= vector[3]
}


