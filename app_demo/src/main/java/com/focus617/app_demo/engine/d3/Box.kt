package com.focus617.app_demo.engine.d3

import android.content.Context
import com.focus617.app_demo.engine.XGLDrawableObject
import com.focus617.app_demo.renderer.texture.XGLTextureBuilder
import com.focus617.app_demo.renderer.texture.XGLTextureSlots
import com.focus617.app_demo.renderer.vertex.XGLVertexArray
import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.math.Ray
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.mesh.d3.Cube
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.scene_graph.GeometryEntity
import com.focus617.core.engine.scene_graph.components.MeshRenderer
import com.focus617.core.engine.scene_graph.renderer.Material
import com.focus617.core.engine.scene_graph.renderer.Mesh
import com.focus617.core.engine.scene_graph.renderer.ShaderUniformConstants.U_MATERIAL_SHININESS
import com.focus617.core.engine.scene_graph.renderer.ShaderUniformConstants.U_MATERIAL_SPECULAR
import com.focus617.core.engine.scene_graph.renderer.ShaderUniformConstants.U_MATERIAL_TEXTURE_DIFFUSE
import com.focus617.core.engine.scene_graph.renderer.ShaderUniformConstants.U_POINT_LIGHT_AMBIENT
import com.focus617.core.engine.scene_graph.renderer.ShaderUniformConstants.U_POINT_LIGHT_CONSTANT
import com.focus617.core.engine.scene_graph.renderer.ShaderUniformConstants.U_POINT_LIGHT_DIFFUSE
import com.focus617.core.engine.scene_graph.renderer.ShaderUniformConstants.U_POINT_LIGHT_LINEAR
import com.focus617.core.engine.scene_graph.renderer.ShaderUniformConstants.U_POINT_LIGHT_POSITION
import com.focus617.core.engine.scene_graph.renderer.ShaderUniformConstants.U_POINT_LIGHT_QUADRATIC
import com.focus617.core.engine.scene_graph.renderer.ShaderUniformConstants.U_POINT_LIGHT_SPECULAR
import com.focus617.core.engine.scene_graph.renderer.ShaderUniformConstants.U_POINT_VIEW_POSITION
import com.focus617.core.engine.scene_graph.scene.PointLight

class Box : GeometryEntity(), XGLDrawableObject{
    init {
        shaderName = ShaderFilePath
    }

    override fun initOpenGlResource() {
        val mesh = Mesh(XGLVertexArray.buildVertexArray(Cube()))
        val meshRenderer = MeshRenderer(mesh, material)
        addComponent(meshRenderer)
    }

    override fun onRender(shader: Shader) {
        shader.setFloat3(U_POINT_VIEW_POSITION, viewPoint)

        shader.setFloat3(U_POINT_LIGHT_POSITION, PointLight.position)
        shader.setFloat3(U_POINT_LIGHT_AMBIENT, PointLight.ambient)
        shader.setFloat3(U_POINT_LIGHT_DIFFUSE, PointLight.diffuse)
        shader.setFloat3(U_POINT_LIGHT_SPECULAR, PointLight.specular)
        shader.setFloat(U_POINT_LIGHT_CONSTANT, PointLight.Constant)
        shader.setFloat(U_POINT_LIGHT_LINEAR, PointLight.Linear)
        shader.setFloat(U_POINT_LIGHT_QUADRATIC, PointLight.Quadratic)

        shader.setInt(U_MATERIAL_TEXTURE_DIFFUSE, textureIndex)
        shader.setFloat3(U_MATERIAL_SPECULAR, specular)
        shader.setFloat(U_MATERIAL_SHININESS, shininess)

        super.onRender(shader)
    }

    fun updateCameraPosition(point: Point3D) {
        viewPoint = point
    }

    override fun intersects(ray: Ray) {
        // test if this ray intersects with the game object
        isSelected = Vector3.intersects(boundingSphere, ray)

    }

    companion object {
        private const val SHADER_PATH = "Cube"
        private const val SHADER_FILE = "CubeWithTextureAndLight.glsl"
        private const val TEXTURE_FILE = "box.png"

        const val ShaderFilePath: String = "$SHADER_PATH/$SHADER_FILE"
        const val TextureFilePath: String = "$SHADER_PATH/$TEXTURE_FILE"

        const val TEXTURE_BOX = "BoxTexture"

        var textureIndex: Int = -1
        val material = Material()

        fun initMaterial(context: Context) {
            val textureBox = XGLTextureBuilder.createTexture(context, TextureFilePath)
            textureBox?.apply {
                textureIndex = XGLTextureSlots.requestIndex(textureBox)
                material.add(TEXTURE_BOX, textureBox)
            }
        }

        lateinit var viewPoint: Point3D

        // 镜面强度(Specular Intensity)
        var specular: Vector3 = Vector3(0.5f, 0.5f, 0.5f)

        // 高光的反光度
        var shininess: Float = 100.0f
    }

}