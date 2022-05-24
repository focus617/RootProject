package com.focus617.app_demo.engine.d3

import android.content.Context
import com.focus617.core.engine.renderer.shader.ShaderLibrary
import com.focus617.core.platform.base.BaseEntity
import com.focus617.opengles.renderer.framebuffer.FrameBufferEntity
import com.focus617.opengles.renderer.shader.XGLShader
import com.focus617.opengles.renderer.shader.XGLShaderBuilder
import com.focus617.opengles.renderer.texture.XGLTextureSlots
import com.focus617.opengles.terrain.Heightmap
import com.focus617.opengles.terrain.SkyBox
import com.focus617.opengles.text.TextEntity2D
import com.focus617.opengles.text.TextEntity3D
import java.io.Closeable

/**
 * Scene is root entity for all Game Entities.
 */
class XGL3DResourceManager(val context: Context, val engine: Sandbox3D) : BaseEntity(), Closeable {
    val mShaderLibrary = ShaderLibrary()

    override fun close() {
        mShaderLibrary.close()
    }

    fun initOpenGlResource() {
        // 初始化顺序不能错
        XGLTextureSlots.initUnderOpenGl()
        initShader()
        initTexture()
        initModel()
        initGameObjects()
    }

    private fun initShader() {
        FrameBufferEntity.shader = XGLShaderBuilder.createShader(
            context,
            FrameBufferEntity.ShaderFilePath
        ) as XGLShader

//        FrameBufferEntity.shaderOutlining = XGLShaderBuilder.createShader(
//            context,
//            FrameBufferEntity.ShaderOutliningFilePath
//        ) as XGLShader

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

        shader = XGLShaderBuilder.createShader(
            context,
            CommonShader
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

    private fun initModel() {
        ModelTest.initModel(context)
        ModelCoord.initModel(context)
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


    companion object {
        private const val SHADER_PATH = "common"
        private const val SHADER_FILE = "ShaderWithTextureAndLight.glsl"
        const val CommonShader = "$SHADER_PATH/$SHADER_FILE"
    }

}