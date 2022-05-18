package com.focus617.core.engine.scene_graph.components.camera

import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.scene_graph.IfComponent
import com.focus617.core.platform.base.BaseEntity

/**
 * Camera 抽象类
 * 负责管理 视图矩阵mViewMatrix：mViewMatrix
 */
abstract class Camera : BaseEntity(), IfComponent {
    // Camera本质就是两个Matrix——视图矩阵mViewMatrix和投影矩阵mProjectionMatrix
    // 投影矩阵mProjectionMatrix委托给了CameraController类
    protected val mViewMatrix = Mat4()

    // 相机的其它属性
    protected open var mPosition: Point3D = Point3D(0.0f, 0.0f, 0.0f)
    protected open var mRotationZAxisInDegree: Float = 90F //在XY平面绕Z轴的旋转角度

    // 根据相机的空间位置和相机绕Z轴的旋转角度，重新计算相机的视图矩阵
    abstract fun reCalculateViewMatrix()

    fun getViewMatrix(): Mat4 = mViewMatrix

    fun getPosition() = mPosition
    open fun setPosition(position: Point3D) {
        mPosition = position
        reCalculateViewMatrix()
    }

    // 相机位置不动，旋转directionUp
    abstract fun setRotation(rollZInDegree: Float = 90f)
    fun getRotation() = mRotationZAxisInDegree

}