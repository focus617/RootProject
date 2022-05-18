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
import com.focus617.core.engine.scene_graph.DrawableEntity
import com.focus617.core.engine.scene_graph.components.MeshRenderer
import com.focus617.core.engine.scene_graph.renderer.Material
import com.focus617.core.engine.scene_graph.renderer.Mesh
import com.focus617.core.engine.scene_graph.scene.PointLight

class Box : DrawableEntity(), XGLDrawableObject{
    init {
        shaderName = ShaderFilePath
    }

    override fun initOpenGlResource() {
        val mesh = Mesh(XGLVertexArray.buildVertexArray(Cube()))
        val meshRenderer = MeshRenderer(mesh, Material())
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


//    override fun getVertices(): FloatArray = floatArrayOf(
//        // 立方体的顶点有4个属性:位置、法线和纹理坐标
//        // 背面
//        // positions         normals           texture Coords
//        -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
//        0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
//        0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 0.0f,
//        -0.5f, -0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 0.0f,
//        -0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 0.0f, 1.0f,
//        0.5f, 0.5f, -0.5f, 0.0f, 0.0f, -1.0f, 1.0f, 1.0f,
//        // 正面
//        -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
//        0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 0.0f,
//        0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
//        0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 1.0f, 1.0f,
//        -0.5f, 0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 1.0f,
//        -0.5f, -0.5f, 0.5f, 0.0f, 0.0f, 1.0f, 0.0f, 0.0f,
//        // 左侧
//        -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
//        -0.5f, 0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
//        -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
//        -0.5f, -0.5f, -0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
//        -0.5f, -0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
//        -0.5f, 0.5f, 0.5f, -1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
//        // 右侧
//        0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
//        0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
//        0.5f, 0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 1.0f,
//        0.5f, -0.5f, -0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 1.0f,
//        0.5f, 0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 1.0f, 0.0f,
//        0.5f, -0.5f, 0.5f, 1.0f, 0.0f, 0.0f, 0.0f, 0.0f,
//        // 底面
//        -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f,
//        0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 1.0f,
//        0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f,
//        0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 1.0f, 0.0f,
//        -0.5f, -0.5f, 0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 0.0f,
//        -0.5f, -0.5f, -0.5f, 0.0f, -1.0f, 0.0f, 0.0f, 1.0f,
//        // 顶面
//        -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
//        0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
//        0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 1.0f,
//        0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 1.0f, 0.0f,
//        -0.5f, 0.5f, -0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 1.0f,
//        -0.5f, 0.5f, 0.5f, 0.0f, 1.0f, 0.0f, 0.0f, 0.0f
//    )
//
//    override fun getLayout(): BufferLayout = BufferLayout(
//        listOf(
//            BufferElement("a_Position", ShaderDataType.Float3, true),
//            BufferElement("a_Normal", ShaderDataType.Float3, true),
//            BufferElement("a_TexCoords", ShaderDataType.Float2, true)
//        )
//    )
//
//    override fun getIndices(): ShortArray = shortArrayOf(
//        // 顶点索引: 6 indices per cube side
//        // Back
//        0, 1, 2,
//        3, 4, 5,
//
//        // Front
//        6, 7, 8,
//        9, 10, 11,
//
//        // Left
//        12, 13, 14,
//        15, 16, 17,
//
//        // Right
//        18, 19, 20,
//        21, 22, 23,
//
//        // Top
//        24, 25, 26,
//        27, 28, 29,
//
//        // Bottom
//        30, 31, 32,
//        33, 34, 35
//    )
//
//    override fun beforeBuild() {
//    }
//
//    override fun afterBuild() {
//    }

    companion object {
        private const val SHADER_PATH = "Cube"
        private const val SHADER_FILE = "CubeWithTextureAndLight.glsl"
        private const val TEXTURE_FILE = "box.png"

        const val ShaderFilePath: String = "$SHADER_PATH/$SHADER_FILE"
        const val TextureFilePath: String = "$SHADER_PATH/$TEXTURE_FILE"

        var textureIndex: Int = -1
        fun initTexture(context: Context) {
            val textureBox = XGLTextureBuilder.createTexture(context, TextureFilePath)
            textureBox?.apply {
                textureIndex = XGLTextureSlots.getId(textureBox)
            }
        }

        lateinit var viewPoint: Point3D

        const val U_POINT_VIEW_POSITION = "u_ViewPos"
        const val U_POINT_LIGHT_POSITION = "light.position"
        const val U_POINT_LIGHT_AMBIENT = "light.ambient"
        const val U_POINT_LIGHT_DIFFUSE = "light.diffuse"
        const val U_POINT_LIGHT_SPECULAR = "light.specular"
        const val U_POINT_LIGHT_CONSTANT = "light.constant"
        const val U_POINT_LIGHT_LINEAR = "light.linear"
        const val U_POINT_LIGHT_QUADRATIC = "light.quadratic"

        const val U_MATERIAL_TEXTURE_DIFFUSE = "material.diffuse"
        const val U_MATERIAL_SPECULAR = "material.specular"
        const val U_MATERIAL_SHININESS = "material.shininess"

        // 镜面强度(Specular Intensity)
        var specular: Vector3 = Vector3(0.5f, 0.5f, 0.5f)

        // 高光的反光度
        var shininess: Float = 100.0f
    }

}