package com.focus617.core.engine.math

import kotlin.math.atan2
import kotlin.math.acos as _acos
import kotlin.math.asin as _asin
import kotlin.math.atan as _atan
import kotlin.math.cos as _cos
import kotlin.math.sin as _sin
import kotlin.math.tan as _tan

object Trigonometric {
    fun sin(angle: Double) = _sin(angle)
    fun sin(angle: Float) = _sin(angle.toDouble()).toFloat()

    fun cos(angle: Double) = _cos(angle)
    fun cos(angle: Float) = _cos(angle.toDouble()).toFloat()

    fun tan(angle: Double) = _tan(angle)
    fun tan(angle: Float) = _tan(angle.toDouble()).toFloat()

    fun acos(angle: Double) = _acos(angle)
    fun acos(angle: Float) = _acos(angle.toDouble()).toFloat()

    fun asin(angle: Double) = _asin(angle)
    fun asin(angle: Float) = _asin(angle.toDouble()).toFloat()

    fun atan(angle: Double) = _atan(angle)
    fun atan(angle: Float) = _atan(angle.toDouble()).toFloat()


    fun cos(angle: Vector4) = Vector4(cos(angle.x), cos(angle.y), cos(angle.z), cos(angle.w))

    fun atan(y: Double, x: Double) = atan2(y, x)
    fun atan(y: Float, x: Float) = atan2(y.toDouble(), x.toDouble()).toFloat()

    fun degreeToRadians(degree: Float): Float = (degree * Math.PI / 180f).toFloat()
    fun radiansToDegree(radians: Float): Float = (radians * 180f / Math.PI).toFloat()
}