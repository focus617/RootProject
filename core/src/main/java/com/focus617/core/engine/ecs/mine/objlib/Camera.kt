package com.focus617.core.engine.ecs.mine.objlib

import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.XMatrix
import com.focus617.core.engine.math.quat.Quat
import com.focus617.core.platform.base.BaseEntity

enum class ProjectionType(val index: Int) {
    Perspective(1),
    Orthographic(2)
}

class Camera : BaseEntity() {
    // Camera本质就是两个Matrix——视图矩阵mViewMatrix和投影矩阵mProjectionMatrix
    private val mProjectionMatrix = Mat4()
    private val mViewMatrix = Mat4()

    var mProjectionType: ProjectionType = ProjectionType.Perspective
        private set
    var isProjectionMatrixDirty: Boolean = true
        private set
    var isViewMatrixDirty: Boolean = true
        private set

    fun setProjectionMatrixDirty() {
        isProjectionMatrixDirty = true
    }

    fun setViewMatrixDirty() {
        isViewMatrixDirty = true
    }

    fun setProjectionType(projectionType: ProjectionType) {
        mProjectionType = projectionType
    }

    fun getProjectionMatrix(): Mat4 {
//        if (isProjectionMatrixDirty) {
        when (mProjectionType) {
            ProjectionType.Perspective -> updatePerspectiveProjectionMatrix()
            ProjectionType.Orthographic -> updateOrthographicProjectionMatrix()
        }
//        isProjectionMatrixDirty = false
//        }
        return mProjectionMatrix
    }

    fun getViewMatrix(): Mat4 {
//        if (isViewMatrixDirty) {
        updateViewMatrix()
//        isViewMatrixDirty = false
//        }
        return mViewMatrix
    }

    // 相机的内部属性
    var mPerspectiveFOVInDegree: Float = 45.0f
        private set
    var mPerspectiveNearClip: Float = 0.01f
        private set
    var mPerspectiveFarClip: Float = 1000.0f
        private set

    var mOrthographicSize: Float = 10.0f
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
    var mDistance: Float = 10.0f
        private set
    var mEulerAngleInDegree: Vector3 = Vector3(0f, 0f, 0f)
        private set
    private var mPosition: Point3D = Point3D(0.0f, 0.0f, 0.0f)

    fun setViewportSize(width: Int, height: Int) {
        mWidth = width
        mHeight = height
        mAspectRatio = mWidth.toFloat() / mHeight.toFloat()
        setProjectionMatrixDirty()
    }

    fun setOrthographic(size: Float, nearClip: Float, farClip: Float) {
        setOrthographicSize(size)
        setOrthographicNearClip(nearClip)
        setOrthographicFarClip(farClip)
    }

    fun setPerspective(fovInDegree: Float, nearClip: Float, farClip: Float) {
        setPerspectiveVerticalFov(fovInDegree)
        setPerspectiveNearClip(nearClip)
        setPerspectiveFarClip(farClip)
    }

    fun setPerspectiveVerticalFov(fovInDegree: Float) {
        mPerspectiveFOVInDegree = fovInDegree
        setProjectionMatrixDirty()
    }

    fun setPerspectiveNearClip(nearClip: Float) {
        mPerspectiveNearClip = nearClip
        setProjectionMatrixDirty()
    }

    fun setPerspectiveFarClip(farClip: Float) {
        mPerspectiveFarClip = farClip
        setProjectionMatrixDirty()
    }

    fun setOrthographicSize(size: Float) {
        mOrthographicSize = size
        setProjectionMatrixDirty()
    }

    fun setOrthographicNearClip(nearClip: Float) {
        mOrthographicNearClip = nearClip
        setProjectionMatrixDirty()
    }

    fun setOrthographicFarClip(farClip: Float) {
        mOrthographicFarClip = farClip
        setProjectionMatrixDirty()
    }

    fun setFocusPoint(point: Point3D) {
        mFocusPoint = point
        setViewMatrixDirty()
    }

    fun setDistance(distance: Float) {
        mDistance = distance
        setViewMatrixDirty()
    }

    fun setRotation(eulerAngleInDegree: Vector3) {
        mEulerAngleInDegree = eulerAngleInDegree
        setViewMatrixDirty()
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

//        mAspectRatio = mWidth.toFloat() / mHeight.toFloat()
//        val orthoLeft: Float = -mOrthographicSize * mAspectRatio * 0.5f
//        val orthoRight: Float = mOrthographicSize * mAspectRatio * 0.5f
//        val orthoTop: Float = mOrthographicSize * 0.5f
//        val orthoBottom: Float = -mOrthographicSize * 0.5f
//
//        // 计算正交投影矩阵 (Project Matrix)
//        XMatrix.orthoM(
//            matrix,
//            0,
//            orthoLeft,
//            orthoRight,
//            orthoBottom,
//            orthoTop,
//            mOrthographicNearClip,
//            mOrthographicFarClip
//        )
//        mProjectionMatrix.setValue(matrix)

        var mZoomLevel: Float = 0.5f
        // 计算正交投影矩阵 (Project Matrix)
        // 默认绘制的区间在横轴[-1.7778f, 1.778f]，纵轴[-1, 1]之间
        if (mWidth > mHeight) {
            // Landscape
            val aspect: Float = mWidth.toFloat() / mHeight.toFloat()
            val ratio = aspect * mZoomLevel
            // 用ZoomLevel来表示top，因为拉近镜头时，ZoomLevel变大，而对应可见区域会变小
            XMatrix.orthoM(
                matrix,
                0,
                -ratio,
                ratio,
                -mZoomLevel,
                mZoomLevel,
                -1.0f,
                1.0f
            )
        } else {
            // Portrait or Square
            val aspect: Float = mHeight.toFloat() / mWidth.toFloat()
            val ratio = aspect * mZoomLevel
            XMatrix.orthoM(
                matrix,
                0,
                -mZoomLevel,
                mZoomLevel,
                -ratio,
                ratio,
                -1.0f,
                1.0f
            )
        }
        mProjectionMatrix.setValue(matrix)
    }

    private fun updateViewMatrix() {
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