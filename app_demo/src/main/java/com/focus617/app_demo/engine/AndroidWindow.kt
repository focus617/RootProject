package com.focus617.app_demo.engine

import android.content.Context
import android.opengl.GLSurfaceView
import android.util.AttributeSet
import com.focus617.core.engine.core.IfWindow
import com.focus617.core.engine.core.WindowProps
import com.focus617.core.platform.event.base.Event

class AndroidWindow private constructor(
    context: Context?,
    attrs: AttributeSet? = null
): GLSurfaceView(context, attrs), IfWindow {
    override val LOG = logger()

    private val mData = WindowData()

    private var mRenderer = XGLRender()

    init {
        // 创建一个OpenGL ES 2.0 的context
        setEGLContextClientVersion(2)

        // 设置渲染器（Renderer）以在GLSurfaceView上绘制
        setRenderer(mRenderer)

        // 仅在绘图数据发生更改时才渲染视图
        // 在该模式下当渲染内容变化时不会主动刷新效果，需要手动调用requestRender() 才行
        renderMode = RENDERMODE_WHEN_DIRTY
    }

    override fun isVSync(): Boolean = mData.VSync
    override fun setVSync(enable: Boolean){
        mData.VSync = enable
    }
    override fun getWindowWidth(): Int = width
    override fun getWindowHeight(): Int = height

    override fun onUpdate() {
        LOG.info("onUpdate")
    }

    override fun setEventCallbackFn(callback: (Event) -> Unit) {
        LOG.info("callback func is set")
    }

    companion object{
        // 用来统一保存Engine对Window的信息需求
        class WindowData() {
            var title: String = ""
            var width: Int = 0
            var height: Int = 0
            var VSync: Boolean = true
        }

        private var instance: AndroidWindow? = null

        private fun create(
            context: Context?,
            attrs: AttributeSet?,
            props: WindowProps
        ): AndroidWindow {
            val window = AndroidWindow(context, attrs)
            with(window){
                mData.title = props.title
                mData.width = props.width
                mData.height = props.height
            }
            return window
        }

        fun createWindow(
            context: Context?,
            attrs: AttributeSet? = null,
            props: WindowProps = WindowProps()
        ): AndroidWindow =
            synchronized(this) {
                (instance ?: create(context, attrs, props)).also { instance = it }
            }
    }

}