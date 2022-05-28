package com.focus617.core.engine.ecs.mine

import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.XMatrix
import com.focus617.core.engine.math.quat.Quat

class NewCamera {
    // Camera本质就是两个Matrix——视图矩阵mViewMatrix和投影矩阵mProjectionMatrix
    protected val mProjectionMatrix = Mat4()
    protected val mViewMatrix = Mat4()

    private var isProjectionMatrixDirty: Boolean = true
    fun isProjectionMatrixDirty() = isProjectionMatrixDirty
    fun setProjectionMatrixDirty() {
        isProjectionMatrixDirty = true
    }

    private var isViewMatrixDirty: Boolean = true
    fun isViewMatrixDirty() = isViewMatrixDirty
    fun setViewMatrixDirty() {
        isViewMatrixDirty = true
    }

    // 相机的其它属性
    protected var mPosition: Point3D = Point3D(0.0f, 0.0f, 0.0f)
    protected var mFocusPoint: Point3D = Point3D(0.0f, 0.0f, 0.0f)
    protected var mDistance: Float = 10.0f
    protected var mEulerAngleInDegree: Vector3 = Vector3(0f, 0f, 0f)

    private var mFOVInDegree: Float = 45.0f
    private var mAspectRatio: Float = 1.778f
    private var mNearClip: Float = 0.1f
    private var mFarClip: Float = 1000.0f

    // Viewport size
    private var mWidth: Int = 0
    private var mHeight: Int = 0


    private fun updatePerspectiveProjectionMatrix() {
        val matrix = FloatArray(16)

        mAspectRatio = mWidth.toFloat() / mHeight.toFloat()

        // 计算透视投影矩阵 (Project Matrix)
        XMatrix.perspectiveM(matrix, 0, mFOVInDegree, mAspectRatio, mNearClip, mFarClip)

        mProjectionMatrix.setValue(matrix)
    }

    fun updateViewMatrix(){
        mPosition = calculatePosition()
        val orientation = getOrientation()

        val translateMat4 = Mat4().translate(mPosition)
        val rotationMat4 = Quat.toMat4(orientation)

        mViewMatrix.setValue((translateMat4 * rotationMat4).invert())
    }

    fun calculatePosition(): Point3D{
        val distance = getForwardDirection() * mDistance
        return Point3D(
            mFocusPoint.x - distance.x,
            mFocusPoint.y - distance.y,
            mFocusPoint.z - distance.z
        )
    }

    fun getOrientation() = Quat(-mEulerAngleInDegree)

    fun getUpDirection(): Vector3 =
        Quat.rotate(getOrientation(), Vector3(0.0f, 1.0f, 0.0f))

    fun getForwardDirection(): Vector3 =
        Quat.rotate(getOrientation(), Vector3(0.0f, 0.0f, -1.0f))

    fun getRightDirection(): Vector3 =
        Quat.rotate(getOrientation(), Vector3(1.0f, 0.0f, 0.0f))

}