package com.focus617.app_demo.engine.d3

import android.content.Context
import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.mesh.Mesh
import com.focus617.core.engine.mesh.d3.Ball
import com.focus617.core.engine.renderer.Material
import com.focus617.core.engine.renderer.api.ShaderUniformConstants.U_POINT_VIEW_POSITION
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.scene_graph.GeometryEntity
import com.focus617.core.engine.scene_graph.components.MeshRenderer
import com.focus617.opengles.renderer.texture.XGLTextureBuilder
import com.focus617.opengles.renderer.texture.XGLTextureSlots
import com.focus617.opengles.renderer.vertex.XGLVertexArray
import com.focus617.opengles.scene_graph.XGLDrawableObject

class Earth : GeometryEntity(), XGLDrawableObject {
    lateinit var viewPoint: Point3D

    init {
        shaderName = ShaderFilePath
    }

    override fun initOpenGlResource() {
        val mesh = Mesh(XGLVertexArray.buildVertexArray(Ball(1.0f)))
        val meshRenderer = MeshRenderer(mesh, material)
        addComponent(meshRenderer)
    }

    override fun onRender(shader: Shader) {
        shader.setInt(U_TEXTURE_UNIT_1, textureIndexDay)
        shader.setInt(U_TEXTURE_UNIT_2, textureIndexNight)

        shader.setFloat3(U_POINT_VIEW_POSITION, viewPoint)
        super.onRender(shader)
    }

    fun updateCameraPosition(point: Point3D) {
        viewPoint = point
    }

    companion object {
        private val SHADER_PATH = "Earth"
        private val SHADER_FILE = "earth.glsl"
        private val DAY_TEXTURE_FILE = "earth_day.png"
        private val NIGHT_TEXTURE_FILE = "earth_night.png"

        val ShaderFilePath: String = "$SHADER_PATH/$SHADER_FILE"
        val DayTextureFilePath: String = "$SHADER_PATH/$DAY_TEXTURE_FILE"
        val NightTextureFilePath: String = "$SHADER_PATH/$NIGHT_TEXTURE_FILE"

        var textureIndexDay: Int = -1
        var textureIndexNight: Int = -1
        var material = Material()

        fun initMaterial(context: Context) {
            val textureDay = XGLTextureBuilder.createTexture(context, DayTextureFilePath)
            textureDay?.apply {
                textureIndexDay = XGLTextureSlots.requestIndex(textureDay)
                material.add("day", textureDay)
            }

            val textureNight = XGLTextureBuilder.createTexture(context, NightTextureFilePath)
            textureNight?.apply {
                textureIndexDay = XGLTextureSlots.requestIndex(textureNight)
                material.add("day", textureNight)
            }
        }

        const val U_TEXTURE_UNIT_1 = "u_TextureUnit1"
        const val U_TEXTURE_UNIT_2 = "u_TextureUnit2"

    }
}