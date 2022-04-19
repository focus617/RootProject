package com.focus617.app_demo.engine

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.MotionEvent
import com.focus617.core.engine.core.IfWindow
import com.focus617.core.engine.core.WindowProps
import com.focus617.core.engine.renderer.IfGraphicsContext
import com.focus617.core.platform.event.applicationEvents.AppUpdateEvent
import com.focus617.core.platform.event.applicationEvents.AppVariant
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventHandler
import com.focus617.core.platform.event.screenTouchEvents.TouchMovedEvent

class AndroidWindow private constructor(
    context: Context
) : GLSurfaceView(context), IfWindow {
    override val LOG = logger()

    private val mData = WindowData()

    private var mRenderer: XGLRenderer = XGLRenderer()
    var isES3Supported: Boolean = false
    override val renderContext: IfGraphicsContext = XGLContext(this)

    fun initView(isES3Supported: Boolean) {
        this.isES3Supported = isES3Supported

        // 初始化Renderer Context
        renderContext.init()

        // 设置渲染器（Renderer）以在GLSurfaceView上绘制
        setRenderer(mRenderer)

        // 仅在绘图数据发生更改时才渲染视图: 在该模式下当渲染内容变化时不会主动刷新效果，需要手动调用requestRender()
        renderMode = RENDERMODE_WHEN_DIRTY
        //renderMode = RENDERMODE_CONTINUOUSLY
    }


    override fun isVSync(): Boolean = mData.VSync
    override fun setVSync(enable: Boolean) {
        mData.VSync = enable
    }

    override fun getWindowWidth(): Int = width
    override fun getWindowHeight(): Int = height

    override fun onUpdate() {
        renderContext.swapBuffers()
    }

    override fun setEventCallbackFn(callback: EventHandler<Event>) {
        LOG.info("callback func is set")
        mData.callback = callback
    }

    override fun performClick(): Boolean {
        return super.performClick()
    }

    override fun onTouchEvent(e: MotionEvent): Boolean {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.
        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                LOG.info("onTouchEvent: ${e.action}(${e.x},${e.y})")
                val event = TouchMovedEvent(e.x, e.y, this)
                mData.callback?.let { it(event) }
            }
            else -> {
                LOG.info("onTouchEvent: ${e.action}")
            }
        }
        return super.onTouchEvent(e)
    }

    private fun testCallback(){
        val event = AppUpdateEvent(AppVariant.MOBILE_DEMO, this)
        mData.callback?.let { it(event) }
    }

    companion object {
        // 用来统一保存Engine对Window的信息需求
        class WindowData() {
            var title: String = ""
            var width: Int = 0
            var height: Int = 0
            var VSync: Boolean = true
            var callback: EventHandler<Event>? = null
        }

        private var instance: AndroidWindow? = null

        private fun create(
            context: Context,
            props: WindowProps
        ): AndroidWindow {
            val window = AndroidWindow(context)
            with(window) {
                mData.title = props.title
                mData.width = props.width
                mData.height = props.height
            }
            return window
        }

        fun createWindow(
            context: Context,
            props: WindowProps = WindowProps()
        ): AndroidWindow =
            synchronized(this) {
                (instance ?: create(context, props)).also { instance = it }
            }

    }

}