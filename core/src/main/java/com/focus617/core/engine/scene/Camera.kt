package com.focus617.core.engine.scene

import com.focus617.core.engine.math.Point3D
import com.focus617.core.platform.base.BaseEntity

abstract class Camera : BaseEntity() {
    // Camera本质就是两个Matrix——视图矩阵mViewMatrix和投影矩阵mProjectionMatrix
    protected var mProjectionMatrix = FloatArray(16)
    protected val mViewMatrix = FloatArray(16)

    // 相机的其它属性
    protected open var mPosition: Point3D = Point3D(0.0f, 0.0f, 0.0f)
    protected open var mRotationZAxis: Float = 90F //在XY平面绕Z轴的旋转角度

    abstract fun reCalculateViewMatrix()

    fun getProjectionMatrix(): FloatArray = mProjectionMatrix
    fun setProjectionMatrix(matrix: FloatArray) {
        System.arraycopy(matrix, 0, mProjectionMatrix, 0, 16)
    }

    fun getViewMatrix(): FloatArray = mViewMatrix

    fun getPosition() = mPosition
    open fun setPosition(position: Point3D) {
        mPosition = position
        reCalculateViewMatrix()
    }

    // 相机位置不动，旋转directionUp
    abstract fun setRotation(rollZ: Float = 90f)
    fun getRotation() = mRotationZAxis

}