package com.focus617.app_demo.engine.d3

import android.content.Context
import com.focus617.app_demo.renderer.framebuffer.FrameBufferEntity
import com.focus617.app_demo.renderer.shader.XGLShader
import com.focus617.app_demo.renderer.shader.XGLShaderBuilder
import com.focus617.app_demo.renderer.texture.XGLTextureSlots
import com.focus617.app_demo.scene_graph.Model
import com.focus617.app_demo.terrain.Heightmap
import com.focus617.app_demo.terrain.SkyBox
import com.focus617.app_demo.text.TextEntity2D
import com.focus617.app_demo.text.TextEntity3D
import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.scene_graph.components.camera.PerspectiveCamera
import com.focus617.core.engine.scene_graph.components.camera.PerspectiveCameraController
import com.focus617.core.engine.scene_graph.core.NodeEntity
import com.focus617.core.engine.scene_graph.core.Transform
import com.focus617.core.engine.scene_graph.scene.Scene

/**
 * Scene is root entity for all Game Entities.
 */
class XGLScene3D(val context: Context, val engine: Sandbox3D) : Scene() {

    private val model = Model(context, "sampledata/Andy/andy.obj")

    init {
        mCamera = PerspectiveCamera()
        addComponent(mCamera)
        mCameraController = PerspectiveCameraController(mCamera as PerspectiveCamera)
        addComponent(mCameraController)
    }

    fun initOpenGlResource() {
        // 初始化顺序不能错
        XGLTextureSlots.initUnderOpenGl()
        initShader()
        initTexture()
        initGameObjects()

        model.initUnderOpenGl()
    }

    private fun initShader() {
        FrameBufferEntity.shader = XGLShaderBuilder.createShader(
            context,
            FrameBufferEntity.ShaderFilePath
        ) as XGLShader

        FrameBufferEntity.shaderOutlining = XGLShaderBuilder.createShader(
            context,
            FrameBufferEntity.ShaderOutliningFilePath
        ) as XGLShader

        TextEntity2D.shaderWithColor = XGLShaderBuilder.createShader(
            context,
            TextEntity2D.ShaderWithColorFilePath
        ) as XGLShader

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
            Box.ShaderFilePath
        ) as XGLShader
        mShaderLibrary.add(shader)

        TextEntity3D.shader = XGLShaderBuilder.createShader(
            context,
            TextEntity3D.ShaderFilePath
        ) as XGLShader
        mShaderLibrary.add(TextEntity3D.shader)
    }

    private fun initTexture() {
        SkyBox.initMaterial(context)
        Heightmap.initMaterial(context)
        Box.initMaterial(context)
    }

    private fun initGameObjects() {
        var layerStack = engine.getLayerStack()
        for (layer in layerStack.mLayers) {
            layer.initOpenGlResource()
        }

        layerStack = engine.getOverLayerStack()
        for (layer in layerStack.mLayers) {
            layer.initOpenGlResource()
        }
    }

    // Used for updating the global resource, such as objects in scene
    override fun onUpdate(timeStep: TimeStep) {
        // Update Camera
        mCameraController.onUpdate(timeStep, Transform(NodeEntity()))

    }

}