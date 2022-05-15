package com.focus617.app_demo.engine.d3

import android.content.Context
import com.focus617.app_demo.renderer.texture.XGLTextureBuilder
import com.focus617.app_demo.renderer.texture.XGLTextureSlots
import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.math.Ray
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.Vector4
import com.focus617.core.engine.objects.d3.Cube
import com.focus617.core.engine.renderer.RenderCommand
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.scene.PointLight
import kotlin.properties.Delegates

class Box : Cube() {
    init {
        shaderName = ShaderFilePath
    }

    override fun submit(shader: Shader) {

        shader.setMat4(U_MODEL_MATRIX, modelMatrix)

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

        vertexArray.bind()
        RenderCommand.drawIndexed(vertexArray)

        // 下面这两行可以省略，以节约GPU的运行资源；
        // 在下个submit，会bind其它handle，自然会实现unbind
        vertexArray.unbind()
        shader.unbind()
    }

    fun updateCameraPosition(point: Point3D) {
        viewPoint = point
    }

    override fun intersects(ray: Ray) {
        // test if this ray intersects with the game object
        isSelected = Vector3.intersects(boundingSphere, ray)

    }


    companion object {
        val SHADER_PATH = "Cube"
        val SHADER_FILE = "CubeWithTextureAndLight.glsl"
        val TEXTURE_FILE = "box.png"

        val ShaderFilePath: String = "$SHADER_PATH/$SHADER_FILE"
        val TextureFilePath: String = "$SHADER_PATH/$TEXTURE_FILE"

        var textureIndex by Delegates.notNull<Int>()
        fun initTexture(context: Context){
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

        val WHITE = Vector4(1.0f, 1.0f, 1.0f, 1.0f)

        // 镜面强度(Specular Intensity)
        var specular: Vector3 = Vector3(0.5f, 0.5f, 0.5f)

        // 高光的反光度
        var shininess: Float = 100.0f
    }

}