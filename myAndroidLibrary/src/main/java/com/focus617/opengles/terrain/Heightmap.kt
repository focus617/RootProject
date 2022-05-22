package com.focus617.opengles.terrain

import android.content.Context
import android.opengl.GLES31
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.scene_graph.GeometryEntity
import com.focus617.core.engine.scene_graph.components.Light
import com.focus617.core.engine.scene_graph.components.MeshRenderer
import com.focus617.core.engine.scene_graph.renderer.Material
import com.focus617.core.engine.scene_graph.renderer.Mesh
import com.focus617.core.engine.scene_graph.renderer.ShaderUniformConstants.U_VECTOR_TO_LIGHT
import com.focus617.opengles.renderer.texture.XGLTextureBuilder
import com.focus617.opengles.renderer.texture.XGLTextureSlots
import com.focus617.opengles.renderer.vertex.XGLVertexArray
import com.focus617.opengles.scene_graph.XGLDrawableObject

class Heightmap(val mesh: HeightmapMesh) : GeometryEntity(), XGLDrawableObject {


    init {
        shaderName = HeightMapShaderFilePath
    }

    override fun initOpenGlResource() {
        val mesh = Mesh(XGLVertexArray.buildVertexArray(mesh))
        val meshRenderer = MeshRenderer(mesh, material)
        addComponent(meshRenderer)
    }

    override fun onRender(shader: Shader) {
        shader.setInt(U_TEXTURE_UNIT_1, textureIndexGrass)
        shader.setInt(U_TEXTURE_UNIT_2, textureIndexStone)

        shader.setFloat3(U_VECTOR_TO_LIGHT, Light.vectorToLight)

        //Enable Cull Back Face
        GLES31.glEnable(GLES31.GL_CULL_FACE)

        super.onRender(shader)

        GLES31.glDisable(GLES31.GL_CULL_FACE)
    }

    companion object {
        private const val HEIGHTMAP_SHADER_PATH = "HeightMap"
        private const val HEIGHTMAP_SHADER_FILE = "heightmap.glsl"
        private const val HEIGHTMAP_BITMAP_FILE = "heightmap.png"
        private const val HEIGHTMAP_GRASS_TEXTURE_FILE = "noisy_grass_public_domain.png"
        private const val HEIGHTMAP_STONE_TEXTURE_FILE = "stone_public_domain.png"

        const val HeightMapShaderFilePath: String = "$HEIGHTMAP_SHADER_PATH/$HEIGHTMAP_SHADER_FILE"
        const val HeightMapBitmapFilePath: String = "$HEIGHTMAP_SHADER_PATH/$HEIGHTMAP_BITMAP_FILE"
        private const val HeightMapGrassFilePath: String =
            "$HEIGHTMAP_SHADER_PATH/$HEIGHTMAP_GRASS_TEXTURE_FILE"
        private const val HeightMapStoneFilePath: String =
            "$HEIGHTMAP_SHADER_PATH/$HEIGHTMAP_STONE_TEXTURE_FILE"

        const val U_TEXTURE_UNIT_1 = "u_TextureUnit1"
        const val U_TEXTURE_UNIT_2 = "u_TextureUnit2"
        const val TEXTURE_GRASS = "grass"
        const val TEXTURE_STONE = "stone"

        val material = Material()
        var textureIndexGrass = -1
        var textureIndexStone = -1

        fun initMaterial(context: Context) {
            val textureGrass =
                XGLTextureBuilder.createTexture(context, HeightMapGrassFilePath)
            textureGrass?.apply {
                textureIndexGrass = XGLTextureSlots.requestIndex(textureGrass)
                material.add(TEXTURE_GRASS, textureGrass)
            }

            val textureStone =
                XGLTextureBuilder.createTexture(context, HeightMapStoneFilePath)
            textureStone?.apply {
                textureIndexStone = XGLTextureSlots.requestIndex(textureStone)
                material.add(TEXTURE_STONE, textureStone)
            }
        }
    }
}
