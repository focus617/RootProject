package com.focus617.app_demo.engine.d2

import com.focus617.app_demo.renderer.XGLTextureSlots
import com.focus617.core.engine.core.Layer
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.math.Vector2
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.Vector4
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
            XGLRenderer2D.drawQuad(Vector2(-0.8f, 1.0f), Vector2(0.5f, 0.8f), RED)
            //XGLRenderer2D.drawQuad(Vector2(0.5f, 0.5f), Vector2(0.75f, 0.5f), BLUE)
            XGLRenderer2D.drawRotatedQuad(
                Vector2(0f, -1.0f), Vector2(1.0f, 0.8f), rotation, BLUE
            )

            XGLRenderer2D.drawQuad(
                Vector3(-5f, -5f, -0.1f),
                Vector2(10f, 10f),
                XGLTextureSlots.TextureSlots[XGLScene2D.textureIndex] as Texture2D,
                10f
            )
            XGLRenderer2D.drawRotatedQuad(
                Vector2(0.0f, 1.0f),
                Vector2(1.0f, 1.0f),
                rotation,
                XGLTextureSlots.TextureSlots[XGLScene2D.textureIndex] as Texture2D,
                20f
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

        with(Renderer2DData) {
            QuadVertexBufferPtr = 0
            QuadIndexCount = 0
        }

        XGLTextureSlots.TextureSlotIndex = 1
    }

    fun endScene() {
        LOG.info("Statistic: drawCalls=${Renderer2DData.stats.drawCalls}," +
                " quadCount=${Renderer2DData.stats.quadCount}")
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
