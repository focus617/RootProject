package com.focus617.app_demo.text

import com.focus617.app_demo.engine.XGLDrawableObject
import com.focus617.core.engine.core.Layer
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.resource.baseDataType.Color
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventDispatcher

class TextLayer3D(name: String) : Layer(name) {
    private val eventDispatcher = EventDispatcher()
    private val textQuad1 = TextQuad()
    private val textQuad2 = TextQuad(false)

    init {
        textQuad1.onTransform3D(
            Vector3(0f, 1.5f, 0f),
            Vector3(1.0f, 0.5f, 1.0f)
        )
        textQuad1.textColor = Color.GOLD
        gameObjectList.add(textQuad1)

        textQuad2.onTransform3D(
            Vector3(-1.0f, 0.5f, 0.0f),
            Vector3(1.0f, 0.5f, 1.0f)
        )
        gameObjectList.add(textQuad2)
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
        textQuad1.text = "你好，徐智勇！"
        textQuad1.textFont = 200f

        textQuad2.text = "Hello World!"
        textQuad2.textFont = 100f
        textQuad2.textColor = Color.RED
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

}

