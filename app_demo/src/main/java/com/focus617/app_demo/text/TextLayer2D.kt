package com.focus617.app_demo.text

import com.focus617.app_demo.engine.XGLDrawableObject
import com.focus617.core.engine.core.Layer
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.math.Vector2
import com.focus617.core.engine.math.XMatrix
import com.focus617.core.engine.resource.baseDataType.Color
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventDispatcher

class TextLayer2D(name: String) : Layer(name) {
    private val eventDispatcher = EventDispatcher()
    private val textQuad = TextQuad()

    init {
        textQuad.onTransform2D(
            Vector2(0f, 2.5f),
            Vector2(1.0f, 0.5f)
        )
        textQuad.textColor = Color.GOLD
        gameObjectList.add(textQuad)
    }

    override fun initOpenGlResource() {
        for (gameObject in gameObjectList) {
            (gameObject as XGLDrawableObject).initOpenGlResource()
        }
    }

    override fun close() {
        LOG.info("${this.mDebugName} closed")
        eventDispatcher.close()
    }

    override fun onAttach() {
        LOG.info("${this.mDebugName} onAttach()")
        registerEventHandlers()
    }

    override fun onDetach() {
        LOG.info("${this.mDebugName} onDetach")
        unRegisterEventHandlers()
    }

    override fun onUpdate(timeStep: TimeStep) {
    }

    override fun beforeDrawFrame() {
        //Enable Cull Back Face
        //GLES31.glEnable(GLES31.GL_CULL_FACE)
        textQuad.text = "Hello World！"
        textQuad.textFont = 200f
        textQuad.textColor = Color.RED
    }

    override fun afterDrawFrame() {
        //Enable Cull Back Face
        //GLES31.glDisable(GLES31.GL_CULL_FACE)
    }

    override fun onEvent(event: Event): Boolean {
        return eventDispatcher.dispatch(event)
    }

    // 处理各种触屏事件，例如可能引起相机位置变化的事件
    private fun registerEventHandlers() {
    }

    private fun unRegisterEventHandlers() {
    }

    companion object{
        val mProjectionMatrix = Mat4()
        var mZoomLevel: Float = 1.0f
        fun onWindowSizeChange(width: Int, height: Int) {
            val matrix = FloatArray(16)
            // 计算正交投影矩阵 (Project Matrix)
            // 默认绘制的区间在横轴[-1.7778f, 1.778f]，纵轴[-1, 1]之间
            if (width > height) {
                // Landscape
                val aspect: Float = width.toFloat() / height.toFloat()
                val ratio = aspect * mZoomLevel

                // 用ZoomLevel来表示top，因为拉近镜头时，ZoomLevel变大，而对应可见区域会变小
                XMatrix.orthoM(
                    matrix,
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
                val aspect: Float = height.toFloat() / width.toFloat()
                val ratio = aspect * mZoomLevel
                XMatrix.orthoM(
                    matrix,
                    0,
                    -mZoomLevel,
                    mZoomLevel,
                    -ratio,
                    ratio,
                    -1.0f,
                    1.0f
                )
            }
            mProjectionMatrix.setValue(matrix)
        }
    }
}

