package com.focus617.core.engine.scene

import com.focus617.core.engine.math.Matrix
import com.focus617.core.engine.math.Point
import com.focus617.core.engine.math.Vector

class Camera(var x: Float, var y: Float, var z: Float) {
    private val worldUp = Vector(0.0f, 1.0f, 0.0f)

    private var mPosition: Point = Point(x, y, z)
    private var directionUp: Vector = worldUp
    private val targetDir: Vector = Vector(0.0f, 0.0f, 0.0f)

    private lateinit var directionFront: Vector
    private lateinit var directionRight: Vector
    private var targetDistance: Float = 150F

    fun rotate(pitch: Float = 0f, yaw: Float = 90f) {
        mPosition.y = kotlin.math.sin(pitch) * targetDistance
        mPosition.x = kotlin.math.cos(pitch) * kotlin.math.cos(yaw) * targetDistance
        mPosition.z = kotlin.math.cos(pitch) * kotlin.math.sin(yaw) * targetDistance

        // also re-calculate the Right and Up vector
        directionFront = Vector(mPosition, targetDir).normalize()
        directionRight = directionFront.crossProduct(worldUp).normalize()
        // normalize the vectors, because their length gets closer to 0
        // the more you look up or down which results in slower movement.
        directionUp = directionRight.crossProduct(directionFront).normalize()
    }

    fun lookAt(): FloatArray {
        val mViewMatrix = FloatArray(16)

        // 设置相机的位置，进而计算出视图矩阵 (View Matrix)
        Matrix.setLookAtM(mViewMatrix, 0,
            mPosition.x, mPosition.y, mPosition.z,
            targetDir.x, targetDir.y, targetDir.z,
            directionUp.x, directionUp.y, directionUp.z)

        return mViewMatrix
    }
}