package com.focus617.core.engine.math.quat

import com.focus617.core.engine.math.Trigonometric
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.clamp
import kotlin.math.cos
import kotlin.math.sin
import kotlin.math.sqrt

object QuaternionTrigonometric {

    /** Returns euler angles, pitch as x, yaw as y, roll as z.
     * The result is expressed in radians.     */
    fun eulerAngles(a: Quat, res: Vector3): Vector3 {
        res.x = pitch(a)
        res.y = yaw(a)
        res.z = roll(a)
        return res
    }

    infix fun eulerAngles(a: Quat): Vector3 =
        eulerAngles(a, Vector3())


    /** Returns roll value of euler angles expressed in radians.    */
    infix fun roll(q: Quat): Float = Trigonometric.atan(
        2f * (q.x * q.y + q.w * q.z),
        q.w * q.w + q.x * q.x - q.y * q.y - q.z * q.z
    )

    /** Returns pitch value of euler angles expressed in radians.   */
    infix fun pitch(q: Quat): Float {
        val y = 2f * (q.y * q.z + q.w * q.x)
        val x = q.w * q.w - q.x * q.x - q.y * q.y + q.z * q.z
        return when {
            //avoid atan2(0,0) - handle singularity - Matiis
            y.equals(0f) && x.equals(0f) -> 2f * Trigonometric.atan(q.x, q.w)
            else -> Trigonometric.atan(y, x)
        }
    }

    /** Returns yaw value of euler angles expressed in radians. */
    infix fun yaw(q: Quat): Float =
        Trigonometric.asin(clamp(-2f * (q.x * q.z - q.w * q.y), -1f, 1f))


    /** Returns the quaternion rotation angle.  */
    fun angle(q: Quat): Float = Trigonometric.acos(q.w) * 2f

    /** Returns the q rotation axis.    */
    fun axis(q: Quat, res: Vector3): Vector3 {
        val tmp1 = 1f - q.w * q.w
        if (tmp1 <= 0f) {
            res.x = 0f
            res.y = 0f
            res.z = 1f
            return res
        }
        val tmp2 = 1f / sqrt(tmp1)
        res.x = q.x * tmp2
        res.y = q.y * tmp2
        res.z = q.z * tmp2
        return res
    }

    fun axis(q: Quat): Vector3 = axis(q, Vector3())

    /**
     * Build a quaternion from an angle main.and a normalized axis.
     */
    fun angleAxis(angle: Float, axisX: Float, axisY: Float, axisZ: Float, res: Quat): Quat {

        val a = angle * 0.5f
        val s = sin(a)

        res.w = cos(a)
        res.x = axisX * s
        res.y = axisY * s
        res.z = axisZ * s

        return res
    }

    fun angleAxis(angle: Float, axisX: Float, axisY: Float, axisZ: Float): Quat =
        angleAxis(angle, axisX, axisY, axisZ, Quat())

    fun angleAxis(angle: Float, axis: Vector3, res: Quat): Quat =
        angleAxis(angle, axis.x, axis.y, axis.z, res)

    fun angleAxis(angle: Float, axis: Vector3): Quat =
        angleAxis(angle, axis.x, axis.y, axis.z, Quat())
}
