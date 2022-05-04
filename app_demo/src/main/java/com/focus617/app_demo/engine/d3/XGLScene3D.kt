package com.focus617.app_demo.engine.d3

import android.content.Context
import android.opengl.GLES31
import com.focus617.app_demo.engine.Sandbox
import com.focus617.app_demo.renderer.*
import com.focus617.app_demo.terrain.Heightmap
import com.focus617.app_demo.terrain.SkyBox
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.renderer.TextureSlots
import com.focus617.core.engine.scene.PerspectiveCamera
import com.focus617.core.engine.scene.PerspectiveCameraController
import com.focus617.core.engine.scene.Scene
import com.focus617.platform.helper.TextureHelper

class XGLScene3D(val context: Context, val engine: Sandbox) : Scene() {

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
        TextureSlots.TextureSlots[0] = texture

        val textureGrass = XGLTextureBuilder.createTexture(context, Heightmap.HeightMapGrassFilePath)
        textureGrass?.apply {
            Heightmap.textureIndexGrass = TextureSlots.getId(textureGrass)
            textureGrass.bind(Heightmap.textureIndexGrass)
            //绑定纹理单元与sampler
            GLES31.glBindSampler(Heightmap.textureIndexGrass, TextureHelper.samplers[0])
        }

        val textureStone = XGLTextureBuilder.createTexture(context, Heightmap.HeightMapStoneFilePath)
        textureStone?.apply {
            Heightmap.textureIndexStone = TextureSlots.getId(textureStone)
            textureStone.bind(Heightmap.textureIndexStone)
            //绑定纹理单元与sampler
            GLES31.glBindSampler(Heightmap.textureIndexStone, TextureHelper.samplers[0])
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