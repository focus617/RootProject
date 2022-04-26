package com.focus617.core.engine.scene

import com.focus617.core.engine.math.Point3D
import com.focus617.core.platform.base.BaseEntity

abstract class Camera  : BaseEntity(){
    protected abstract var mPosition: Point3D

    protected var mProjectionMatrix = FloatArray(16)
    protected val mViewMatrix = FloatArray(16)

    abstract fun reCalculateViewMatrix()

    fun getProjectionMatrix(): FloatArray = mProjectionMatrix
    abstract fun setProjectionMatrix(width: Int, height: Int)

    fun getViewMatrix(): FloatArray = mViewMatrix
    fun getPosition() = mPosition

    open fun setPosition(position: Point3D) {
        mPosition = position
        reCalculateViewMatrix()
    }

    open fun setPosition(x: Float, y: Float, z: Float) {
        mPosition = Point3D(x, y, z)
        reCalculateViewMatrix()
    }

    // 相机位置不动，旋转directionUp
    abstract fun setRotation(rollZ: Float = 90f)


}