package com.focus617.app_demo.engine.d2

import android.content.Context
import com.focus617.app_demo.engine.Sandbox
import com.focus617.app_demo.renderer.XGLTextureBuilder
import com.focus617.app_demo.renderer.XGLTextureSlots
import com.focus617.app_demo.renderer.XGLVertexArray
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.scene.OrthographicCamera
import com.focus617.core.engine.scene.OrthographicCameraController
import com.focus617.core.engine.scene.Scene
import kotlin.properties.Delegates

class XGLScene2D(val context: Context, val engine: Sandbox) : Scene() {
    var initialized: Boolean = false

    init {
        mCamera = OrthographicCamera()
        mCameraController = OrthographicCameraController(mCamera as OrthographicCamera)
    }

    fun initOpenGlResource() {
        initTexture()
        initGameObjects()
        initialized = true
    }

    private fun initTexture() {
        var texture = XGLTextureBuilder.createTexture(
            context, TextureCheckboxFilePath
        )!!
        textureCheckboxIndex = XGLTextureSlots.getId(texture)

        texture = XGLTextureBuilder.createTexture(
            context, TextureAtlasFilePath
        )!!
        textureAltasIndex = XGLTextureSlots.getId(texture)
    }

    // TODO: My thought: I may call it in renderer.onSurfaceChanged when 2D GO ready,
    //  poll each game object for opengl related initialization(vertexArray & textures).
    private fun initGameObjects() {
        val layerStack = engine.getLayerStack()
        for (layer in layerStack.mLayers)
            for (gameObject in layer.gameObjectList) {
                gameObject.vertexArray = XGLVertexArray.buildVertexArray(gameObject)
            }

    }

    // Used for updating the global resource, such as objects in scene
    override fun onUpdate(timeStep: TimeStep) {
        // Update Camera
        mCameraController.onUpdate(timeStep)

    }

    companion object {

        //TODO: I have no idea about 2D game object, put its data here temporarily
        private val PATH = "Quad"
        private val TEXTURE_FILE_CKBoard = "Checkerboard.png"
        private val TEXTURE_FILE_Atlas = "TextureAtlas.png"

        val TextureCheckboxFilePath = "$PATH/$TEXTURE_FILE_CKBoard"
        val TextureAtlasFilePath = "$PATH/$TEXTURE_FILE_Atlas"

        var textureCheckboxIndex by Delegates.notNull<Int>()
        var textureAltasIndex by Delegates.notNull<Int>()
    }

}