package com.focus617.core.engine.ecs.mine.objlib

import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.XMatrix
import com.focus617.core.engine.math.quat.Quat
import com.focus617.core.platform.base.BaseEntity

enum class ProjectionType(val index: Int) {
    None(0),
    Perspective(1),
    Orthographic(2);

    override fun toString() = when (index) {
        0 -> "Undefined"
        1 -> "Perspective Camera"
        2 -> "Orthographic Camera"
        else -> "Unknown Camera"
    }
}

class Camera : BaseEntity() {
    // Camera本质就是两个Matrix——视图矩阵mViewMatrix和投影矩阵mProjectionMatrix
    var mProjectionMatrix = Mat4()
        private set
    var mViewMatrix = Mat4()
        private set
    var mProjectionType: ProjectionType = ProjectionType.Orthographic
        private set

    // 相机的内部属性
    var mPerspectiveFOVInDegree: Float = 45.0f
        private set
    var mPerspectiveNearClip: Float = 0.01f
        private set
    var mPerspectiveFarClip: Float = 1000.0f
        private set

    var mOrthographicSize: Float = 4.0f
        private set
    var mOrthographicNearClip: Float = -1.0f
        private set
    var mOrthographicFarClip: Float = 1.0f
        private set

    // Viewport size
    var mWidth: Int = 0
        private set
    var mHeight: Int = 0
        private set
    private var mAspectRatio: Float = 1.778f

    // 相机的外部几何设置， 原则：相机位置跟踪focusPoint
    var mFocusPoint: Point3D = Point3D(0.0f, 0.0f, 0.0f)
        private set
    var mDistance: Float = 1.0f
        private set
    var mEulerAngleInDegree: Vector3 = Vector3(0f, 0f, 0f)
        private set
    private var mPosition: Point3D = Point3D(0.0f, 0.0f, 0.0f)

    fun setProjectionType(projectionType: ProjectionType) {
        mProjectionType = projectionType
        reCalculateProjection()
    }

    fun setViewportSize(width: Int, height: Int) {
        check(width > 0 && height > 0) {
            "Wrong size: width=$width, height=$height"
        }

        mWidth = width
        mHeight = height
        mAspectRatio = mWidth.toFloat() / mHeight.toFloat()
        reCalculateProjection()
    }

    fun setOrthographic(size: Float, nearClip: Float, farClip: Float) {
        mProjectionType = ProjectionType.Orthographic
        setOrthographicSize(size)
        setOrthographicNearClip(nearClip)
        setOrthographicFarClip(farClip)
    }

    fun setPerspective(fovInDegree: Float, nearClip: Float, farClip: Float) {
        mProjectionType = ProjectionType.Perspective
        setPerspectiveVerticalFov(fovInDegree)
        setPerspectiveNearClip(nearClip)
        setPerspectiveFarClip(farClip)
    }

    fun setPerspectiveVerticalFov(fovInDegree: Float) {
        mPerspectiveFOVInDegree = fovInDegree
        reCalculateProjection()
    }

    fun setPerspectiveNearClip(nearClip: Float) {
        mPerspectiveNearClip = nearClip
        reCalculateProjection()
    }

    fun setPerspectiveFarClip(farClip: Float) {
        mPerspectiveFarClip = farClip
        reCalculateProjection()
    }

    fun setOrthographicSize(size: Float) {
        mOrthographicSize = size
        reCalculateProjection()
    }

    fun setOrthographicNearClip(nearClip: Float) {
        mOrthographicNearClip = nearClip
        reCalculateProjection()
    }

    fun setOrthographicFarClip(farClip: Float) {
        mOrthographicFarClip = farClip
        reCalculateProjection()
    }

    fun setFocusPoint(point: Point3D) {
        mFocusPoint = point
        reCalculateViewMatrix()
    }

    fun setDistance(distance: Float) {
        mDistance = distance
        reCalculateViewMatrix()
    }

    fun setRotation(eulerAngleInDegree: Vector3) {
        mEulerAngleInDegree = eulerAngleInDegree
        reCalculateViewMatrix()
    }

    private fun reCalculateProjection() {
        if (mProjectionType == ProjectionType.Perspective) {
            updatePerspectiveProjectionMatrix()
        } else {
            updateOrthographicProjectionMatrix()
        }
    }

    private fun updatePerspectiveProjectionMatrix() {
        LOG.info("PerspectiveCamera recalculate projectionMatrix.")
        val matrix = FloatArray(16)

        // 计算透视投影矩阵 (Project Matrix)
        XMatrix.perspectiveM(
            matrix,
            0,
            mPerspectiveFOVInDegree,
            mAspectRatio,
            mPerspectiveNearClip,
            mPerspectiveFarClip
        )
        mProjectionMatrix.setValue(matrix)
    }

    private fun updateOrthographicProjectionMatrix() {
        LOG.info("OrthographicCamera recalculate projectionMatrix.")
        val matrix = FloatArray(16)

        mAspectRatio = mWidth.toFloat() / mHeight.toFloat()
        val orthoLeft: Float = -mOrthographicSize * mAspectRatio * 0.5f
        val orthoRight: Float = mOrthographicSize * mAspectRatio * 0.5f
        val orthoTop: Float = mOrthographicSize * 0.5f
        val orthoBottom: Float = -mOrthographicSize * 0.5f

        // 计算正交投影矩阵 (Project Matrix)
        XMatrix.orthoM(
            matrix,
            0,
            orthoLeft,
            orthoRight,
            orthoBottom,
            orthoTop,
            mOrthographicNearClip,
            mOrthographicFarClip
        )
        mProjectionMatrix.setValue(matrix)
    }

    private fun reCalculateViewMatrix() {
        LOG.info("Camera recalculate viewMatrix.")
        mPosition = calculatePosition()
        val orientation = getOrientation()

        val translateMat4 = Mat4().translate(mPosition)
        val rotationMat4 = Quat.toMat4(orientation)

        mViewMatrix.setValue((translateMat4 * rotationMat4).invert())
    }


    private fun calculatePosition(): Point3D {
        val distance = getForwardDirection() * mDistance
        return Point3D(
            mFocusPoint.x - distance.x,
            mFocusPoint.y - distance.y,
            mFocusPoint.z - distance.z
        )
    }

    private fun getOrientation() = Quat(-mEulerAngleInDegree)

    private fun getUpDirection(): Vector3 =
        Quat.rotate(getOrientation(), Vector3(0.0f, 1.0f, 0.0f))

    private fun getForwardDirection(): Vector3 =
        Quat.rotate(getOrientation(), Vector3(0.0f, 0.0f, -1.0f))

    private fun getRightDirection(): Vector3 =
        Quat.rotate(getOrientation(), Vector3(1.0f, 0.0f, 0.0f))

}