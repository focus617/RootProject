package com.focus617.app_demo.text

import com.focus617.app_demo.renderer.framebuffer.XGLTexture2DBuffer
import com.focus617.core.engine.core.Layer
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.renderer.framebuffer.FrameBufferTextureFormat
import com.focus617.core.engine.resource.baseDataType.Color
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventDispatcher
import com.focus617.platform.helper.BitmapHelper
import com.focus617.platform.helper.BitmapHelper.convert

/**
 * TextLayer2D is often used for screen text showing on overlayer
 */
class TextLayer2D(name: String) : Layer(name) {
    private val eventDispatcher = EventDispatcher()
    private val mQuad: TextEntity2D = TextEntity2D()

    private var welcome = "欢迎访问我的虚拟世界！"
    private var isDirty = true

    fun setWelcome(words: String){
        welcome = words
        isDirty = true
    }

    private fun refreshWelcome() {
        if(isDirty) {
            val bitmap =
                BitmapHelper.createBitmap(welcome, 50f).convert(1f, -1f)
            texture!!.setData(
                bitmap,
                screenWidth - bitmap.width - 20,
                screenHeight - bitmap.height - 20
            )
            bitmap.recycle()
            isDirty = false
        }
    }

    override fun initOpenGlResource() {
        mQuad.initOpenGlResource()
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
        texture?.apply {
            mQuad.fontColor = Color.CYAN

            refreshWelcome()

            TextEntity2D.shaderWithColor.bind()
            TextEntity2D.shaderWithColor.setInt(
                TextEntity2D.U_TEXTURE, texture!!.screenTextureIndex
            )
            mQuad.onRender(TextEntity2D.shaderWithColor)
        }
    }



    override fun afterDrawFrame() {
    }

    override fun onEvent(event: Event): Boolean {
        return eventDispatcher.dispatch(event)
    }

    // 处理各种触屏事件，例如可能引起相机位置变化的事件
    private fun registerEventHandlers() {
    }

    private fun unRegisterEventHandlers() {
    }

    companion object {
        private var texture: XGLTexture2DBuffer? = null
        private var screenWidth: Int = 0
        private var screenHeight: Int = 0

        fun onWindowSizeChange(width: Int, height: Int) {
            screenWidth = width
            screenHeight = height

            texture?.close()

            // Generate Texture2D for Overlay
            // 把纹理的维度设置为屏幕大小：传入width和height，只分配相应的内存，而不填充
            texture = XGLTexture2DBuffer(
                FrameBufferTextureFormat.RGBA8, screenWidth, screenHeight
            )
        }
    }

}

