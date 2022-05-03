package com.focus617.app_demo.engine

import android.content.Context
import com.focus617.app_demo.renderer.XGLShader
import com.focus617.app_demo.renderer.XGLShaderBuilder
import com.focus617.app_demo.renderer.XGLTextureCubeMap
import com.focus617.app_demo.renderer.XGLVertexArray
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

        mCameraController.setPosition(0f, -1.5f, 0f)
    }

    fun initOpenGlResource() {
        initShader()
        initTexture()
        initGameObjects()
    }

    private fun initShader() {
        val shader = XGLShaderBuilder.createShader(
            context,
            "${SHADER_PATH}/${SHADER_FILE}"
        ) as XGLShader
        shader.bind()
        shader.setInt("u_TextureUnit", 0)
        mShaderLibrary.add(shader)

        shaderName = shader.getName()
    }

    private fun initTexture() {
        val texture = XGLTextureCubeMap(
            context,
            SHADER_PATH,
            arrayOf("left.png", "right.png", "bottom.png", "top.png", "front.png", "back.png")
        )
        mTextureLibrary.add(texture)

        textureName = texture.filePath
    }

    private fun initGameObjects() {
        val layerStack = engine.getLayerStack()
        for (layer in layerStack.mLayers)
            for (gameObject in layer.gameObjectList) {
                gameObject.vertexArray = XGLVertexArray.buildVertexArray(gameObject)
                gameObject.shaderName = shaderName
                gameObject.textureName = textureName
            }

    }

    // Used for updating the global resource, such as objects in scene
    override fun onUpdate(timeStep: TimeStep) {
        // Update Camera
        mCameraController.onUpdate(timeStep)

    }

    companion object {
        private val SHADER_PATH = "Cube"
        private val SHADER_FILE = "SkyBox.glsl"

        private var shaderName: String = "null"
        private var textureName: String = "null"

    }
}