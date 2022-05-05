package com.focus617.app_demo.engine.d2

import android.content.Context
import android.opengl.GLSurfaceView
import com.focus617.app_demo.engine.XGLContext
import com.focus617.app_demo.renderer.XGLTextureSlots
import com.focus617.core.engine.baseDataType.Color
import com.focus617.core.engine.math.Vector2
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.Vector4
import com.focus617.core.engine.math.XMatrix
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
        with(Renderer2DData) {
            TextureShader.bind()
            TextureShader.setMat4("u_ProjectionMatrix", SceneData.sProjectionMatrix)
            TextureShader.setMat4("u_ViewMatrix", SceneData.sViewMatrix)

            val modelMatrix: FloatArray = FloatArray(16)
            XMatrix.setIdentityM(modelMatrix, 0)
            TextureShader.setMat4("u_ModelMatrix", modelMatrix)
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
        XGLTextureSlots.flush()

        with(Renderer2DData) {
            QuadVertexArray.bind()
            RenderCommand.drawIndexed(QuadVertexArray, QuadIndexCount)
        }
    }

    companion object {
        fun drawQuad(position: Vector3, size: Vector2, color: Vector4) {
            val texIndex: Float = 0.0f // White Texture
            val tilingFactor: Float = 1.0f

            Renderer2DData.put(position)
            Renderer2DData.put(color)
            Renderer2DData.put(Vector2(0.0f, 0.0f))
            Renderer2DData.put(texIndex)
            Renderer2DData.put(tilingFactor)

            Renderer2DData.put(Vector3(position.x + size.x, position.y, 0.0f))
            Renderer2DData.put(color)
            Renderer2DData.put(Vector2(1.0f, 0.0f))
            Renderer2DData.put(texIndex)
            Renderer2DData.put(tilingFactor)

            Renderer2DData.put(Vector3(position.x + size.x, position.y + size.y, 0.0f))
            Renderer2DData.put(color)
            Renderer2DData.put(Vector2(1.0f, 1.0f))
            Renderer2DData.put(texIndex)
            Renderer2DData.put(tilingFactor)

            Renderer2DData.put(Vector3(position.x, position.y + size.y, 0.0f))
            Renderer2DData.put(color)
            Renderer2DData.put(Vector2(0.0f, 1.0f))
            Renderer2DData.put(texIndex)
            Renderer2DData.put(tilingFactor)

            Renderer2DData.QuadIndexCount += 6
        }

        fun drawQuad(position: Vector2, size: Vector2, color: Vector4) {
            drawQuad(Vector3(position.x, position.y, 0.0f), size, color)
        }

        fun drawQuad(
            position: Vector3, size: Vector2, texture: Texture2D, tilingFactor: Float = 1.0f,
            tintColor: Vector4 = Renderer2DData.WHITE
        ) {
            val textureIndex: Float = XGLTextureSlots.getId(texture).toFloat()

            Renderer2DData.put(position)
            Renderer2DData.put(Renderer2DData.WHITE)
            Renderer2DData.put(Vector2(0.0f, 0.0f))
            Renderer2DData.put(textureIndex)
            Renderer2DData.put(tilingFactor)

            Renderer2DData.put(Vector3(position.x + size.x, position.y, 0.0f))
            Renderer2DData.put(Renderer2DData.WHITE)
            Renderer2DData.put(Vector2(1.0f, 0.0f))
            Renderer2DData.put(textureIndex)
            Renderer2DData.put(tilingFactor)

            Renderer2DData.put(Vector3(position.x + size.x, position.y + size.y, 0.0f))
            Renderer2DData.put(Renderer2DData.WHITE)
            Renderer2DData.put(Vector2(1.0f, 1.0f))
            Renderer2DData.put(textureIndex)
            Renderer2DData.put(tilingFactor)

            Renderer2DData.put(Vector3(position.x, position.y + size.y, 0.0f))
            Renderer2DData.put(Renderer2DData.WHITE)
            Renderer2DData.put(Vector2(0.0f, 1.0f))
            Renderer2DData.put(textureIndex)
            Renderer2DData.put(tilingFactor)

            Renderer2DData.QuadIndexCount += 6
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

        fun drawRotatedQuad(
            position: Vector3,
            size: Vector2,
            rotationInDegree: Float,
            color: Vector4
        ) {
            val texIndex: Float = 0.0f // White Texture
            val tilingFactor: Float = 1.0f

            val transform: FloatArray = getTransform(position, size, rotationInDegree)

            Renderer2DData.put(
                vector3AfterTransform(Renderer2DData.QuadVertexPosition[0], transform)
            )
            Renderer2DData.put(color)
            Renderer2DData.put(Vector2(0.0f, 0.0f))
            Renderer2DData.put(texIndex)
            Renderer2DData.put(tilingFactor)

            Renderer2DData.put(
                vector3AfterTransform(Renderer2DData.QuadVertexPosition[1], transform)
            )
            Renderer2DData.put(color)
            Renderer2DData.put(Vector2(1.0f, 0.0f))
            Renderer2DData.put(texIndex)
            Renderer2DData.put(tilingFactor)

            Renderer2DData.put(
                vector3AfterTransform(Renderer2DData.QuadVertexPosition[2], transform)
            )
            Renderer2DData.put(color)
            Renderer2DData.put(Vector2(1.0f, 1.0f))
            Renderer2DData.put(texIndex)
            Renderer2DData.put(tilingFactor)

            Renderer2DData.put(
                vector3AfterTransform(Renderer2DData.QuadVertexPosition[3], transform)
            )
            Renderer2DData.put(color)
            Renderer2DData.put(Vector2(0.0f, 1.0f))
            Renderer2DData.put(texIndex)
            Renderer2DData.put(tilingFactor)

            Renderer2DData.QuadIndexCount += 6
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
            val textureIndex: Float = XGLTextureSlots.getId(texture).toFloat()

            val transform: FloatArray = getTransform(position, size, rotationInDegree)

            Renderer2DData.put(
                vector3AfterTransform(Renderer2DData.QuadVertexPosition[0], transform)
            )
            Renderer2DData.put(Renderer2DData.WHITE)
            Renderer2DData.put(Vector2(0.0f, 0.0f))
            Renderer2DData.put(textureIndex)
            Renderer2DData.put(tilingFactor)

            Renderer2DData.put(
                vector3AfterTransform(Renderer2DData.QuadVertexPosition[1], transform)
            )
            Renderer2DData.put(Renderer2DData.WHITE)
            Renderer2DData.put(Vector2(1.0f, 0.0f))
            Renderer2DData.put(textureIndex)
            Renderer2DData.put(tilingFactor)

            Renderer2DData.put(
                vector3AfterTransform(Renderer2DData.QuadVertexPosition[2], transform)
            )
            Renderer2DData.put(Renderer2DData.WHITE)
            Renderer2DData.put(Vector2(1.0f, 1.0f))
            Renderer2DData.put(textureIndex)
            Renderer2DData.put(tilingFactor)

            Renderer2DData.put(
                vector3AfterTransform(Renderer2DData.QuadVertexPosition[3], transform)
            )
            Renderer2DData.put(Renderer2DData.WHITE)
            Renderer2DData.put(Vector2(0.0f, 1.0f))
            Renderer2DData.put(textureIndex)
            Renderer2DData.put(tilingFactor)

            Renderer2DData.QuadIndexCount += 6

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


        private fun getTransform(
            position: Vector3,
            size: Vector2,
            rotationInDegree: Float = 0.0f
        ): FloatArray {
            val transform = FloatArray(16)
            XMatrix.setIdentityM(transform, 0)
            XMatrix.scaleM(transform, 0, size.x, size.y, 1.0f)
            XMatrix.rotateM(transform, 0, rotationInDegree, 0.0f, 0.0f, 1.0f)
            XMatrix.translateM(transform, 0, position)
            return transform
        }

        private fun vector3AfterTransform(vector4: Vector4, transform: FloatArray): Vector3 {
            val result = FloatArray(4)
            XMatrix.xMultiplyMV(result, 0, transform, 0, vector4)
            return Vector4(result).toVector3()
        }
    }
}
