package com.focus617.app_demo.engine.d2

import android.content.Context
import android.opengl.GLSurfaceView
import com.focus617.app_demo.engine.XGLContext
import com.focus617.app_demo.renderer.XGLShader
import com.focus617.app_demo.renderer.XGLShaderBuilder
import com.focus617.app_demo.renderer.XGLTextureBuilder
import com.focus617.app_demo.renderer.XGLVertexArray
import com.focus617.core.engine.baseDataType.Color
import com.focus617.core.engine.math.Vector2
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.Vector4
import com.focus617.core.engine.objects.IfDrawable
import com.focus617.core.engine.objects.d2.Quad
import com.focus617.core.engine.renderer.RenderCommand
import com.focus617.core.engine.renderer.Texture2D
import com.focus617.core.engine.renderer.XRenderer
import com.focus617.core.engine.scene.Camera
import com.focus617.core.engine.scene.OrthographicCamera
import com.focus617.core.engine.scene.OrthographicCameraController
import com.focus617.core.engine.scene.Scene
import java.io.Closeable
import java.nio.LongBuffer
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
        initTextureForScene()

        initStaticData(context)     // 初始化本Render的静态数据
    }

    private fun initTextureForScene() {
        val texture = XGLTextureBuilder.createTexture(
            context,
            "$PATH/$TEXTURE_FILE"
        )!!
        scene.register(objectTextureName, texture)

        val whiteTexture = XGLTextureBuilder.createTexture(1, 1)!!
        val whiteTextureData = longArrayOf(0xffffffff)
        whiteTexture.setData(LongBuffer.wrap(whiteTextureData), Int.SIZE_BYTES)
        scene.register(whiteTextureName, whiteTexture)
    }

    override fun close() {
        quadVertexArray.close()
        textureShader.close()
    }

    fun flush(){

    }

    override fun beginScene(camera: Camera) {
        // 在多线程渲染里，会把BeginScene函数放在RenderCommandQueue里执行
        // camera在多线程渲染的时候不能保证主线程是否正在更改Camera的相关信息
        synchronized(camera) {
            System.arraycopy(camera.getProjectionMatrix(), 0, SceneData.sProjectionMatrix, 0, 16)
            System.arraycopy(camera.getViewMatrix(), 0, SceneData.sViewMatrix, 0, 16)
        }

        textureShader.bind()
        textureShader.setMat4("u_ProjectionMatrix", SceneData.sProjectionMatrix)
        textureShader.setMat4("u_ViewMatrix", SceneData.sViewMatrix)
    }

    override fun endScene() {

    }

    fun drawQuad(position: Vector3, size: Vector2, color: Vector4) {
        textureShader.setFloat4("u_Color", color)
        textureShader.setFloat("u_TilingFactor", 1.0f)
        textureShader.setMat4("u_ModelMatrix", getTransform(position, size))

        // Bind white texture here
        scene.texture(whiteTextureName)!!.bind()

        quadVertexArray.bind()
        RenderCommand.drawIndexed(quadVertexArray)
    }

    fun drawQuad(position: Vector2, size: Vector2, color: Vector4) {
        drawQuad(Vector3(position.x, position.y, 0.0f), size, color)
    }

    fun drawQuad(
        position: Vector3, size: Vector2, texture: Texture2D, tilingFactor: Float = 1.0f,
        tintColor: Vector4 = WHITE
    ) {
        textureShader.setFloat4("u_Color", tintColor)
        textureShader.setFloat("u_TilingFactor", tilingFactor)
        textureShader.setMat4("u_ModelMatrix", getTransform(position, size))

        // Bind texture
        texture.bind()

        // Bind VertexArray
        quadVertexArray.bind()
        RenderCommand.drawIndexed(quadVertexArray)
    }

    fun drawQuad(
        position: Vector2,
        size: Vector2,
        rotation: Float,
        texture: Texture2D,
        tilingFactor: Float = 1.0f,
        tintColor: Vector4 = Vector4(1.0f, 1.0f, 1.0f, 1.0f)
    ) {
        drawRotatedQuad(
            Vector3(position.x, position.y, 0.0f),
            size,
            rotation,
            texture,
            tilingFactor,
            tintColor
        )
    }

    fun drawRotatedQuad(position: Vector3, size: Vector2, rotation: Float, color: Vector4) {
        textureShader.setFloat4("u_Color", color)
        textureShader.setFloat("u_TilingFactor", 1.0f)
        textureShader.setMat4("u_ModelMatrix", getTransform(position, size, rotation))

        // Bind white texture here
        scene.texture(whiteTextureName)!!.bind()

        // Bind VertexArray
        quadVertexArray.bind()
        RenderCommand.drawIndexed(quadVertexArray)
    }

    fun drawRotatedQuad(position: Vector2, size: Vector2, rotation: Float, color: Vector4) {
        drawRotatedQuad(Vector3(position.x, position.y, 0.0f), size, rotation, color)
    }

    fun drawRotatedQuad(
        position: Vector3,
        size: Vector2,
        rotation: Float,
        texture: Texture2D,
        tilingFactor: Float = 1.0f,
        tintColor: Vector4 = WHITE
    ) {
        textureShader.setFloat4("u_Color", tintColor)
        textureShader.setFloat("u_TilingFactor", tilingFactor)
        textureShader.setMat4("u_ModelMatrix", getTransform(position, size, rotation))

        // Bind texture
        texture.bind()

        // Bind VertexArray
        quadVertexArray.bind()
        RenderCommand.drawIndexed(quadVertexArray)
    }

    fun drawRotatedQuad(
        position: Vector2,
        size: Vector2,
        rotation: Float,
        texture: Texture2D,
        tilingFactor: Float = 1.0f,
        tintColor: Vector4 = Vector4(1.0f, 1.0f, 1.0f, 1.0f)
    ) {
        drawRotatedQuad(
            Vector3(position.x, position.y, 0.0f),
            size,
            rotation,
            texture,
            tilingFactor,
            tintColor
        )
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
        drawQuad(Vector2(-1.0f, 0f), Vector2(0.8f, 0.8f), RED)
        drawRotatedQuad(Vector2(0.5f, -0.5f), Vector2(0.5f, 0.75f), 45f, BLUE)
        drawQuad(
            Vector3(0.0f, 0.0f, -0.1f),
            Vector2(10f, 10f),
            scene.texture(objectTextureName)!! as Texture2D,
            10f
        )

        endScene()
    }

    companion object Renderer2DStorage {

        lateinit var quadVertexArray: XGLVertexArray    // 一个Mesh, 代表Quad
        lateinit var textureShader: XGLShader           // Shader

        fun initStaticData(context: Context) {
            initShader(context)
            initVertexArray(Quad())
        }

        private fun initShader(context: Context) {
            textureShader = XGLShaderBuilder.createShader(
                context,
                "$PATH/$TEXTURE_SHADER_FILE"
            ) as XGLShader
            textureShader.bind()
            textureShader.setInt("u_Texture", 0)
        }

        private fun initVertexArray(drawingObject: IfDrawable) {
            quadVertexArray = XGLVertexArray.buildVertexArray(drawingObject)
        }

        private fun getTransform(
            position: Vector3,
            size: Vector2,
            rotation: Float = 0.0f
        ): FloatArray {
            val quad = Quad()
            quad.resetTransform()
            quad.onTransform(position, size, rotation)
            return quad.modelMatrix
        }

        val WHITE = Vector4(1.0f, 1.0f, 1.0f, 1.0f)
        val RED = Vector4(0.8f, 0.3f, 0.2f, 1.0f)
        val BLUE = Vector4(0.2f, 0.3f, 0.8f, 1.0f)

        private val PATH = "SquareWithTexture"
        private val TEXTURE_SHADER_FILE = "Texture.glsl"
        private val TEXTURE_FILE = "Checkerboard.png"

        val whiteTextureName = "WhiteTexture"
        val objectTextureName = "$PATH/$TEXTURE_FILE"

    }

}