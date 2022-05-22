package com.focus617.app_demo.engine.d2

import com.focus617.core.engine.core.Layer
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.math.Vector2
import com.focus617.core.engine.renderer.framebuffer.FrameBufferAttachmentSpecification
import com.focus617.core.engine.renderer.framebuffer.FrameBufferSpecification
import com.focus617.core.engine.renderer.framebuffer.FrameBufferTextureFormat
import com.focus617.core.engine.renderer.framebuffer.FrameBufferTextureSpecification
import com.focus617.core.engine.renderer.texture.SubTexture2D
import com.focus617.core.engine.renderer.texture.Texture2D
import com.focus617.core.platform.event.base.Event
import com.focus617.opengles.renderer.framebuffer.XGLFrameBuffer
import com.focus617.opengles.renderer.framebuffer.XGLFrameBufferBuilder
import com.focus617.opengles.renderer.texture.XGLTextureSlots

class MapLayer(name: String, private val scene: XGLScene2D) : Layer(name) {
    var initialized: Boolean = false
    lateinit var textureDirty: SubTexture2D
    lateinit var textureWater: SubTexture2D
    lateinit var textureTree: SubTexture2D
    lateinit var textureMilestone: SubTexture2D

    private var mFramebuffer: XGLFrameBuffer? = null

    override fun initOpenGlResource() {
        val fbSpec = FrameBufferSpecification()
        fbSpec.attachment = FrameBufferAttachmentSpecification(
            listOf(
                FrameBufferTextureSpecification(FrameBufferTextureFormat.RGBA8),
                FrameBufferTextureSpecification(FrameBufferTextureFormat.DEPTH24STENCIL8)
            )
        )
        fbSpec.mWidth = 1024
        fbSpec.mHeight = 2048
        mFramebuffer = XGLFrameBufferBuilder.createFrameBuffer(fbSpec) as XGLFrameBuffer
    }

    override fun close() {
        LOG.info("${this.mDebugName} closed")
    }

    // 当Layer添加到LayerStack的时候会调用此函数，相当于Init函数
    override fun onAttach() {
        LOG.info("${this.mDebugName} onAttach()")

    }
    //当Layer从LayerStack移除的时候会调用此函数，相当于Shutdown函数
    override fun onDetach() {
        LOG.info("${this.mDebugName} onDetach")
    }

    override fun onUpdate(timeStep: TimeStep) {
        //LOG.info("${this.mDebugName} onUpdate")
        mFramebuffer?.bind()

        if (Renderer2DData.initialized && scene.initialized) {
            if (!this.initialized) initSubTexture()

            val tileSize = TileSize
            val height = Row * tileSize
            val width = Column * tileSize

            for (y in 0 until 16)
                for (x in 0 until 8) {
                    val t = sMapTiles[x + y * 8]
                    val texSub = sMap[t] ?: textureMilestone
                    // y轴坐标取相反数, 是为了跟绘制的地图char数组相同
                    val xPos: Float = x.toFloat() * tileSize - width / 2
                    val yPos: Float = (height / 2) - (y.toFloat() + 1) * tileSize
                    // 使用SubTexture绘制
                    XGLRenderer2D.drawQuad(
                        Point3D(xPos, yPos, 1f),
                        Vector2(tileSize, tileSize),
                        texSub,
                        1.0f
                    )
                }
        }

        mFramebuffer?.unbind()
    }

    override fun beforeDrawFrame() {
    }

    override fun afterDrawFrame() {
    }

    override fun onEvent(event: Event): Boolean {
        //LOG.info("${this.mDebugName} onEvent")
        return false
    }

    fun initSubTexture() {
        LOG.info("${this.mDebugName} onUpdate - initialize sub-texture")
        // 构造SubTexture
        textureDirty = SubTexture2D.createFromCoords(
            XGLTextureSlots.TextureSlots[XGLScene2D.textureAltasIndex] as Texture2D,
            Vector2(6f, 1f),            // SubTexture Coords（原点在左上角，Y轴向下）
            Vector2(SpriteSize, SpriteSize), // sprite size
            Vector2(1f, 1f)             // cell size
        )
        sMap['D'] = textureDirty

        textureWater = SubTexture2D.createFromCoords(
            XGLTextureSlots.TextureSlots[XGLScene2D.textureAltasIndex] as Texture2D,
            Vector2(11f, 1f),           // SubTexture Coords
            Vector2(SpriteSize, SpriteSize)  // sprite size
        )
        sMap['W'] = textureWater

        textureTree = SubTexture2D.createFromCoords(
            XGLTextureSlots.TextureSlots[XGLScene2D.textureAltasIndex] as Texture2D,
            Vector2(0f, 10f),           // SubTexture Coords（原点在左上角，Y轴向下）
            Vector2(SpriteSize, SpriteSize), // sprite size
            Vector2(1f, 2f)             // cell size
        )
        sMap['T'] = textureTree

        textureMilestone = SubTexture2D.createFromCoords(
            XGLTextureSlots.TextureSlots[XGLScene2D.textureAltasIndex] as Texture2D,
            Vector2(9f, 10f),           // SubTexture Coords
            Vector2(SpriteSize, SpriteSize)  // sprite size
        )
        sMap['S'] = textureMilestone

        this.initialized = true
    }

    companion object {
        // 16行8列
        private const val Row = 16
        private const val Column = 8
        private const val TileSize = 0.23f      // 实际绘制时，每个Quad的边长

        private const val SpriteSize = 128f     // 纹理图 SubTexture的取样单位

        // 这种写法其实代表一个长字符串, D代表Dirt土地Tile, W代表Water Tile, S代表路标Tile
        // 注意第一个Tile为D, 虽然在数组里坐标为(0,0), 但是在屏幕上对应的坐标应该是(0,1)
        val sMapTiles: CharArray = (
                "WWWWWWWW" +
                        "WWWDWWWW" +
                        "WWWDDWWW" +
                        "WWDDDDWW" +
                        "WDDSTDDW" +
                        "WWDDDDWW" +
                        "WWWDDWWW" +
                        "WWWDWWWW" +
                        "WWWWWWWW" +
                        "WWWDWWWW" +
                        "WWDDDWWW" +
                        "WDDTDDWW" +
                        "WDDSSDDW" +
                        "WWWDDWWW" +
                        "WWWDDWWW" +
                        "WWWDDWWW"
                ).toCharArray()
    }

    val sMap = HashMap<Char, SubTexture2D>()    // SubTexture字典，KEY为 sMapTiles中的Char

}