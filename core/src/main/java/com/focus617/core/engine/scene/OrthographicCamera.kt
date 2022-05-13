package com.focus617.core.engine.scene

import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.math.Vector2

class OrthographicCamera : Camera() {

    override var mPosition: Point3D = Point3D(0f, 0f, 0f)
    override var mRotationZAxisInDegree: Float = 0F // 相机绕Z轴的旋转角度

    init {
        reCalculateViewMatrix()
    }

    override fun setRotation(rollZInDegree: Float) {
        mRotationZAxisInDegree = rollZInDegree
        reCalculateViewMatrix()
    }

    // 根据相机的空间位置和相机绕Z轴的旋转角度，重新计算相机的视图矩阵
    override fun reCalculateViewMatrix() {
        mViewMatrix
            .setIdentity()
            .transform2D(mPosition.toVector3(), Vector2(1.0f, 1.0f), mRotationZAxisInDegree)
    }

}