package com.focus617.app_demo.engine.d2

import com.focus617.core.engine.core.Layer
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.math.Vector2
import com.focus617.core.engine.renderer.texture.SubTexture2D
import com.focus617.core.engine.renderer.texture.Texture2D
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventDispatcher
import com.focus617.opengles.renderer.texture.XGLTextureSlots

class GameLayer(name: String, private val resourceManager: XGL2DResourceManager) : Layer(name) {
    private val eventDispatcher = EventDispatcher()

    private lateinit var textureTree: SubTexture2D
    private lateinit var textureBarrel: SubTexture2D

    override fun close() {
        LOG.info("${this.mDebugName} closed")
        eventDispatcher.close()
    }

    override fun onAttach() {
        LOG.info("${this.mDebugName} onAttach()")
    }

    override fun onDetach() {
        LOG.info("${this.mDebugName} onDetach")
    }

    override fun onUpdate(timeStep: TimeStep) {
        if (Renderer2DData.initialized && resourceManager.initialized) {

            // 使用SubTexture绘制
            XGLRenderer2D.drawQuad(
                Point3D(0f, 0f, 1f),
                Vector2(1.0f, 2.0f),
                textureTree,
                1.0f
            )
            XGLRenderer2D.drawQuad(
                Point3D(0f, 0f, 2f),
                Vector2(1.0f, 1.0f),
                textureBarrel,
                1.0f
            )
        }
    }

    override fun initOpenGlResource() {
        // 构造SubTexture
        textureTree = SubTexture2D.createFromCoords(
            XGLTextureSlots.TextureSlots[XGL2DResourceManager.textureAltasIndex] as Texture2D,
            Vector2(0f, 10f),       // SubTexture Coords（原点在左上角，Y轴向下）
            Vector2(128f, 128f),    // sprite size
            Vector2(1f, 2f)         // cell size
        )

        textureBarrel = SubTexture2D.createFromCoords(
            XGLTextureSlots.TextureSlots[XGL2DResourceManager.textureAltasIndex] as Texture2D,
            Vector2(6f, 12f),        // SubTexture Coords
            Vector2(128f, 128f)     // SubTexture Size
        )
    }

    override fun beforeDrawFrame() {}
    override fun afterDrawFrame() {}

    override fun onEvent(event: Event): Boolean {
        return eventDispatcher.dispatch(event)
    }

}
