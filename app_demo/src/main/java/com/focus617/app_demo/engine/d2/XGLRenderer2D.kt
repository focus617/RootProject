package com.focus617.app_demo.engine.d2

import android.opengl.GLSurfaceView
import com.focus617.core.engine.ecs.mine.system.OrthographicCameraSystem
import com.focus617.core.engine.ecs.mine.system.RenderSystem
import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.math.Point2D
import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.math.Vector2
import com.focus617.core.engine.renderer.IfRenderer
import com.focus617.core.engine.renderer.RenderCommand
import com.focus617.core.engine.renderer.texture.SubTexture2D
import com.focus617.core.engine.renderer.texture.Texture2D
import com.focus617.core.engine.resource.baseDataType.Color
import com.focus617.core.platform.base.BaseEntity
import com.focus617.opengles.egl.XGLContext
import com.focus617.opengles.renderer.texture.XGLTextureSlots
import java.io.Closeable
import java.nio.FloatBuffer
import javax.microedition.khronos.egl.EGLConfig
import javax.microedition.khronos.opengles.GL10

class XGLRenderer2D(
    private val xglResourceManager: XGL2DResourceManager
) : BaseEntity(), IfRenderer, GLSurfaceView.Renderer, Closeable {

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
    }

    override fun onSurfaceChanged(unused: GL10, width: Int, height: Int) {
        LOG.info("onSurfaceChanged (width = $width, height = $height)")

        // 设置渲染的OpenGL场景（视口）的位置和大小
        RenderCommand.setViewport(0, 0, width, height)

        OrthographicCameraSystem.onWindowSizeChange(width, height)
    }

    override fun onDrawFrame(unused: GL10) {
        // 清理屏幕，重绘背景颜色
        RenderCommand.setClearColor(Color(0.1F, 0.1F, 0.1F, 1F))
        RenderCommand.clear()

        beginScene()

        endScene()
    }

    override fun close() {
        Renderer2DData.close()
    }

    override fun beginScene() {
        XGLContext.checkGLError("before beginScene")
        with(Renderer2DData.TextureShader) {
            bind()
            setMat4("u_ProjectionMatrix", RenderSystem.SceneData.sProjectionMatrix)
            setMat4("u_ViewMatrix", RenderSystem.SceneData.sViewMatrix)
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
        fun drawQuad(position: Point3D, size: Vector2, color: Color) {
            val texIndex: Float = 0.0f // White Texture
            val tilingFactor: Float = 1.0f

            Renderer2DData.putQuadVertex(
                position, size, color, texIndex, tilingFactor
            )

            Renderer2DData.QuadIndexCount += 6

            Renderer2DData.stats.quadCount++
        }

        fun drawQuad(position: Point2D, size: Vector2, color: Color) {
            drawQuad(Point3D(position.x, position.y, 0.0f), size, color)
        }

        /**
         * Tiling功能：Tint是着色、染色的东西，其实就是给Texture的 Color加上一个颜色的滤镜而已
         */
        fun drawQuad(
            position: Point3D, size: Vector2, texture: Texture2D, tilingFactor: Float = 1.0f
        ) {
            val texIndex: Float = XGLTextureSlots.requestIndex(texture).toFloat()

            Renderer2DData.putQuadVertex(
                position, size, Color.WHITE, texIndex, tilingFactor
            )

            Renderer2DData.QuadIndexCount += 6

            Renderer2DData.stats.quadCount++
        }

        fun drawQuad(
            position: Point2D,
            size: Vector2,
            texture: Texture2D,
            tilingFactor: Float = 1.0f
        ) {
            drawQuad(
                Point3D(position.x, position.y, 0.0f),
                size,
                texture,
                tilingFactor
            )
        }

        fun drawQuad(
            position: Point3D, size: Vector2, subTexture: SubTexture2D, tilingFactor: Float = 1.0f
        ) {
            val texIndex: Float = XGLTextureSlots.requestIndex(subTexture.mTextureAtlas).toFloat()

            Renderer2DData.putQuadVertex(
                position, size, Color.WHITE, subTexture, texIndex, tilingFactor
            )

            Renderer2DData.QuadIndexCount += 6

            Renderer2DData.stats.quadCount++
        }

        fun drawQuad(
            position: Point2D, size: Vector2, subTexture: SubTexture2D, tilingFactor: Float = 1.0f
        ) {
            drawQuad(
                Point3D(position.x, position.y, 0.0f),
                size,
                subTexture,
                tilingFactor
            )
        }

        fun drawRotatedQuad(
            position: Point3D,
            size: Vector2,
            rotationInDegree: Float,
            color: Color
        ) {
            val texIndex: Float = 0.0f // White Texture
            val tilingFactor: Float = 1.0f

            val transform: Mat4 = Mat4().transform2D(position.toVector3(), size, rotationInDegree)
            //LOG.info(transform.toString("Transform Matrix"))

            Renderer2DData.putVertex(
                (transform * Renderer2DData.QuadVertexPosition[0]).toPoint3D()!!,
                color,
                Vector2(0.0f, 0.0f),
                texIndex,
                tilingFactor
            )
            Renderer2DData.putVertex(
                (transform * Renderer2DData.QuadVertexPosition[1]).toPoint3D()!!,
                color,
                Vector2(1.0f, 0.0f),
                texIndex,
                tilingFactor
            )
            Renderer2DData.putVertex(
                (transform * Renderer2DData.QuadVertexPosition[2]).toPoint3D()!!,
                color,
                Vector2(1.0f, 1.0f),
                texIndex,
                tilingFactor
            )
            Renderer2DData.putVertex(
                (transform * Renderer2DData.QuadVertexPosition[3]).toPoint3D()!!,
                color,
                Vector2(0.0f, 1.0f),
                texIndex,
                tilingFactor
            )

            Renderer2DData.QuadIndexCount += 6

            Renderer2DData.stats.quadCount++
        }

        fun drawRotatedQuad(
            position: Point2D,
            size: Vector2,
            rotationInDegree: Float,
            color: Color
        ) {
            drawRotatedQuad(Point3D(position.x, position.y, 0.0f), size, rotationInDegree, color)
        }

        fun drawRotatedQuad(
            position: Point3D,
            size: Vector2,
            rotationInDegree: Float,
            texture: Texture2D,
            tilingFactor: Float = 1.0f,
            tintColor: Color = Color.WHITE
        ) {
            val texIndex: Float = XGLTextureSlots.requestIndex(texture).toFloat()

            val transform: Mat4 = Mat4().transform2D(position.toVector3(), size, rotationInDegree)
            //LOG.info(transform.toString("Transform Matrix"))

            Renderer2DData.putVertex(
                (transform * Renderer2DData.QuadVertexPosition[0]).toPoint3D()!!,
                tintColor,
                Vector2(0.0f, 0.0f),
                texIndex,
                tilingFactor
            )
            Renderer2DData.putVertex(
                (transform * Renderer2DData.QuadVertexPosition[1]).toPoint3D()!!,
                tintColor,
                Vector2(1.0f, 0.0f),
                texIndex,
                tilingFactor
            )
            Renderer2DData.putVertex(
                (transform * Renderer2DData.QuadVertexPosition[2]).toPoint3D()!!,
                tintColor,
                Vector2(1.0f, 1.0f),
                texIndex,
                tilingFactor
            )
            Renderer2DData.putVertex(
                (transform * Renderer2DData.QuadVertexPosition[3]).toPoint3D()!!,
                tintColor,
                Vector2(0.0f, 1.0f),
                texIndex,
                tilingFactor
            )

            Renderer2DData.QuadIndexCount += 6

            Renderer2DData.stats.quadCount++
        }

        fun drawRotatedQuad(
            position: Point2D,
            size: Vector2,
            rotationInDegree: Float,
            texture: Texture2D,
            tilingFactor: Float = 1.0f,
            tintColor: Color = Color.WHITE
        ) {
            drawRotatedQuad(
                Point3D(position.x, position.y, 0.0f),
                size,
                rotationInDegree,
                texture,
                tilingFactor,
                tintColor
            )
        }

        fun drawRotatedQuad(
            position: Point3D,
            size: Vector2,
            rotationInDegree: Float,
            subTexCoords: SubTexture2D,
            tilingFactor: Float = 1.0f,
            tintColor: Color = Color.WHITE
        ) {
            val texIndex: Float = XGLTextureSlots.requestIndex(subTexCoords.mTextureAtlas).toFloat()

            val transform: Mat4 = Mat4().transform2D(position.toVector3(), size, rotationInDegree)
            //LOG.info(transform.toString("Transform Matrix"))

            for (i in 0..3) {
                Renderer2DData.putVertex(
                    (transform * Renderer2DData.QuadVertexPosition[i]).toPoint3D()!!,
                    tintColor,
                    subTexCoords.mTexCoords[i],
                    texIndex,
                    tilingFactor
                )
            }

            Renderer2DData.QuadIndexCount += 6

            Renderer2DData.stats.quadCount++
        }

        fun drawRotatedQuad(
            position: Point2D,
            size: Vector2,
            rotationInDegree: Float,
            texture: SubTexture2D,
            tilingFactor: Float = 1.0f,
            tintColor: Color = Color.WHITE
        ) {
            drawRotatedQuad(
                Point3D(position.x, position.y, 0.0f),
                size,
                rotationInDegree,
                texture,
                tilingFactor,
                tintColor
            )
        }

    }

    object SceneData {
        var sProjectionMatrix: Mat4 = Mat4()
        var sViewMatrix: Mat4 = Mat4()
    }
}
