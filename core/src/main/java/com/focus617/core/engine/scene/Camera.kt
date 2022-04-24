package com.focus617.core.engine.scene

import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.XMatrix
import com.focus617.core.platform.base.BaseEntity
import kotlin.math.cos
import kotlin.math.sin

class Camera : BaseEntity() {
    private val target: Point3D = Point3D(0.0f, 0.0f, 0.0f)
    private val worldUp = Vector3(0.0f, 1.0f, 0.0f)
    private val defaultDistance: Float = 4.0F

    private var mPosition: Point3D = Point3D(0.0f, 0.0f, defaultDistance)
    private var mTargetDistance: Float = defaultDistance
    private var directionUp: Vector3 = worldUp

    private val mViewMatrix = FloatArray(16)

    private lateinit var directionFront: Vector3
    private lateinit var directionRight: Vector3

    init {
        reCalculateViewMatrix()
    }

    fun getPosition() = mPosition
    fun setPosition(x: Float, y: Float, z: Float) {
        mPosition = Point3D(x, y, z)
        reCalculateViewMatrix()
    }

    fun getDistance() = mTargetDistance
    fun setDistance(value: Float) {
        mTargetDistance = value
    }

    // 相机位置不动，旋转directionUp
    fun setRotation(rollZ: Float = 90f){
        directionUp.x = cos(rollZ)
        directionUp.y = sin(rollZ)
        directionUp.z = 0f
        reCalculateViewMatrix()
    }

    fun setRotation(pitchX: Float = 0f, yawY: Float = 90f) {
        mPosition.y = kotlin.math.sin(pitchX) * mTargetDistance
        mPosition.x = kotlin.math.cos(pitchX) * kotlin.math.cos(yawY) * mTargetDistance
        mPosition.z = kotlin.math.cos(pitchX) * kotlin.math.sin(yawY) * mTargetDistance

        // also re-calculate the Right and Up vector
        directionFront = Vector3(mPosition, target).normalize()
        directionRight = directionFront.crossProduct(worldUp).normalize()
        // normalize the vectors, because their length gets closer to 0
        // the more you look up or down which results in slower movement.
        directionUp = directionRight.crossProduct(directionFront).normalize()

        reCalculateViewMatrix()
    }

    fun getViewMatrix(): FloatArray = mViewMatrix

    private fun reCalculateViewMatrix() {
        // 设置相机的位置，进而计算出视图矩阵 (View Matrix)
        XMatrix.setLookAtM(
            mViewMatrix, 0,
            mPosition.x, mPosition.y, mPosition.z,
            target.x, target.y, target.z,
            directionUp.x, directionUp.y, directionUp.z
        )
    }
}