package com.focus617.app_demo.engine.d2

import com.focus617.app_demo.renderer.XGLTextureSlots
import com.focus617.core.engine.core.Layer
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.math.Vector2
import com.focus617.core.engine.math.Vector4
import com.focus617.core.engine.renderer.SubTexture2D
import com.focus617.core.engine.renderer.Texture2D
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventDispatcher

class GameLayer(name: String, private val scene: XGLScene2D) : Layer(name) {
    private val eventDispatcher = EventDispatcher()

    override fun initOpenGlResource() { }

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

    var rotation: Float = 0.0f
    override fun onUpdate(timeStep: TimeStep) {
        if (Renderer2DData.initialized && scene.initialized) {
            rotation += timeStep * 0.01f

//            XGLRenderer2D.drawQuad(Point3D(0f, 0f, 2f), Vector2(1f, 1f), RED)
//            //XGLRenderer2D.drawQuad(Vector2(0.5f, 0.5f), Vector2(0.75f, 0.5f), BLUE)
//            XGLRenderer2D.drawRotatedQuad(
//                Point3D(0f, -1.0f, 0.5f), Vector2(1.0f, 0.8f), rotation, BLUE
//            )
//
//            XGLRenderer2D.drawQuad(
//                Point3D(-5f, -5f, 0.1f),
//                Vector2(1f, 1f),
//                XGLTextureSlots.TextureSlots[XGLScene2D.textureCheckboxIndex] as Texture2D,
//                10f
//            )
//            XGLRenderer2D.drawRotatedQuad(
//                Point3D(0.0f, 1.0f, 0.1f),
//                Vector2(1.0f, 1.0f),
//                rotation,
//                XGLTextureSlots.TextureSlots[XGLScene2D.textureCheckboxIndex] as Texture2D,
//                20f
//            )

            // 构造SubTexture
            val textureTree = SubTexture2D.createFromCoords(
                XGLTextureSlots.TextureSlots[XGLScene2D.textureAltasIndex] as Texture2D,
                Vector2(0f, 10f),       // SubTexture Coords（原点在左上角，Y轴向下）
                Vector2(128f, 128f),    // sprite size
                Vector2(1f, 2f)         // cell size
            )
            val textureBarrel = SubTexture2D.createFromCoords(
                XGLTextureSlots.TextureSlots[XGLScene2D.textureAltasIndex] as Texture2D,
                Vector2(6f, 12f),        // SubTexture Coords
                Vector2(128f, 128f)     // SubTexture Size
            )
            // 使用SubTexture绘制
//            XGLRenderer2D.drawQuad(
//                Point3D(0f, 0f, 1f),
//                Vector2(1.0f, 2.0f),
//                textureTree,
//                1.0f
//            )
            XGLRenderer2D.drawQuad(
                Point3D(0f, 0f, 2f),
                Vector2(1.0f, 1.0f),
                textureBarrel,
                1.0f
            )
        }
    }

    override fun beforeDrawFrame() {
    }

    override fun afterDrawFrame() {
    }

    override fun onEvent(event: Event): Boolean {
        return eventDispatcher.dispatch(event)
    }

    companion object {
        val WHITE = Vector4(1.0f, 1.0f, 1.0f, 1.0f)
        val RED = Vector4(0.8f, 0.3f, 0.2f, 1.0f)
        val BLUE = Vector4(0.2f, 0.3f, 0.8f, 1.0f)

    }
}
