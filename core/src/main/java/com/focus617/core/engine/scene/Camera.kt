package com.focus617.core.engine.scene

import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.math.Point3D
import com.focus617.core.platform.base.BaseEntity

/**
 * Camera 抽象类
 * 负责管理 视图矩阵mViewMatrix：mViewMatrix
 */
abstract class Camera : BaseEntity() {
    // Camera本质就是两个Matrix——视图矩阵mViewMatrix和投影矩阵mProjectionMatrix
    // 投影矩阵mProjectionMatrix委托给了CameraController类
    protected val mViewMatrix = Mat4()

    // 相机的其它属性
    protected open var mPosition: Point3D = Point3D(0.0f, 0.0f, 0.0f)
    protected open var mRotationZAxis: Float = 90F //在XY平面绕Z轴的旋转角度

    abstract fun reCalculateViewMatrix()

    fun getViewMatrix(): Mat4 = mViewMatrix

    fun getPosition() = mPosition
    open fun setPosition(position: Point3D) {
        mPosition = position
        reCalculateViewMatrix()
    }

    // 相机位置不动，旋转directionUp
    abstract fun setRotation(rollZ: Float = 90f)
    fun getRotation() = mRotationZAxis

    companion object {
        const val U_VIEW_MATRIX = "u_ViewMatrix"
        const val U_PROJECT_MATRIX = "u_ProjectionMatrix"

    }
}