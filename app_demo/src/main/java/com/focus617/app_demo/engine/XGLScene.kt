package com.focus617.app_demo.engine

import android.content.Context
import com.focus617.app_demo.renderer.*
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.scene.*

class XGLScene(
    val context: Context,
    val engine: Sandbox,
    is3D: Boolean
) : Scene() {

    init {
        mCamera = if (is3D) PerspectiveCamera() else OrthographicCamera()
        mCameraController =
            if (is3D) PerspectiveCameraController(mCamera as PerspectiveCamera)
            else OrthographicCameraController(mCamera as OrthographicCamera)
    }

    fun initOpenGlResource() {
        initShader()
        initTexture()
        initGameObjects()
    }

    private fun initShader() {
        var shader = XGLShaderBuilder.createShader(
            context,
            SkyBoxShaderFilePath
        ) as XGLShader
        mShaderLibrary.add(shader)

        shader = XGLShaderBuilder.createShader(
            context,
            HeightMapShaderFilePath
        ) as XGLShader
        mShaderLibrary.add(shader)
    }

    private fun initTexture() {
        val texture = XGLTextureCubeMap(
            context,
            SKYBOX_SHADER_PATH,
            arrayOf("left.png", "right.png", "bottom.png", "top.png", "front.png", "back.png")
        )
        mTextureLibrary.add(texture)

        var texture2D = XGLTextureBuilder.createTexture(context, HeightMapGrassFilePath)
        mTextureLibrary.add(texture2D!!)
        texture2D = XGLTextureBuilder.createTexture(context, HeightMapStoneFilePath)
        mTextureLibrary.add(texture2D!!)
    }

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
        private val SKYBOX_SHADER_PATH = "SkyBox"
        private val SKYBOX_SHADER_FILE = "SkyBox.glsl"

        private val HEIGHTMAP_SHADER_PATH = "HeightMap"
        private val HEIGHTMAP_SHADER_FILE = "heightmap.glsl"
        private val HEIGHTMAP_BITMAP_FILE = "heightmap.png"
        private val HEIGHTMAP_GRASS_TEXTURE_FILE = "noisy_grass_public_domain.png"
        private val HEIGHTMAP_STONE_TEXTURE_FILE = "stone_public_domain.png"

        val SkyBoxShaderFilePath: String = "$SKYBOX_SHADER_PATH/$SKYBOX_SHADER_FILE"
        val SkyBoxTextureFilePath: String = "$SKYBOX_SHADER_PATH/SkyBox"

        val HeightMapShaderFilePath: String = "$HEIGHTMAP_SHADER_PATH/$HEIGHTMAP_SHADER_FILE"
        val HeightMapBitmapFilePath: String = "$HEIGHTMAP_SHADER_PATH/$HEIGHTMAP_BITMAP_FILE"
        val HeightMapGrassFilePath: String = "$HEIGHTMAP_SHADER_PATH/$HEIGHTMAP_GRASS_TEXTURE_FILE"
        val HeightMapStoneFilePath: String = "$HEIGHTMAP_SHADER_PATH/$HEIGHTMAP_STONE_TEXTURE_FILE"
    }
}