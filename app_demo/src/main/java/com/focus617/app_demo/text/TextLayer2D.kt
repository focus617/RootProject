package com.focus617.app_demo.text

import com.focus617.app_demo.engine.XGLDrawableObject
import com.focus617.core.engine.core.Layer
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventDispatcher

class TextLayer2D(name: String) : Layer(name) {
    private val eventDispatcher = EventDispatcher()

    init {

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

