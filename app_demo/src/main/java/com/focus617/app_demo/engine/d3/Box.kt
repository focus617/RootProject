package com.focus617.app_demo.engine.d3

import com.focus617.core.engine.math.Vector4
import com.focus617.core.engine.objects.d3.Cube
import com.focus617.core.engine.renderer.RenderCommand
import com.focus617.core.engine.renderer.Shader
import kotlin.properties.Delegates

class Box : Cube() {
    init {
        shaderName = ShaderFilePath
    }

    companion object{
        val SHADER_PATH = "Cube"
        val SHADER_FILE = "Texture.glsl"
        val TEXTURE_FILE = "box.png"

        val ShaderFilePath: String = "$SHADER_PATH/$SHADER_FILE"
        val TextureFilePath: String = "$SHADER_PATH/$TEXTURE_FILE"

        var textureIndex by Delegates.notNull<Int>()

        const val U_TEXTURE_UNIT = "u_Texture"
        const val U_COLOR = "u_Color"

        val WHITE = Vector4(1.0f, 1.0f, 1.0f, 1.0f)
    }

    override fun submit(shader: Shader) {
        shader.setInt(U_TEXTURE_UNIT, textureIndex)
        shader.setMat4(U_MODEL_MATRIX, modelMatrix)
        shader.setFloat4(U_COLOR, WHITE)

        vertexArray.bind()
        RenderCommand.drawIndexed(vertexArray)

        // 下面这两行可以省略，以节约GPU的运行资源；
        // 在下个submit，会bind其它handle，自然会实现unbind
        vertexArray.unbind()
        shader.unbind()
    }
}