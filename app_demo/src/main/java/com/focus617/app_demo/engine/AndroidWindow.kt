package com.focus617.app_demo.engine

import android.content.Context
import android.opengl.GLSurfaceView
import android.view.KeyEvent
import android.view.MotionEvent
import com.focus617.core.engine.core.IfWindow
import com.focus617.core.engine.core.WindowProps
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.screenTouchEvents.ViewOnTouchEvent

class AndroidWindow private constructor(
    context: Context,
) : GLSurfaceView(context), IfWindow {
    override val LOG = logger()

    private val mData = WindowData()

    private var mRenderer: XGLRenderer = XGLRenderer()


    fun initView(isES3Supported: Boolean) {
        // Check if the system supports OpenGL ES 3.0.
        if (isES3Supported) {
            // Request an OpenGL ES 3.0 compatible context.
            setEGLContextClientVersion(2)
        } else {
            // Request an OpenGL ES 2.0 compatible context.
            setEGLContextClientVersion(2)
        }
        /**
         * 一个给定的Android设备可能支持多个EGLConfig渲染配置。
         * 可用的配置可能在有多少个数据通道和分配给每个数据通道的比特数上不同。
         * 默认情况下，GLSurfaceView选择的EGLConfig有RGB_888像素格式，至少有16位深度缓冲和没有模板。
         * 安装一个ConfigChooser，它将至少具有指定的depthSize和stencilSize的配置，并精确指定redSize、
         * greenSize、blueSize和alphaSize(Alpha used for plane blending)。
         */
        //setEGLConfigChooser(8, 8, 8, 8, 16, 0)
        setEGLConfigChooser(true)
        requestFocus()                   //获取焦点
        isFocusableInTouchMode = true    //设置为可触控

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
        LOG.info("onUpdate")
    }

    override fun setEventCallbackFn(callback: (Event) -> Unit) {
        LOG.info("callback func is set")
        mData.callback = callback
    }

    override fun onKeyDown(keyCode: Int, event: KeyEvent?): Boolean {
        if (keyCode == KeyEvent.KEYCODE_DPAD_CENTER) {
            //queueEvent { mRenderer.handleDpadCenter() }
            LOG.info("onKeyDown")
            return true
        }
        return super.onKeyDown(keyCode, event)
    }

    private var previousX: Float = 0f
    private var previousY: Float = 0f

    override fun onTouchEvent(e: MotionEvent): Boolean {
        // MotionEvent reports input details from the touch screen
        // and other input controls. In this case, you are only
        // interested in events where the touch position changed.
        when (e.action) {
            MotionEvent.ACTION_MOVE -> {
                LOG.info("onTouchEvent: ${e.action}(${e.x},${e.y})")
                val event = ViewOnTouchEvent(e.x, e.y, this)
                mData.callback?.let { it(event) }
            }
            else -> {
                LOG.info("onTouchEvent: ${e.action}")
            }
        }
        return super.onTouchEvent(e)
    }


    companion object {
        // 用来统一保存Engine对Window的信息需求
        class WindowData() {
            var title: String = ""
            var width: Int = 0
            var height: Int = 0
            var VSync: Boolean = true
            var callback: ((Event) -> Unit)? = null
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