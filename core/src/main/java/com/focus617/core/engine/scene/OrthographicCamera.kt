package com.focus617.core.engine.scene

import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.math.XMatrix

class OrthographicCamera : Camera() {

    override var mPosition: Point3D = Point3D(0f, 0f, 0f)
    private var mRotation: Float = 0F

    init {
        reCalculateViewMatrix()
    }

    fun getRotation() = mRotation
    override fun setRotation(rollZ: Float) {
        mRotation = rollZ
        reCalculateViewMatrix()
    }

    override fun setProjectionMatrix(width: Int, height: Int) {
        // 计算正交投影矩阵 (Project Matrix)

        if (width > height) {
            // Landscape
            val ratio: Float = width.toFloat() / height.toFloat()
            XMatrix.orthoM(mProjectionMatrix, 0, -ratio, ratio, -1.0f, 1.0f, -1.0f, 1.0f)
        } else {
            // Portrait or Square
            val ratio: Float = height.toFloat() / width.toFloat()
            XMatrix.orthoM(mProjectionMatrix, 0, -1.0f, 1.0f, -ratio, ratio, -1.0f, 1.0f)
        }
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
        XMatrix.setRotateM(rotateMatrix, 0, mRotation, 0f, 0f, 1.0f)

        val transformMatrix = FloatArray(16)
        XMatrix.xMultiplyMM(transformMatrix, 0, translateMatrix, 0, rotateMatrix, 0)

        XMatrix.invertM(mViewMatrix, 0, transformMatrix, 0)
    }

}