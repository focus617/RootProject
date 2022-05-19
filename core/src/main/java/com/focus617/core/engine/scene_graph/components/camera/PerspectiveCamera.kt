package com.focus617.core.engine.scene_graph.components.camera

import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.XMatrix
import com.focus617.core.engine.math.degreeToRadians
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.scene_graph.NodeEntity
import com.focus617.core.engine.scene_graph.Transform
import com.focus617.core.platform.event.base.Event
import kotlin.math.cos
import kotlin.math.sin

class PerspectiveCamera : Camera() {
    override lateinit var mParent: NodeEntity

    override var mPosition: Point3D = Point3D(0.0f, 1.0f, defaultDistance)

    private val target: Point3D = Point3D(0.0f, 1.0f, 0.0f)
    private var mTargetDistance: Float = defaultDistance

    private var directionUp: Vector3 = worldUp
    private lateinit var directionFront: Vector3
    private lateinit var directionRight: Vector3

    init {
        setRotation(mRotationZAxisInDegree)
    }

    fun getDistance() = mTargetDistance
    fun setDistance(value: Float) {
        mTargetDistance = value
    }

    // 相机位置不动，旋转directionUp
    override fun setRotation(rollZInDegree: Float) {
        val angle: Float = degreeToRadians(rollZInDegree)
        //val angle: Float = rollZInDegree * (Math.PI / 180.0f).toFloat()

        directionUp.x = sin(angle)
        directionUp.y = cos(angle)
        directionUp.z = 0f
        reCalculateViewMatrix()
    }

    fun setRotation(pitchXInDegree: Float = 0f, yawYInDegree: Float = 90f) {
//        val pitchXInRadians: Float = pitchXInDegree * (Math.PI / 180.0f).toFloat()
//        val yawYInRadians: Float = yawYInDegree * (Math.PI / 180.0f).toFloat()
        val pitchXInRadians: Float = degreeToRadians(pitchXInDegree)
        val yawYInRadians: Float = degreeToRadians(yawYInDegree)

        mPosition.y = sin(pitchXInRadians) * mTargetDistance
        mPosition.x = cos(pitchXInRadians) * cos(yawYInRadians) * mTargetDistance
        mPosition.z = cos(pitchXInRadians) * sin(yawYInRadians) * mTargetDistance

        // also re-calculate the Right and Up vector
        directionFront = Vector3(mPosition, target).normalize()
        directionRight = directionFront.crossProduct(worldUp).normalize()
        // normalize the vectors, because their length gets closer to 0
        // the more you look up or down which results in slower movement.
        directionUp = directionRight.crossProduct(directionFront).normalize()

        reCalculateViewMatrix()
    }

    // 根据相机的空间位置和相机绕Z轴的旋转角度，重新计算相机的视图矩阵(View Matrix)
    override fun reCalculateViewMatrix() {
        val viewMatrixFloatArray = FloatArray(16)
        XMatrix.setLookAtM(
            viewMatrixFloatArray, 0,
            mPosition.x, mPosition.y, mPosition.z,
            target.x, target.y, target.z,
            directionUp.x, directionUp.y, directionUp.z
        )
        mViewMatrix.setValue(viewMatrixFloatArray)
    }

    override fun onEvent(event: Event): Boolean = false

    override fun onUpdate(timeStep: TimeStep, transform: Transform) {}

    override fun onRender(shader: Shader, transform: Transform) {}

    companion object {
        val worldUp = Vector3(0.0f, 1.0f, 0.0f)
        const val defaultDistance: Float = 3.0F
    }
}