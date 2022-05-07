package com.focus617.app_demo.engine.d2

import android.content.Context
import android.opengl.GLSurfaceView
import com.focus617.app_demo.engine.XGLContext
import com.focus617.app_demo.renderer.XGLTextureSlots
import com.focus617.core.engine.baseDataType.Color
import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.math.Vector2
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.Vector4
import com.focus617.core.engine.renderer.RenderCommand
import com.focus617.core.engine.renderer.Texture2D
import com.focus617.core.engine.renderer.XRenderer
import com.focus617.core.engine.scene.Camera
import java.io.Closeable
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class XGLRenderer2D(
    private val context: Context,
    private val scene: XGLScene2D
) : XRenderer(), GLSurfaceView.Renderer, Closeable {

    override fun onSurfaceCreated(unused: GL10, config: EGLConfig) {
        // 打印OpenGL Version，Vendor，etc
        XGLContext.getOpenGLInfo()

        // TODO: 当前的问题是，必须在opengl线程才能调用opengl api，无法在主线程调用。
        // 调用scene.initOpenGlResource, 因为涉及opengl api, 只好在这里调用
        scene.initOpenGlResource()

        Renderer2DData.initStaticData(context)     // 初始化本Render的静态数据

        RenderCommand.init()
        // 设置重绘背景框架颜色
        RenderCommand.setClearColor(Color(0.1F, 0.1F, 0.1F, 1F))
        RenderCommand.clear()

        // ES3.2 doesn't support DebugMessageCallback
        //XGLContext.initDebug()
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        LOG.info("onSurfaceChanged (width = $width, height = $height)")

        // 设置渲染的OpenGL场景（视口）的位置和大小
        RenderCommand.setViewport(0, 0, width, height)

        scene.mCameraController.onWindowSizeChange(width, height)
    }

    override fun onDrawFrame(unused: GL10) {
        // 清理屏幕，重绘背景颜色
        RenderCommand.setClearColor(Color(0.1F, 0.1F, 0.1F, 1F))
        RenderCommand.clear()

        beginScene(scene.mCamera)

        endScene()
    }

    override fun close() {
        Renderer2DData.close()
    }

    override fun beginScene(camera: Camera) {
        XGLContext.checkGLError("before beginScene")
        with(Renderer2DData.TextureShader) {
            bind()
            setMat4("u_ProjectionMatrix", SceneData.sProjectionMatrix)
            setMat4("u_ViewMatrix", SceneData.sViewMatrix)
            setMat4("u_ModelMatrix", Mat4())
        }
    }

    override fun endScene() {
        // 将顶点数据注入VertexBuffer
        with(Renderer2DData) {
            QuadVertexBuffer.setData(
                FloatBuffer.wrap(QuadVertexBufferBase),
                QuadVertexBufferPtr * Float.SIZE_BYTES
            )
        }
        flush()
        XGLContext.checkGLError("after endScene")
    }

    fun flush() {
        if (Renderer2DData.QuadIndexCount == 0) return

        XGLTextureSlots.flush()

        with(Renderer2DData) {
            QuadVertexArray.bind()
            RenderCommand.drawIndexed(QuadVertexArray, QuadIndexCount)
            stats.drawCalls++
        }
    }

//    fun flushAndReset() {
//        endScene()
//        Renderer2DData.QuadIndexCount = 0
//        Renderer2DData.QuadVertexBufferPtr = 0
//        XGLTextureSlots.TextureSlotIndex = 1
//    }

    companion object {
        /**
         * 支持批处理 Batching Renderer后的 DrawQuad函数的做法是，每次调用 DrawQuad函数，
         * 就去动态填充Vertex Buffer里顶点的各项顶点属性数据。
         */
        fun drawQuad(position: Vector3, size: Vector2, color: Vector4) {
            val texIndex: Float = 0.0f // White Texture
            val tilingFactor: Float = 1.0f

            Renderer2DData.putQuadVertex(
                position, color, Vector2(0.0f, 0.0f), texIndex, tilingFactor
            )
            Renderer2DData.putQuadVertex(
                Vector3(position.x + size.x, position.y, 0.0f),
                color,
                Vector2(1.0f, 0.0f),
                texIndex,
                tilingFactor
            )
            Renderer2DData.putQuadVertex(
                Vector3(position.x + size.x, position.y + size.y, 0.0f),
                color,
                Vector2(1.0f, 1.0f),
                texIndex,
                tilingFactor
            )
            Renderer2DData.putQuadVertex(
                Vector3(position.x, position.y + size.y, 0.0f),
                color,
                Vector2(0.0f, 1.0f),
                texIndex,
                tilingFactor
            )

            Renderer2DData.QuadIndexCount += 6

            Renderer2DData.stats.quadCount++
        }

        fun drawQuad(position: Vector2, size: Vector2, color: Vector4) {
            drawQuad(Vector3(position.x, position.y, 0.0f), size, color)
        }

        /**
         * Tiling功能：Tint是着色、染色的东西，其实就是给Texture的 Color加上一个颜色的滤镜而已
         */
        fun drawQuad(
            position: Vector3, size: Vector2, texture: Texture2D, tilingFactor: Float = 1.0f
        ) {
            val texIndex: Float = XGLTextureSlots.getId(texture).toFloat()

            Renderer2DData.putQuadVertex(
                position, Renderer2DData.WHITE, Vector2(0.0f, 0.0f), texIndex, tilingFactor
            )
            Renderer2DData.putQuadVertex(
                Vector3(position.x + size.x, position.y, 0.0f),
                Renderer2DData.WHITE,
                Vector2(1.0f, 0.0f),
                texIndex,
                tilingFactor
            )
            Renderer2DData.putQuadVertex(
                Vector3(position.x + size.x, position.y + size.y, 0.0f),
                Renderer2DData.WHITE,
                Vector2(1.0f, 1.0f),
                texIndex,
                tilingFactor
            )
            Renderer2DData.putQuadVertex(
                Vector3(position.x, position.y + size.y, 0.0f),
                Renderer2DData.WHITE,
                Vector2(0.0f, 1.0f),
                texIndex,
                tilingFactor
            )

            Renderer2DData.QuadIndexCount += 6

            Renderer2DData.stats.quadCount++
        }

        fun drawQuad(
            position: Vector2,
            size: Vector2,
            rotation: Float,
            texture: Texture2D,
            tilingFactor: Float = 1.0f
        ) {
            drawRotatedQuad(
                Vector3(position.x, position.y, 0.0f),
                size,
                rotation,
                texture,
                tilingFactor
            )
        }

        fun drawRotatedQuad(
            position: Vector3,
            size: Vector2,
            rotationInDegree: Float,
            color: Vector4
        ) {
            val texIndex: Float = 0.0f // White Texture
            val tilingFactor: Float = 1.0f

            val transform: Mat4 = Mat4().transform2D(position, size, rotationInDegree)
            //LOG.info(transform.toString("Transform Matrix"))

            Renderer2DData.putQuadVertex(
                (transform * Renderer2DData.QuadVertexPosition[0]).toVector3(),
                color,
                Vector2(0.0f, 0.0f),
                texIndex,
                tilingFactor
            )
            Renderer2DData.putQuadVertex(
                (transform * Renderer2DData.QuadVertexPosition[1]).toVector3(),
                color,
                Vector2(1.0f, 0.0f),
                texIndex,
                tilingFactor
            )
            Renderer2DData.putQuadVertex(
                (transform * Renderer2DData.QuadVertexPosition[2]).toVector3(),
                color,
                Vector2(1.0f, 1.0f),
                texIndex,
                tilingFactor
            )
            Renderer2DData.putQuadVertex(
                (transform * Renderer2DData.QuadVertexPosition[3]).toVector3(),
                color,
                Vector2(0.0f, 1.0f),
                texIndex,
                tilingFactor
            )

            Renderer2DData.QuadIndexCount += 6

            Renderer2DData.stats.quadCount++
        }

        fun drawRotatedQuad(
            position: Vector2,
            size: Vector2,
            rotationInDegree: Float,
            color: Vector4
        ) {
            drawRotatedQuad(Vector3(position.x, position.y, 0.0f), size, rotationInDegree, color)
        }

        fun drawRotatedQuad(
            position: Vector3,
            size: Vector2,
            rotationInDegree: Float,
            texture: Texture2D,
            tilingFactor: Float = 1.0f,
            tintColor: Vector4 = Renderer2DData.WHITE
        ) {
            val texIndex: Float = XGLTextureSlots.getId(texture).toFloat()

            val transform: Mat4 = Mat4().transform2D(position, size, rotationInDegree)
            //LOG.info(transform.toString("Transform Matrix"))

            Renderer2DData.putQuadVertex(
                (transform * Renderer2DData.QuadVertexPosition[0]).toVector3(),
                Renderer2DData.WHITE,
                Vector2(0.0f, 0.0f),
                texIndex,
                tilingFactor
            )
            Renderer2DData.putQuadVertex(
                (transform * Renderer2DData.QuadVertexPosition[1]).toVector3(),
                Renderer2DData.WHITE,
                Vector2(1.0f, 0.0f),
                texIndex,
                tilingFactor
            )
            Renderer2DData.putQuadVertex(
                (transform * Renderer2DData.QuadVertexPosition[2]).toVector3(),
                Renderer2DData.WHITE,
                Vector2(1.0f, 1.0f),
                texIndex,
                tilingFactor
            )
            Renderer2DData.putQuadVertex(
                (transform * Renderer2DData.QuadVertexPosition[3]).toVector3(),
                Renderer2DData.WHITE,
                Vector2(0.0f, 1.0f),
                texIndex,
                tilingFactor
            )

            Renderer2DData.QuadIndexCount += 6

            Renderer2DData.stats.quadCount++
        }

        fun drawRotatedQuad(
            position: Vector2,
            size: Vector2,
            rotationInDegree: Float,
            texture: Texture2D,
            tilingFactor: Float = 1.0f,
            tintColor: Vector4 = Renderer2DData.WHITE
        ) {
            drawRotatedQuad(
                Vector3(position.x, position.y, 0.0f),
                size,
                rotationInDegree,
                texture,
                tilingFactor,
                tintColor
            )
        }
    }
}
