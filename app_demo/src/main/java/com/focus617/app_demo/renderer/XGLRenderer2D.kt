package com.focus617.app_demo.renderer

import android.content.Context
import android.opengl.GLSurfaceView
import com.focus617.app_demo.engine.XGLContext
import com.focus617.core.engine.baseDataType.Color
import com.focus617.core.engine.math.*
import com.focus617.core.engine.renderer.*
import com.focus617.core.engine.scene.OrthographicCamera
import com.focus617.core.engine.scene.OrthographicCameraController
import com.focus617.core.engine.scene.Scene
import java.io.Closeable
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class XGLRenderer2D(
    private val context: Context,
    private val scene: Scene
) : XRenderer(), GLSurfaceView.Renderer, Closeable {

    //TODO: Game objects should NOT owned by Renderer .
    // It should be injected from Engine's Scene, since GlSurfaceView/Renderer is always recreated
    // in case of configuration change, etc.
    override val mCameraController =
        OrthographicCameraController(scene.mCamera as OrthographicCamera)

    override fun initRenderer() {
        RenderCommand.init()

        // 初始化本Render的静态数据
        initShader(context)
        initVertexArray()
        XMatrix.setIdentityM(transform, 0)
    }

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // 打印OpenGL Version，Vendor，etc
        XGLContext.getOpenGLInfo()

        // 设置重绘背景框架颜色
        RenderCommand.setClearColor(Color(0.1F, 0.1F, 0.1F, 1F))
        RenderCommand.clear()

        // TODO: 当前的问题是，必须在opengl线程才能调用opengl api，无法在主线程调用。
        // 调用XRenderer.initRenderer, 因为涉及opengl api, 只好在这里调用
        initRenderer()
    }


    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        LOG.info("onSurfaceChanged (width = $width, height = $height)")

        // 设置渲染的OpenGL场景（视口）的位置和大小
        RenderCommand.setViewport(0, 0, width, height)

        mCameraController.onWindowSizeChange(width, height)
    }

    override fun onDrawFrame(unused: GL10) {
        // 清理屏幕，重绘背景颜色
        RenderCommand.setClearColor(Color(0.1F, 0.1F, 0.1F, 1F))
        RenderCommand.clear()

        beginScene(scene.mCamera)
        flatColorShader.bind()
        flatColorShader.uploadUniformFloat4("u_Color", mColor)
        submit(flatColorShader, quadVertexArray, transform)
        endScene()
    }

    override fun submit(
        shader: Shader,
        vertexArray: VertexArray,
        transform: FloatArray
    ) {
        (shader as XGLShader).bind()
        // 将模型视图投影矩阵传递给顶点着色器
        shader.uploadUniformMat4("u_ProjectionMatrix", SceneData.sProjectionMatrix)
        shader.uploadUniformMat4("u_ViewMatrix", SceneData.sViewMatrix)
        shader.uploadUniformMat4("u_ModelMatrix", transform)

        vertexArray.bind()
        RenderCommand.drawIndexed(vertexArray)

        // 下面这两行可以省略，以节约GPU的运行资源；
        // 在下个submit，会bind其它handle，自然会实现unbind
        vertexArray.unbind()
        shader.unbind()
    }

    override fun close() {
        quadVertexArray.close()
        flatColorShader.close()
        //textureShader.close()
        //whiteTexture.close()
    }

    fun drawQuad(position: Point3D, size: Vector2, color: Vector4) {
        flatColorShader.bind()
        flatColorShader.uploadUniformFloat4("u_Color", color)

        quadVertexArray.bind()
        RenderCommand.drawIndexed(quadVertexArray)
    }

    fun drawQuad(position: Point2D, size: Vector2, color: Vector4) {
        drawQuad(Point3D(position.x, position.y, 0.0f), size, color)
    }



    companion object Renderer2DStorage {

        lateinit var quadVertexArray: XGLVertexArray    // 一个Mesh, 代表Quad
        lateinit var flatColorShader: XGLShader            // 两个Shader
        //lateinit var textureShader: XGLShader
        //lateinit var whiteTexture: XGLTexture2D            // 一个默认贴图, 用于Blend等

        private val PATH = "SquareWithTexture"
        private val SHADER_FILE = "FlatColor.glsl"
        private var transform: FloatArray = FloatArray(16)
        private val mColor = Vector4(0.2f, 0.3f, 0.8f, 1.0f)

        private fun initShader(context: Context) {
            flatColorShader = XGLShaderBuilder.createShader(
                context,
                "$PATH/$SHADER_FILE"
            ) as XGLShader
        }

        private fun initVertexArray() {
            quadVertexArray =
                XGLBufferBuilder.createVertexArray() as XGLVertexArray

            // 每个顶点有2个顶点属性一位置、纹理
            val vertices = floatArrayOf(
                // x,   y,     z,  TextureX, TextureY
                -0.5f, -0.5f, 0.0f, 0.0f, 0.0f,
                0.5f, -0.5f, 0.0f, 1.0f, 0.0f,
                0.5f, 0.5f, 0.0f, 1.0f, 1.0f,
                -0.5f, 0.5f, 0.0f, 0.0f, 1.0f
            )
            val vertexBuffer = XGLBufferBuilder.createVertexBuffer(
                vertices, vertices.size * Float.SIZE_BYTES
            ) as XGLVertexBuffer

            val layout = BufferLayout(
                listOf(
                    BufferElement("a_Position", ShaderDataType.Float3, true),
                    BufferElement("a_TexCoord", ShaderDataType.Float2, true)
                )
            )
            vertexBuffer.setLayout(layout)
            quadVertexArray.addVertexBuffer(vertexBuffer)

            val indices = shortArrayOf(
                0, 1, 2, 2, 3, 0
            )
            val indexBuffer = XGLBufferBuilder.createIndexBuffer(
                indices, indices.size
            ) as XGLIndexBuffer

            quadVertexArray.setIndexBuffer(indexBuffer)
        }
    }

}