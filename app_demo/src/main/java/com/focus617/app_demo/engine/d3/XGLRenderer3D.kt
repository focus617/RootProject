package com.focus617.app_demo.engine.d3

import android.opengl.GLSurfaceView
import com.focus617.app_demo.engine.XGLContext
import com.focus617.app_demo.framebuffer.XGLFrameBuffer
import com.focus617.app_demo.framebuffer.submitWithOutlining
import com.focus617.app_demo.renderer.XGLTextureSlots
import com.focus617.core.engine.baseDataType.Color
import com.focus617.core.engine.renderer.RenderCommand
import com.focus617.core.engine.renderer.Shader
import com.focus617.core.engine.renderer.VertexArray
import com.focus617.core.engine.renderer.XRenderer
import com.focus617.core.engine.scene.Camera
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class XGLRenderer3D(private val scene: XGLScene3D) : XRenderer(), GLSurfaceView.Renderer {

    private lateinit var mFrameBuffer: XGLFrameBuffer

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // 打印OpenGL Version，Vendor，etc
        XGLContext.getOpenGLInfo()

        // TODO: 当前的问题是，必须在opengl线程才能调用opengl api，无法在主线程调用。
        // 调用scene.initOpenGlResource, 因为涉及opengl api, 只好在这里调用
        scene.initOpenGlResource()

        RenderCommand.init()
        // 设置重绘背景框架颜色
        RenderCommand.setClearColor(Color(0.1F, 0.1F, 0.1F, 1F))
        RenderCommand.clear()

        // ES3.2 doesn't support DebugMessageCallback
        //XGLContext.initDebug()

        mFrameBuffer = XGLFrameBuffer()
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        LOG.info("onSurfaceChanged (width = $width, height = $height)")

        // 设置渲染的OpenGL场景（视口）的位置和大小
        RenderCommand.setViewport(0, 0, width, height)

        scene.mCameraController.onWindowSizeChange(width, height)

        mFrameBuffer.resizeColorAttachment(width, height)
    }

    override fun onDrawFrame(unused: GL10) {
        XGLContext.checkGLError("Before onDrawFrame")
        // First pass: draw on FrameBuffer
        mFrameBuffer.bind()

        beginScene(scene.mCamera)

        // 清理屏幕，重绘背景颜色
        RenderCommand.setClearColor(Color(0.1F, 0.1F, 0.1F, 1.0F))
        RenderCommand.clear()

        XGLTextureSlots.flush()

        val layerStack = scene.engine.getLayerStack()
        for (layer in layerStack.mLayers) {
            layer.beforeDrawFrame()
            for (gameObject in layer.gameObjectList) {
                val shader = scene.mShaderLibrary.get(gameObject.shaderName)

                shader?.apply {
                    bind()
//                  LOG.info(XMatrix.toString(SceneData.sProjectionMatrix, matrixName = "ProjectionMatrix"))
//                  LOG.info(XMatrix.toString(SceneData.sViewMatrix, matrixName = "ViewMatrix"))
//                  LOG.info(XMatrix.toString(gameObject.modelMatrix, matrixName = "ModelMatrix"))

                    setMat4(Camera.U_PROJECT_MATRIX, SceneData.sProjectionMatrix)
                    setMat4(Camera.U_VIEW_MATRIX, SceneData.sViewMatrix)

                    if (gameObject.isSelected) {
                        gameObject.submitWithOutlining(shader)
                    } else {
                        gameObject.submit(shader)
                    }
                }
            }
            layer.afterDrawFrame()
        }
        endScene()

        mFrameBuffer.unbind()   // back to default

        // 清理屏幕，重绘背景颜色
        RenderCommand.setClearColor(Color(0.1F, 0.1F, 0.1F, 1.0F))
        RenderCommand.clear()

        // Second pass: draw on real screen
        mFrameBuffer.drawOnScreen()

        XGLContext.checkGLError("After onDrawFrame")
    }


    override fun beginScene(camera: Camera) {
        // 在多线程渲染里，会把BeginScene函数放在RenderCommandQueue里执行
        // camera在多线程渲染的时候不能保证主线程是否正在更改Camera的相关信息
        synchronized(camera) {
            System.arraycopy(
                camera.getProjectionMatrix(), 0, SceneData.sProjectionMatrix, 0, 16
            )
            System.arraycopy(camera.getViewMatrix(), 0, SceneData.sViewMatrix, 0, 16)
        }

    }

    override fun endScene() {
    }

    override fun submit(
        shader: Shader,
        vertexArray: VertexArray,
        transform: FloatArray
    ) {
    }

}