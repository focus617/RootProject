package com.focus617.core.engine.scene

import com.focus617.core.engine.math.XMatrix
import com.focus617.core.platform.base.BaseEntity

class CameraController(private var mCamera: Camera): BaseEntity() {
    private val mProjectionMatrix = FloatArray(16)
    private var mZoomLevel: Float = 1.0f
    private var mWidth: Int = 0
    private var mHeight: Int = 0

    fun getCamera() = mCamera

    fun getZoomLevel() = mZoomLevel
    fun setZoomLevel(level: Float) {
        mZoomLevel = level
        reCalculateOrthoGraphicProjectionMatrix()
        mCamera.setProjectionMatrix(mProjectionMatrix)
    }

    fun onWindowSizeChange(width: Int, height: Int){
        mWidth = width
        mHeight = height
        when(mCamera){
            is OrthographicCamera -> reCalculateOrthoGraphicProjectionMatrix()
            is PerspectiveCamera -> reCalculatePerspectiveProjectionMatrix()
        }
        mCamera.setProjectionMatrix(mProjectionMatrix)
    }

    private fun reCalculatePerspectiveProjectionMatrix() {
        // 计算透视投影矩阵 (Project Matrix)
        val ratio: Float = mWidth.toFloat() / mHeight.toFloat()
        XMatrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

    private fun reCalculateOrthoGraphicProjectionMatrix() {
        // 计算正交投影矩阵 (Project Matrix)
        if (mWidth > mHeight) {
            // Landscape
            val aspect: Float = mWidth.toFloat() / mHeight.toFloat()
            val ratio = aspect * mZoomLevel
            XMatrix.orthoM(
                mProjectionMatrix,
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
                mProjectionMatrix,
                0,
                -mZoomLevel,
                mZoomLevel,
                -ratio,
                ratio,
                -1.0f,
                1.0f
            )
        }
    }

}