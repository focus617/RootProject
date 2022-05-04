package com.focus617.app_demo.engine

import android.content.Context
import com.focus617.app_demo.renderer.XGLShader
import com.focus617.app_demo.renderer.XGLShaderBuilder
import com.focus617.app_demo.renderer.XGLTextureCubeMap
import com.focus617.app_demo.renderer.XGLVertexArray
import com.focus617.app_demo.terrain.Heightmap
import com.focus617.app_demo.terrain.SkyBox
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
            SkyBox.SkyBoxShaderFilePath
        ) as XGLShader
        mShaderLibrary.add(shader)

        shader = XGLShaderBuilder.createShader(
            context,
            Heightmap.HeightMapShaderFilePath
        ) as XGLShader
        mShaderLibrary.add(shader)
    }

    private fun initTexture() {
        val texture = XGLTextureCubeMap(
            context,
            SkyBox.SKYBOX_SHADER_PATH,
            arrayOf("left.png", "right.png", "bottom.png", "top.png", "front.png", "back.png")
        )
        mTextureLibrary.add(texture)

//        var texture2D = XGLTextureBuilder.createTexture(context, Heightmap.HeightMapGrassFilePath)
//        mTextureLibrary.add(texture2D!!)
//        texture2D = XGLTextureBuilder.createTexture(context, Heightmap.HeightMapStoneFilePath)
//        mTextureLibrary.add(texture2D!!)
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

}