package com.focus617.app_demo.engine.d2

import com.focus617.app_demo.renderer.XGLTextureSlots
import com.focus617.core.engine.core.Layer
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.math.Point2D
import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.math.Vector2
import com.focus617.core.engine.math.Vector4
import com.focus617.core.engine.renderer.SubTexture2D
import com.focus617.core.engine.renderer.Texture2D
import com.focus617.core.engine.renderer.XRenderer
import com.focus617.core.engine.scene.Camera
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventDispatcher

class GameLayer(name: String, private val scene: XGLScene2D, val is3D: Boolean) : Layer(name) {
    private val eventDispatcher = EventDispatcher()

    override fun onAttach() {
        LOG.info("${this.mDebugName} onAttach()")
    }

    override fun onDetach() {
        LOG.info("${this.mDebugName} onDetach")
    }

    var rotation: Float = 0.0f
    override fun onUpdate(timeStep: TimeStep) {
        if (Renderer2DData.initialized && scene.initialized) {
            rotation += timeStep * 0.01f

            beginScene(scene.mCamera)
            XGLRenderer2D.drawQuad(Point2D(-0.8f, 1.0f), Vector2(0.5f, 0.8f), RED)
            //XGLRenderer2D.drawQuad(Vector2(0.5f, 0.5f), Vector2(0.75f, 0.5f), BLUE)
            XGLRenderer2D.drawRotatedQuad(
                Point2D(0f, -1.0f), Vector2(1.0f, 0.8f), rotation, BLUE
            )

            XGLRenderer2D.drawQuad(
                Point3D(-5f, -5f, -0.1f),
                Vector2(10f, 10f),
                XGLTextureSlots.TextureSlots[XGLScene2D.textureCheckboxIndex] as Texture2D,
                10f
            )
            XGLRenderer2D.drawRotatedQuad(
                Point2D(0.0f, 1.0f),
                Vector2(1.0f, 1.0f),
                rotation,
                XGLTextureSlots.TextureSlots[XGLScene2D.textureCheckboxIndex] as Texture2D,
                20f
            )

            var subTexture2D = SubTexture2D.createFromCoords(
                XGLTextureSlots.TextureSlots[XGLScene2D.textureAltasIndex] as Texture2D,
                Vector2(0f, 5f),        // SubTexture Coords
                Vector2(128f, 256f)     // SubTexture Size
            )

            XGLRenderer2D.drawQuad(
                Point3D(-0.5f, -1.0f, 0.01f),
                Vector2(1.0f, 2.0f),
                subTexture2D,
                1.0f
            )

            subTexture2D = SubTexture2D.createFromCoords(
                XGLTextureSlots.TextureSlots[XGLScene2D.textureAltasIndex] as Texture2D,
                Vector2(6f, 12f),        // SubTexture Coords
                Vector2(128f, 128f)     // SubTexture Size
            )

            XGLRenderer2D.drawQuad(
                Point3D(-0.5f, -1.2f, 0.02f),
                Vector2(1.0f, 1.0f),
                subTexture2D,
                1.0f
            )

            endScene()
        }
    }

    fun beginScene(camera: Camera) {
        // 在多线程渲染里，会把BeginScene函数放在RenderCommandQueue里执行
        // camera在多线程渲染的时候不能保证主线程是否正在更改Camera的相关信息
        synchronized(camera) {
            System.arraycopy(camera.getProjectionMatrix(), 0, XRenderer.sProjectionMatrix, 0, 16)
            System.arraycopy(camera.getViewMatrix(), 0, XRenderer.sViewMatrix, 0, 16)
        }

        Renderer2DData.resetVertexBuffer()
        XGLTextureSlots.resetTextureSlot()
    }

    fun endScene() {
//        LOG.info(
//            "Statistic: drawCalls=${Renderer2DData.stats.drawCalls}," +
//                    " quadCount=${Renderer2DData.stats.quadCount}"
//        )
    }

    override fun onEvent(event: Event): Boolean {
        return eventDispatcher.dispatch(event)
    }

    override fun close() {
        LOG.info("${this.mDebugName} closed")
        eventDispatcher.close()
    }

    companion object {
        val WHITE = Vector4(1.0f, 1.0f, 1.0f, 1.0f)
        val RED = Vector4(0.8f, 0.3f, 0.2f, 1.0f)
        val BLUE = Vector4(0.2f, 0.3f, 0.8f, 1.0f)

    }
}
