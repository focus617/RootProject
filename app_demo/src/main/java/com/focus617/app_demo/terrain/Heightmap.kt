package com.focus617.app_demo.terrain

import android.opengl.GLES31
import com.focus617.app_demo.engine.XGLDrawableObject
import com.focus617.app_demo.renderer.vertex.XGLVertexArray
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.scene.Light
import com.focus617.core.engine.scene_graph.DrawableEntity
import com.focus617.core.engine.scene_graph.components.MeshRenderer
import com.focus617.core.engine.scene_graph.renderer.Material
import com.focus617.core.engine.scene_graph.renderer.Mesh
import kotlin.properties.Delegates

class Heightmap(val mesh: HeightmapMesh) : DrawableEntity(), XGLDrawableObject {

    init {
        shaderName = HeightMapShaderFilePath
    }

    override fun initOpenGlResource() {
        val mesh = Mesh(XGLVertexArray.buildVertexArray(mesh))
        val meshRenderer = MeshRenderer(mesh, Material())
        addComponent(meshRenderer)
    }

    override fun onRender(shader: Shader) {
        shader.setInt(U_TEXTURE_UNIT_1, textureIndexGrass)
        shader.setInt(U_TEXTURE_UNIT_2, textureIndexStone)

        shader.setFloat3(Light.U_VECTOR_TO_LIGHT, Light.vectorToLight)

        //Enable Cull Back Face
        GLES31.glEnable(GLES31.GL_CULL_FACE)

        super.onRender(shader)

        GLES31.glDisable(GLES31.GL_CULL_FACE)
    }

    companion object {
        private val HEIGHTMAP_SHADER_PATH = "HeightMap"
        private val HEIGHTMAP_SHADER_FILE = "heightmap.glsl"
        private val HEIGHTMAP_BITMAP_FILE = "heightmap.png"
        private val HEIGHTMAP_GRASS_TEXTURE_FILE = "noisy_grass_public_domain.png"
        private val HEIGHTMAP_STONE_TEXTURE_FILE = "stone_public_domain.png"

        val HeightMapShaderFilePath: String = "$HEIGHTMAP_SHADER_PATH/$HEIGHTMAP_SHADER_FILE"
        val HeightMapBitmapFilePath: String = "$HEIGHTMAP_SHADER_PATH/$HEIGHTMAP_BITMAP_FILE"
        val HeightMapGrassFilePath: String = "$HEIGHTMAP_SHADER_PATH/$HEIGHTMAP_GRASS_TEXTURE_FILE"
        val HeightMapStoneFilePath: String = "$HEIGHTMAP_SHADER_PATH/$HEIGHTMAP_STONE_TEXTURE_FILE"

        const val U_TEXTURE_UNIT_1 = "u_TextureUnit1"
        const val U_TEXTURE_UNIT_2 = "u_TextureUnit2"

        var textureIndexGrass by Delegates.notNull<Int>()
        var textureIndexStone by Delegates.notNull<Int>()
    }
}
