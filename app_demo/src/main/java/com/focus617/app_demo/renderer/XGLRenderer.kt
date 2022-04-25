package com.focus617.app_demo.renderer

import android.opengl.GLES30
import android.opengl.GLES31
import android.opengl.GLSurfaceView
import com.focus617.app_demo.engine.AndroidWindow
import com.focus617.app_demo.engine.XGLContext
import com.focus617.app_demo.objects.d2.Square
import com.focus617.core.engine.baseDataType.Color
import com.focus617.core.engine.core.IfWindow
import com.focus617.core.engine.math.XMatrix
import com.focus617.core.engine.renderer.RenderCommand
import com.focus617.core.engine.renderer.Shader
import com.focus617.core.engine.renderer.VertexArray
import com.focus617.core.engine.renderer.XRenderer
import timber.log.Timber
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class XGLRenderer(private val window: IfWindow) : XRenderer(), GLSurfaceView.Renderer {
    private val mProjectionMatrix = FloatArray(16)

    private val PATH = "SquareWithTexture"
    private val VERTEX_FILE = "vertex_shader_mvp.glsl"
    private val FRAGMENT_FILE = "fragment_shader.glsl"
    private val TEXTURE_FILE = "Checkerboard.png"
    private val TEXTURE_LOGO_FILE = "Logo.png"
    private lateinit var mShader: XGLShader
    private var mTexture: XGLTexture2D? = null
    private var mTextureLogo: XGLTexture2D? = null

    //private lateinit var mTriangle: Triangle
    private lateinit var mSquare: Square

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // 打印OpenGL Version，Vendor，etc
        ((window as AndroidWindow).mRenderContext as XGLContext).getOpenGLInfo()

        // 设置重绘背景框架颜色
        RenderCommand.setClearColor(Color(0.1F, 0.1F, 0.1F, 1F))
        RenderCommand.clear()

        // TODO: 当前的问题是，必须在opengl线程才能调用opengl api，无法在主线程调用。
        // 调用XRenderer.init, 因为涉及opengl api, 只好在这里调用
        this.init()

        // TODO: How to create objects in Sandbox layer?
        // shader = XGLShader(vertexShaderCode, fragmentShaderCode)
        mShader = XGLShaderBuilder.createShader(
            window.context,
            "MyShader",
            PATH,
            VERTEX_FILE,
            FRAGMENT_FILE
        ) as XGLShader

        mTexture = XGLTextureBuilder.createTexture(
            window.context,
            "$PATH/$TEXTURE_FILE"
        )
        mTextureLogo = XGLTextureBuilder.createTexture(
            window.context,
            "$PATH/$TEXTURE_LOGO_FILE"
        )

        //mTriangle = Triangle()
        mSquare = Square()
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        Timber.d("width = $width, height = $height")

        // 设置渲染的OpenGL场景（视口）的位置和大小
        GLES31.glViewport(0, 0, width, height)

        // 计算透视投影矩阵 (Project Matrix)，而后将应用于onDrawFrame（）方法中的对象坐标
        val ratio: Float = width.toFloat() / height.toFloat()
        XMatrix.frustumM(mProjectionMatrix, 0, -ratio, ratio, -1f, 1f, 3f, 7f)
    }

    override fun onDrawFrame(unused: GL10) {
        // 清理屏幕，重绘背景颜色
        RenderCommand.setClearColor(Color(0.1F, 0.1F, 0.1F, 1F))
        RenderCommand.clear()

        mTexture?.bind()

        //submit(shader, mTriangle.vertexArray, mTriangle.transform)
        submit(mShader, mSquare.vertexArray, mSquare.transform)

        // This texture has transparent alpha for part of image
        mTextureLogo?.bind()
        submit(mShader, mSquare.vertexArray, mSquare.transform)
    }

    override fun submit(
        shader: Shader,
        vertexArray: VertexArray,
        transform: FloatArray
    ) {
        (shader as XGLShader).bind()
        // 将模型视图投影矩阵传递给顶点着色器
        shader.uploadUniformMat4("u_ProjectionMatrix", mProjectionMatrix)
        shader.uploadUniformMat4("u_ViewMatrix", SceneData.sViewMatrix)
        shader.uploadUniformMat4("u_ModelMatrix", transform)

        shader.uploadUniformTexture("u_Texture", 0)

        vertexArray.bind()
        RenderCommand.drawIndexed(vertexArray)

        // 下面这两行可以省略，以节约GPU的运行资源；
        // 在下个submit，会bind其它handle，自然会实现unbind
        vertexArray.unbind()
        shader.unbind()
    }

    fun checkGLError() {
        val error = GLES30.glGetError()
        if (error != GLES30.GL_NO_ERROR) {
            val hexErrorCode = Integer.toHexString(error)
            LOG.error("glError: $hexErrorCode")
            throw RuntimeException("GLError")
        }
    }
}