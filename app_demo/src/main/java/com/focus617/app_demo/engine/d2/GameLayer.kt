package com.focus617.app_demo.engine.d2

import com.focus617.core.engine.core.Layer
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.ecs.fleks.Entity
import com.focus617.core.engine.ecs.mine.component.Animation
import com.focus617.core.engine.ecs.mine.component.Sprite
import com.focus617.core.engine.ecs.mine.component.TransformMatrix
import com.focus617.core.engine.ecs.mine.static.Scene
import com.focus617.core.engine.ecs.mine.static.hasComponent
import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.math.Vector2
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.renderer.texture.SubTexture2D
import com.focus617.core.engine.renderer.texture.Texture2D
import com.focus617.core.engine.resource.baseDataType.Color
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventDispatcher
import com.focus617.opengles.renderer.texture.XGLTextureSlots

class GameLayer(name: String, private val resourceManager: XGL2DResourceManager) : Layer(name) {
    private val eventDispatcher = EventDispatcher()

    private val square: Entity

    init {
        val squareTransform = Mat4().transform2D(
            Vector3(0f, 0f, 0f), Vector2(0.75f, 0.75f), 0f
        )

        square = Scene.createEntity("Square") {
            add<TransformMatrix> { transform.setValue(squareTransform) }
            add<Sprite> { color = Color.GOLD }
        }

        LOG.info("Square has component Sprite = ${square.hasComponent<Sprite>()}")
        LOG.info("Square has component Animation = ${square.hasComponent<Animation>()}")
    }

    var rotationInDegree: Float = 0f
    override fun onUpdate(timeStep: TimeStep) {
        if (Renderer2DData.initialized && resourceManager.initialized) {
            // 使用SubTexture绘制
            XGLRenderer2D.drawQuad(
                Point3D(-0.5f, 1f, 0f),
                Vector2(0.25f, 0.5f),
                textureTree!!,
                1.0f
            )
            XGLRenderer2D.drawQuad(
                Point3D(0f, -1.5f, 0f),
                Vector2(0.25f, 0.25f),
                textureBarrel!!,
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

    override fun close() {
        LOG.info("${this.mDebugName} closed")
        eventDispatcher.close()
    }

    override fun beforeDrawFrame() {}

    override fun afterDrawFrame() {}

    override fun onEvent(event: Event): Boolean {
        return eventDispatcher.dispatch(event)
    }

    override fun onAttach() {
        LOG.info("${this.mDebugName} onAttach()")
    }

    override fun onDetach() {
        LOG.info("${this.mDebugName} onDetach")
    }

    companion object {
        private var textureTree: SubTexture2D? = null
        private var textureBarrel: SubTexture2D? = null
    }

}
