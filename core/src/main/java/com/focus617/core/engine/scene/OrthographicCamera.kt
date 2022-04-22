package com.focus617.core.engine.scene

import com.focus617.core.engine.math.Point
import com.focus617.core.engine.math.XMatrix

class OrthographicCamera(left: Float, right: Float, bottom: Float, top: Float) {

    private var mPosition: Point = Point(0f, 0f, 0f)
    private var mRotation: Float = 0F

    private var mProjectionMatrix = FloatArray(16)
    private val mViewMatrix = FloatArray(16)
    private val mViewProjectionMatrix = FloatArray(16)

    init {
        XMatrix.orthoM(mProjectionMatrix, 0, left, right, bottom, top, -1.0f, 1.0f)
        XMatrix.setIdentityM(mViewMatrix, 0)

        // 视图转换：计算视图投影矩阵VPXMatrix，该矩阵可以将模型空间的坐标转换为归一化设备空间坐标
        XMatrix.xMultiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0)
    }

    fun getPosition() = mPosition
    fun setPosition(position: Point) {
        mPosition = position
        reCalculateViewMatrix()
    }

    fun getRotation() = mRotation
    fun setRotation(rotation: Float) {
        mRotation = rotation
        reCalculateViewMatrix()
    }

    fun getViewProjectionMatrix(): FloatArray = mViewProjectionMatrix

    private fun reCalculateViewMatrix() {
        // 计算相机的空间移动
        val translateMatrix = FloatArray(16)
        XMatrix.setIdentityM(translateMatrix, 0)
        with(mPosition) {
            XMatrix.translateM(translateMatrix, 0, x, y, z)
        }
        // 计算相机的旋转
        val rotateMatrix = FloatArray(16)
        XMatrix.rotateM(rotateMatrix, 0, mRotation, 0f, 0f, 1.0f)

        val transformMatrix = FloatArray(16)
        XMatrix.xMultiplyMM(transformMatrix, 0, translateMatrix, 0, rotateMatrix, 0)

        XMatrix.invertM(mViewMatrix, 0, transformMatrix, 0)

        // 视图转换：计算视图投影矩阵VPXMatrix，该矩阵可以将模型空间的坐标转换为归一化设备空间坐标
        XMatrix.xMultiplyMM(mViewProjectionMatrix, 0, mProjectionMatrix, 0, mViewMatrix, 0)

    }

}