package com.focus617.core.engine.scene

import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.math.XMatrix

class OrthographicCamera : Camera() {

    override var mPosition: Point3D = Point3D(0f, 0f, 0f)
    override var mRotationZAxis: Float = 0F

    init {
        reCalculateViewMatrix()
    }

    override fun setRotation(rollZ: Float) {
        mRotationZAxis = rollZ
        reCalculateViewMatrix()
    }

    override fun reCalculateViewMatrix() {
        // 计算相机的空间移动
        val translateMatrix = FloatArray(16)
        XMatrix.setIdentityM(translateMatrix, 0)
        with(mPosition) {
            XMatrix.translateM(translateMatrix, 0, x, y, z)
        }
        // 计算相机的旋转(绕Z轴)
        val rotateMatrix = FloatArray(16)
        XMatrix.setRotateM(rotateMatrix, 0, mRotationZAxis, 0f, 0f, 1.0f)

        val transformMatrix = FloatArray(16)
        XMatrix.xMultiplyMM(transformMatrix, 0, translateMatrix, 0, rotateMatrix, 0)

        // 通过记录相机的transformation矩阵，然后取逆矩阵，就可以得到对应的View矩阵
        XMatrix.invertM(mViewMatrix, 0, transformMatrix, 0)
    }

}