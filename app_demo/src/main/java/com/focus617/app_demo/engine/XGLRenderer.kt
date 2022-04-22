package com.focus617.app_demo.engine

import android.opengl.GLES31
import android.opengl.GLSurfaceView
import com.focus617.app_demo.objects.Triangle
import com.focus617.app_demo.renderer.XGLShader
import com.focus617.core.engine.baseDataType.Color
import com.focus617.core.engine.core.IfWindow
import com.focus617.core.engine.renderer.RenderCommand
import com.focus617.core.engine.renderer.Shader
import com.focus617.core.engine.renderer.VertexArray
import com.focus617.core.engine.renderer.XRenderer
import com.focus617.core.engine.scene.OrthographicCamera
import timber.log.Timber
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class XGLRenderer(private val window: IfWindow) : XRenderer(), GLSurfaceView.Renderer {

    private val mCamera = OrthographicCamera(-1.0f, 1.0f, -1.0f, 1.0f)
    private lateinit var mTriangle: Triangle

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // 打印OpenGL Version，Vendor，etc
        ((window as AndroidWindow).mRenderContext as XGLContext).getOpenGLInfo()

        // 设置重绘背景框架颜色
        RenderCommand.setClearColor(Color(0.1F, 0.1F, 0.1F, 1F))
        RenderCommand.clear()

        mTriangle = Triangle(window.context)
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        Timber.d("width = $width, height = $height")

        // 设置渲染的OpenGL场景（视口）的位置和大小
        GLES31.glViewport(0, 0, width, height)

        // 计算透视投影矩阵 (Project Matrix)，而后将应用于onDrawFrame（）方法中的对象坐标
        //val ratio: Float = width.toFloat() / height.toFloat()
        //Matrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

    override fun onDrawFrame(unused: GL10) {
        // 首先清理屏幕，重绘背景颜色
        RenderCommand.setClearColor(Color(0.1F, 0.1F, 0.1F, 1F))
        RenderCommand.clear()

        mCamera.setPosition(0.5F, 0.5F, 0F)
        //mCamera.setRotation(270.0F)

        beginScene(mCamera)

        with(mTriangle) { submit(shader, vertexArray) }

        endScene()
    }

    override fun submit(shader: Shader, vertexArray: VertexArray) {
        (shader as XGLShader).bind()
        // 将模型视图投影矩阵传递给顶点着色器
        shader.uploadUniformMat4("u_ViewProjection", SceneData.mViewProjectionMatrix)

        vertexArray.bind()
        RenderCommand.drawIndexed(vertexArray)

        //vertexArray.unbind()
        //shader.unbind()
    }
}