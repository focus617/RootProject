package com.focus617.app_demo.engine.d3

import android.opengl.GLES31
import com.focus617.app_demo.renderer.text.TextQuad3D
import com.focus617.app_demo.renderer.text.TextTexture2D
import com.focus617.app_demo.renderer.vertex.XGLVertexArray
import com.focus617.core.engine.core.Layer
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.math.Vector3
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventDispatcher

class TextLayer(name: String) : Layer(name) {
    private val eventDispatcher = EventDispatcher()
    private lateinit var textTexture: TextTexture2D

    init {
        val textQuad = TextQuad3D()
        textQuad.onTransform3D(
            Vector3(0f, 1.5f, 0f),
            Vector3(1.0f, 0.5f, 1.0f)
        )
        gameObjectList.add(textQuad)
    }

    override fun initOpenGlResource() {
        for (gameObject in gameObjectList) {
            gameObject.vertexArray = XGLVertexArray.buildVertexArray(gameObject)
        }

        textTexture = TextTexture2D(256, 256)
        TextQuad3D.textureIndex = textTexture.textureIndex

        textTexture.setText("Hello World!", 100f)
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
        GLES31.glEnable(GLES31.GL_CULL_FACE)
        textTexture.setText("你好，徐智勇!", 100f)
    }

    override fun afterDrawFrame() {
        //Enable Cull Back Face
        GLES31.glDisable(GLES31.GL_CULL_FACE)
    }

    override fun onEvent(event: Event): Boolean {
        return eventDispatcher.dispatch(event)
    }

    // 处理各种触屏事件，例如可能引起相机位置变化的事件
    private fun registerEventHandlers() {
    }

    private fun unRegisterEventHandlers() {
    }

}