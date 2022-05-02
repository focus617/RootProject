package com.focus617.core.engine.math

fun clamp(value: Int, min: Int, max: Int): Int {
    return kotlin.math.max(min, kotlin.math.min(max, value))
}

fun clamp(value: Float, min: Float, max: Float): Float {
    return kotlin.math.max(min, kotlin.math.min(max, value))
}

fun rotate(value: Float, min: Float = -180.0f, max: Float = 180.0f): Float {
    var degree: Float = value

    if (degree > max) degree -= 360.0f
    else if (degree < -min) degree += 360.0f

    return degree
}