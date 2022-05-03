package com.focus617.core.engine.scene

import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.XMatrix
import kotlin.math.cos
import kotlin.math.sin

class PerspectiveCamera : Camera() {
    private val target: Point3D = Point3D(0.0f, 0.0f, 0.0f)
    private val worldUp = Vector3(0.0f, 1.0f, 0.0f)
    private val defaultDistance: Float = 4.0F

    override var mPosition: Point3D = Point3D(0.0f, 0.0f, defaultDistance)
    private var mTargetDistance: Float = defaultDistance

    private var directionUp: Vector3 = worldUp
    private lateinit var directionFront: Vector3
    private lateinit var directionRight: Vector3

    init {
        setRotation(mRotationZAxis)
    }

    fun getDistance() = mTargetDistance
    fun setDistance(value: Float) {
        mTargetDistance = value
    }

    // 相机位置不动，旋转directionUp
    override fun setRotation(rollZInDegree: Float) {
        val angle: Float = rollZInDegree * (Math.PI / 180.0f).toFloat()

        directionUp.x = cos(angle)
        directionUp.y = sin(angle)
        directionUp.z = 0f
        reCalculateViewMatrix()
    }

    fun setRotation(pitchXInDegree: Float = 0f, yawYInDegree: Float = 90f) {
        val angleX: Float = pitchXInDegree * (Math.PI / 180.0f).toFloat()
        val angleY: Float = yawYInDegree * (Math.PI / 180.0f).toFloat()

        mPosition.y = sin(angleX) * mTargetDistance
        mPosition.x = cos(angleX) * cos(angleY) * mTargetDistance
        mPosition.z = cos(angleX) * sin(angleY) * mTargetDistance

        // also re-calculate the Right and Up vector
        directionFront = Vector3(mPosition, target).normalize()
        directionRight = directionFront.crossProduct(worldUp).normalize()
        // normalize the vectors, because their length gets closer to 0
        // the more you look up or down which results in slower movement.
        directionUp = directionRight.crossProduct(directionFront).normalize()

        reCalculateViewMatrix()
    }

    fun setRotationNotWork(pitchXInDegree: Float = 0f, yawYInDegree: Float = 90f) {
        val angleX: Float = pitchXInDegree * (Math.PI / 180.0f).toFloat()
        val angleY: Float = yawYInDegree * (Math.PI / 180.0f).toFloat()

        XMatrix.setIdentityM(mViewMatrix, 0)
        XMatrix.rotateM(mViewMatrix, 0, angleX, 1f, 0f, 0f)
        XMatrix.rotateM(mViewMatrix, 0, angleY, 0f, 1f, 0f)
        XMatrix.translateM(mViewMatrix, 0, 0f, -1.5f, -5f)
    }

    override fun reCalculateViewMatrix() {
        // 设置相机的位置，进而计算出视图矩阵 (View Matrix)
        XMatrix.setLookAtM(
            mViewMatrix, 0,
            mPosition.x, mPosition.y, mPosition.z,
            target.x, target.y, target.z,
            directionUp.x, directionUp.y, directionUp.z
        )
    }
}