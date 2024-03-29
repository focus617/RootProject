package com.focus617.opengles.terrain

import android.content.Context
import android.opengl.GLES31
import com.focus617.core.engine.mesh.Mesh
import com.focus617.core.engine.mesh.d3.Cube
import com.focus617.core.engine.renderer.Material
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.scene_graph.GeometryEntity
import com.focus617.core.engine.scene_graph.components.MeshRenderer
import com.focus617.opengles.renderer.texture.XGLTextureCubeMap
import com.focus617.opengles.renderer.texture.XGLTextureSlots
import com.focus617.opengles.renderer.vertex.XGLVertexArray
import com.focus617.opengles.scene_graph.XGLDrawableObject

class SkyBox : GeometryEntity(), XGLDrawableObject {
    init {
        shaderName = SkyBoxShaderFilePath
    }

    override fun initOpenGlResource() {
        val mesh = Mesh(XGLVertexArray.buildVertexArray(Cube()))
        val meshRenderer = MeshRenderer(mesh, material)
        addComponent(meshRenderer)
    }

    override fun onRender(shader: Shader) {
        // This avoids problems with the skybox itself getting clipped.
        GLES31.glDepthFunc(GLES31.GL_LEQUAL)

        super.onRender(shader)

        GLES31.glDepthFunc(GLES31.GL_LESS)
    }

    companion object {
        val SKYBOX_SHADER_PATH = "SkyBox"
        private val SKYBOX_SHADER_FILE = "SkyBox.glsl"
        val SkyBoxShaderFilePath: String = "$SKYBOX_SHADER_PATH/$SKYBOX_SHADER_FILE"

        const val U_TEXTURE_UNIT = "u_TextureUnit"
        const val textureIndexCubeMap = 0 // skybox always use slot 0

        val material = Material()
        fun initMaterial(context: Context) {
            val texture = XGLTextureCubeMap(
                context,
                SKYBOX_SHADER_PATH,
                arrayOf("left.png", "right.png", "bottom.png", "top.png", "front.png", "back.png")
            )
            XGLTextureSlots.TextureSlots[textureIndexCubeMap] = texture
            material.add(U_TEXTURE_UNIT, textureIndexCubeMap)
        }
    }
}