package com.focus617.app_demo.engine

import android.annotation.SuppressLint
import android.content.Context
import android.opengl.GLSurfaceView
import com.focus617.app_demo.BuildConfig
import com.focus617.app_demo.GameActivity
import com.focus617.app_demo.engine.d2.Sandbox2D
import com.focus617.app_demo.engine.d2.XGLRenderer2D
import com.focus617.app_demo.engine.d2.XGLScene2D
import com.focus617.app_demo.engine.d3.Sandbox3D
import com.focus617.app_demo.engine.d3.XGLRenderer3D
import com.focus617.app_demo.engine.d3.XGLScene3D
import com.focus617.app_demo.engine.input.GestureInput
import com.focus617.app_demo.renderer.XGLRendererAPI
import com.focus617.core.engine.core.Engine
import com.focus617.core.engine.core.IfWindow
import com.focus617.core.engine.core.WindowProps
import com.focus617.core.engine.renderer.IfGraphicsContext
import com.focus617.core.engine.renderer.RenderCommand
import com.focus617.core.engine.renderer.XRenderer
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventHandler
import java.io.Closeable

/**
 * 现在先借用 GLSurfaceView 来实现 IfWindow 定义的功能。
 * 依靠 mRenderContext.swapBuffers() in onUpdate() 实现两种方案的连接
 */
class AndroidWindow private constructor(
    context: Context
) : GLSurfaceView(context), IfWindow, Closeable {
    override val LOG = logger()

    val mData = WindowData()
    private lateinit var renderer: XRenderer

    override lateinit var mRenderer: XRenderer  // Used for Engine
    override val mRenderContext: IfGraphicsContext = XGLContext(this)

    fun setDebug() {
        debugFlags = if (BuildConfig.DEBUG) DEBUG_LOG_GL_CALLS else DEBUG_CHECK_GL_ERROR
    }

    override fun isVSync(): Boolean = mData.VSync
    override fun setVSync(enable: Boolean) {
        mData.VSync = enable
    }

    override fun getWindowWidth(): Int = width
    override fun getWindowHeight(): Int = height

    override fun onUpdate() {
        mRenderContext.swapBuffers()
    }

    override fun setEventCallbackFn(callback: EventHandler<Event>) {
        LOG.info("callback func is set")
        mData.callback = callback
    }

    override fun close() {
        instance = null
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

        // 保证Window的单例
        private var instance: AndroidWindow? = null

        fun createWindow(
            context: Context,
            engine: Engine,
            props: WindowProps = WindowProps()
        ): AndroidWindow =
            synchronized(this) {
                return if (instance != null) instance!!
                else {
                    instance = create(context, props)
                    initRendererCommand()
                    initView(
                        (context as GameActivity).isES3Supported(),
                        engine
                    )
                    setOnTouchListener(instance!!)

                    return instance as AndroidWindow
                }
            }

        private fun create(context: Context, props: WindowProps): AndroidWindow {
            val window = AndroidWindow(context)
            with(window) {
                mData.title = props.title
                mData.width = props.width
                mData.height = props.height
            }
            return window
        }

        private fun initRendererCommand() {
            RenderCommand.sRendererAPI = XGLRendererAPI()
        }

        private fun initView(isES3Supported: Boolean, engine: Engine) {
            with(instance!!) {
                // 初始化Renderer Context
                (mRenderContext as XGLContext).isES3Supported = isES3Supported
                mRenderContext.init()

                // 创建并设置渲染器（Renderer）以在GLSurfaceView上绘制
                renderer =
                    if(engine is Sandbox3D) XGLRenderer3D(engine.scene as XGLScene3D)
                    else XGLRenderer2D((engine as Sandbox2D).scene as XGLScene2D)

                setRenderer(renderer as Renderer)
                mRenderer = renderer

                // 仅在绘图数据发生更改时才渲染视图: 在该模式下当渲染内容变化时不会主动刷新效果，需要手动调用requestRender()
                renderMode = RENDERMODE_WHEN_DIRTY
                //renderMode = RENDERMODE_CONTINUOUSLY
            }
        }

        @SuppressLint("ClickableViewAccessibility")
        private fun setOnTouchListener(window: AndroidWindow) {
            window.setOnTouchListener(GestureInput(window))
        }
    }
}