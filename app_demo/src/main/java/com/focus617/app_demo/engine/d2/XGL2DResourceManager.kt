package com.focus617.app_demo.engine.d2

import android.content.Context
import com.focus617.core.engine.renderer.shader.ShaderLibrary
import com.focus617.core.platform.base.BaseEntity
import com.focus617.opengles.renderer.framebuffer.FrameBufferEntity
import com.focus617.opengles.renderer.texture.XGLTextureBuilder
import com.focus617.opengles.renderer.texture.XGLTextureSlots
import com.focus617.opengles.scene_graph.XGLDrawableObject
import java.io.Closeable

/**
 * Scene is root entity for all Game Entities.
 */
class XGL2DResourceManager(val context: Context, val engine: Sandbox2D) : BaseEntity(), Closeable {
    val mShaderLibrary = ShaderLibrary()

    override fun close() {
        mShaderLibrary.close()
    }

    var initialized: Boolean = false

    fun initOpenGlResource() {
        FrameBufferEntity.initShader(context)
        XGLTextureSlots.initUnderOpenGl()
        Renderer2DData.initRenderer2DData(context)     // 初始化本Render的静态数据
        initTexture()
        initGameObjects()
        initialized = true
    }

    private fun initTexture() {
//        var texture = XGLTextureBuilder.createTexture(
//            context, TextureCheckboxFilePath
//        )!!
//        textureCheckboxIndex = XGLTextureSlots.requestIndex(texture)

        val texture = XGLTextureBuilder.createTexture(
            context, TextureAtlasFilePath
        )!!
        textureAltasIndex = XGLTextureSlots.requestIndex(texture)
    }

    // TODO: My thought: I may call it in renderer.onSurfaceChanged when 2D GO ready,
    //  poll each game object for opengl related initialization(vertexArray & textures).
    private fun initGameObjects() {
        val layerStack = engine.getLayerStack()
        for (layer in layerStack.mLayers) {
            layer.initOpenGlResource()
            for (gameObject in layer.gameObjectList) {
                (gameObject as XGLDrawableObject).initOpenGlResource()
            }
        }
    }

    companion object {

        //TODO: I have no idea about 2D game object, put its data here temporarily
        private val PATH = "Quad"
        private val TEXTURE_FILE_CKBoard = "Checkerboard.png"
        private val TEXTURE_FILE_Atlas = "TextureAtlas.png"

        val TextureCheckboxFilePath = "$PATH/$TEXTURE_FILE_CKBoard"
        val TextureAtlasFilePath = "$PATH/$TEXTURE_FILE_Atlas"

        var textureCheckboxIndex: Int = -1
        var textureAltasIndex: Int = -1
    }

}