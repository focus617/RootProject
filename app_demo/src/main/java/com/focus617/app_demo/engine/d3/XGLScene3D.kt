package com.focus617.app_demo.engine.d3

import android.content.Context
import com.focus617.app_demo.renderer.*
import com.focus617.app_demo.renderer.framebuffer.FrameBufferQuad
import com.focus617.app_demo.terrain.Heightmap
import com.focus617.app_demo.terrain.SkyBox
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.scene.PerspectiveCamera
import com.focus617.core.engine.scene.PerspectiveCameraController
import com.focus617.core.engine.scene.Scene

class XGLScene3D(val context: Context, val engine: Sandbox3D) : Scene() {

    init {
        mCamera = PerspectiveCamera()
        mCameraController =PerspectiveCameraController(mCamera as PerspectiveCamera)
    }

    fun initOpenGlResource() {
        initShader()
        initTexture()
        initGameObjects()
    }

    private fun initShader() {
        FrameBufferQuad.shader = XGLShaderBuilder.createShader(
            context,
            FrameBufferQuad.ShaderFilePath
        ) as XGLShader
        mShaderLibrary.add(FrameBufferQuad.shader)

        FrameBufferQuad.shaderOutlining = XGLShaderBuilder.createShader(
            context,
            FrameBufferQuad.ShaderOutliningFilePath
        ) as XGLShader
        mShaderLibrary.add(FrameBufferQuad.shaderOutlining)

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

        shader = XGLShaderBuilder.createShader(
            context,
            Earth.ShaderFilePath
        ) as XGLShader
        mShaderLibrary.add(shader)

        shader = XGLShaderBuilder.createShader(
            context,
            Box.ShaderFilePath
        ) as XGLShader
        mShaderLibrary.add(shader)
    }

    private fun initTexture() {
        val texture = XGLTextureCubeMap(
            context,
            SkyBox.SKYBOX_SHADER_PATH,
            arrayOf("left.png", "right.png", "bottom.png", "top.png", "front.png", "back.png")
        )
        XGLTextureSlots.TextureSlots[0] = texture

        val textureGrass = XGLTextureBuilder.createTexture(context, Heightmap.HeightMapGrassFilePath)
        textureGrass?.apply {
            Heightmap.textureIndexGrass = XGLTextureSlots.getId(textureGrass)
       }

        val textureStone = XGLTextureBuilder.createTexture(context, Heightmap.HeightMapStoneFilePath)
        textureStone?.apply {
            Heightmap.textureIndexStone = XGLTextureSlots.getId(textureStone)
        }

        val textureEarthDay = XGLTextureBuilder.createTexture(context, Earth.DayTextureFilePath)
        textureEarthDay?.apply {
            Earth.textureIndexDay = XGLTextureSlots.getId(textureEarthDay)
        }

        val textureEarthNight = XGLTextureBuilder.createTexture(context, Earth.NightTextureFilePath)
        textureEarthNight?.apply {
            Earth.textureIndexNight = XGLTextureSlots.getId(textureEarthNight)
        }

        val textureBox = XGLTextureBuilder.createTexture(context, Box.TextureFilePath)
        textureBox?.apply {
            Box.textureIndex = XGLTextureSlots.getId(textureBox)
        }
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