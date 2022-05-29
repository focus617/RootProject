package com.focus617.app_demo.engine.d3

import android.opengl.GLSurfaceView
import com.focus617.core.engine.ecs.mine.static.SceneData
import com.focus617.core.engine.ecs.mine.system.SceneCameraSystem
import com.focus617.core.engine.renderer.RenderCommand
import com.focus617.core.engine.renderer.api.IfRenderer
import com.focus617.core.engine.renderer.api.ShaderUniformConstants.U_PROJECT_MATRIX
import com.focus617.core.engine.renderer.api.ShaderUniformConstants.U_VIEW_MATRIX
import com.focus617.core.engine.renderer.framebuffer.FrameBufferAttachmentSpecification
import com.focus617.core.engine.renderer.framebuffer.FrameBufferSpecification
import com.focus617.core.engine.renderer.framebuffer.FrameBufferTextureFormat
import com.focus617.core.engine.renderer.framebuffer.FrameBufferTextureSpecification
import com.focus617.core.engine.resource.baseDataType.Color
import com.focus617.core.platform.base.BaseEntity
import com.focus617.opengles.egl.XGLContext
import com.focus617.opengles.renderer.framebuffer.XGLFrameBuffer
import com.focus617.opengles.renderer.framebuffer.XGLFrameBufferBuilder
import com.focus617.opengles.renderer.framebuffer.submitWithOutlining
import com.focus617.opengles.renderer.texture.XGLTextureSlots
import com.focus617.opengles.text.TextEntity3D
import com.focus617.opengles.text.TextLayer2D
import java.io.Closeable
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class XGLRenderer3D(
    private val xglResourceManager: XGL3DResourceManager
) : BaseEntity(), IfRenderer, GLSurfaceView.Renderer, Closeable {

    private lateinit var mFrameBuffer: XGLFrameBuffer

    override fun close() {
        mFrameBuffer.close()
    }

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // 打印OpenGL Version，Vendor，etc
        XGLContext.getOpenGLInfo()

        // TODO: 当前的问题是，必须在opengl线程才能调用opengl api，无法在主线程调用。
        // 调用scene.initOpenGlResource, 因为涉及opengl api, 只好在这里调用
        xglResourceManager.initOpenGlResource()

        RenderCommand.init()
        // 设置重绘背景框架颜色
        RenderCommand.setClearColor(Color(0.1F, 0.1F, 0.1F, 1F))
        RenderCommand.clear()

        // ES3.2 doesn't support DebugMessageCallback
        //XGLContext.initDebug()

        val fbSpec = FrameBufferSpecification()
        fbSpec.attachment = FrameBufferAttachmentSpecification(
            listOf(
                FrameBufferTextureSpecification(FrameBufferTextureFormat.RGBA8),
                FrameBufferTextureSpecification(FrameBufferTextureFormat.DEPTH24STENCIL8)
            )
        )
        fbSpec.mWidth = 1080
        fbSpec.mHeight = 2220
        mFrameBuffer = XGLFrameBufferBuilder.createFrameBuffer(fbSpec) as XGLFrameBuffer
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        LOG.info("onSurfaceChanged (width = $width, height = $height)")

        // 设置渲染的OpenGL场景（视口）的位置和大小
        RenderCommand.setViewport(0, 0, width, height)

        SceneCameraSystem.onWindowSizeChange(width, height)

        mFrameBuffer.resize(width, height)
        TextLayer2D.onWindowSizeChange(width, height)   // used for text on screen
        TextEntity3D.onWindowSizeChange(width, height)  // used for projection matrix
    }

    override fun onDrawFrame(unused: GL10) {
        XGLContext.checkGLError("Before onDrawFrame")
        // First pass: draw on FrameBuffer
        mFrameBuffer.bind()

        beginScene()

        // 清理屏幕，重绘背景颜色
        RenderCommand.setClearColor(Color(0.1F, 0.1F, 0.1F, 1.0F))
        RenderCommand.clear()

        XGLTextureSlots.flush()

        val layerStack = xglResourceManager.engine.getLayerStack()
        for (layer in layerStack.mLayers) {
            layer.beforeDrawFrame()
            for (gameObject in layer.gameObjectList) {
                val shader = xglResourceManager.mShaderLibrary.get(gameObject.shaderName)

                shader?.apply {
                    bind()
//                  LOG.info(XMatrix.toString(SceneData.sProjectionMatrix, matrixName = "ProjectionMatrix"))
//                  LOG.info(XMatrix.toString(SceneData.sViewMatrix, matrixName = "ViewMatrix"))
                    setMat4(U_PROJECT_MATRIX, SceneData.sProjectionMatrix)
                    setMat4(U_VIEW_MATRIX, SceneData.sViewMatrix)

                    if (gameObject.isSelected) {
                        gameObject.submitWithOutlining(shader, Color.GOLD)
                    } else {
                        gameObject.onRender(shader)
                    }
                }
            }
            layer.afterDrawFrame()
        }

        val overLayerStack = xglResourceManager.engine.getOverLayerStack()
        for (layer in overLayerStack.mLayers) {
            layer.beforeDrawFrame() // 暂时先在这里DrawFrame
            layer.afterDrawFrame()
        }
        endScene()
        XGLContext.checkGLError("After endScene")

        mFrameBuffer.unbind()   // back to default

        // 清理屏幕，重绘背景颜色
        RenderCommand.setClearColor(Color(0.1F, 0.1F, 0.1F, 1.0F))
        RenderCommand.clear()

        // Second pass: draw on real screen
        mFrameBuffer.drawOnScreen()

        XGLContext.checkGLError("After onDrawFrame")
    }

}