package com.focus617.app_demo.engine.d3

import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.objects.d3.Ball
import com.focus617.core.engine.renderer.RenderCommand
import com.focus617.core.engine.renderer.Shader
import com.focus617.core.engine.scene.PointLight
import kotlin.properties.Delegates

class Earth : Ball(1.0f) {
    lateinit var viewPoint: Point3D

    init {
        shaderName = ShaderFilePath
    }

    companion object {
        private val SHADER_PATH = "Earth"
        private val SHADER_FILE = "earth.glsl"
        private val DAY_TEXTURE_FILE = "earth_day.png"
        private val NIGHT_TEXTURE_FILE = "earth_night.png"

        val ShaderFilePath: String = "$SHADER_PATH/$SHADER_FILE"
        val DayTextureFilePath: String = "$SHADER_PATH/$DAY_TEXTURE_FILE"
        val NightTextureFilePath: String = "$SHADER_PATH/$NIGHT_TEXTURE_FILE"

        var textureIndexDay by Delegates.notNull<Int>()
        var textureIndexNight by Delegates.notNull<Int>()

        const val U_POINT_VIEW_POSITION = "u_ViewPos"
        const val U_TEXTURE_UNIT_1 = "u_TextureUnit1"
        const val U_TEXTURE_UNIT_2 = "u_TextureUnit2"

        const val U_POINT_LIGHT_POSITION = "light.position"
        const val U_POINT_LIGHT_AMBIENT = "light.ambient"
        const val U_POINT_LIGHT_DIFFUSE = "light.diffuse"
        const val U_POINT_LIGHT_SPECULAR = "light.specular"
        const val U_POINT_LIGHT_CONSTANT = "light.constant"
        const val U_POINT_LIGHT_LINEAR = "light.linear"
        const val U_POINT_LIGHT_QUADRATIC = "light.quadratic"
    }

    override fun submit(shader: Shader) {
        shader.setInt(U_TEXTURE_UNIT_1, textureIndexDay)
        shader.setInt(U_TEXTURE_UNIT_2, textureIndexNight)
        shader.setMat4(U_MODEL_MATRIX, modelMatrix)
        shader.setFloat3(U_POINT_VIEW_POSITION, viewPoint)

        shader.setFloat3(U_POINT_LIGHT_POSITION, PointLight.position)
        shader.setFloat3(U_POINT_LIGHT_AMBIENT, PointLight.ambient)
        shader.setFloat3(U_POINT_LIGHT_DIFFUSE, PointLight.diffuse)
        shader.setFloat3(U_POINT_LIGHT_SPECULAR, PointLight.specular)
        shader.setFloat(U_POINT_LIGHT_CONSTANT, PointLight.Constant)
        shader.setFloat(U_POINT_LIGHT_LINEAR, PointLight.Linear)
        shader.setFloat(U_POINT_LIGHT_QUADRATIC, PointLight.Quadratic)

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

}