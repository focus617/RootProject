package com.focus617.core.engine.scene_graph.components.camera

import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.scene_graph.IfComponent
import com.focus617.core.platform.base.BaseEntity

/**
 * CameraController抽象类
 * 负责管理 投影矩阵mProjectionMatrix：mProjectionMatrix
 */
abstract class CameraController(protected val mCamera: Camera) : BaseEntity(), IfComponent {
    fun getCamera() = mCamera

    protected val mProjectionMatrix = Mat4()
    fun getProjectionMatrix(): Mat4 = mProjectionMatrix

    abstract var mZoomLevel: Float

    // Viewport size
    abstract var mWidth: Int
    abstract var mHeight: Int

    abstract fun onWindowSizeChange(width: Int, height: Int)

    abstract fun setPosition(x: Float, y: Float, z: Float)

    override fun close() {}
}