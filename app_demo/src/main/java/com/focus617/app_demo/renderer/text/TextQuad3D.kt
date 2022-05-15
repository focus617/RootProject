package com.focus617.app_demo.renderer.text

import com.focus617.core.engine.objects.DrawableObject
import com.focus617.core.engine.renderer.RenderCommand
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.renderer.vertex.BufferElement
import com.focus617.core.engine.renderer.vertex.BufferLayout
import com.focus617.core.engine.renderer.vertex.ShaderDataType
import com.focus617.core.engine.resource.baseDataType.Color


class TextQuad3D : DrawableObject() {
    init {
        shaderName = ShaderFilePath
    }

    override fun submit(shader: Shader) {
        if (textureIndex == -1) return

        shader.setMat4(U_MODEL_MATRIX, modelMatrix)
        shader.setInt(U_TEXTURE, textureIndex)
        shader.setFloat4(U_COLOR, Color.BLUE)

        vertexArray.bind()
        RenderCommand.drawIndexed(vertexArray)

        // 下面这两行可以省略，以节约GPU的运行资源；
        // 在下个submit，会bind其它handle，自然会实现unbind
        vertexArray.unbind()
        shader.unbind()
    }

    override fun getVertices(): FloatArray = floatArrayOf(
        // Vertex attributes for a quad that fills the entire screen in Normalized Device Coordinates.
        // 每个顶点有2个顶点属性一位置、纹理
        // 在OpenGLES3.0采用的坐标系统中，纹理坐标的原点是纹理图的左上角
        // x,   y,  TextureX, TextureY
        -0.5f, 0.5f, 0.0f, 0.0f,  //0 左上
        -0.5f, -0.5f, 0.0f, 1.0f,  //1 左下
        0.5f, -0.5f, 1.0f, 1.0f,   //2 右下
        0.5f, 0.5f, 1.0f, 0.0f    //3 右上
    )

    override fun getLayout(): BufferLayout = BufferLayout(
        listOf(
            BufferElement("a_Position", ShaderDataType.Float2, true),
            BufferElement("a_TexCoords", ShaderDataType.Float2, true)
        )
    )

    override fun getIndices(): ShortArray = shortArrayOf(
        0, 1, 2, 0, 2, 3
    )

    override fun beforeBuild() {
    }

    override fun afterBuild() {
    }

    companion object {
        const val SHADER_PATH = "Text"
        const val SHADER_FILE = "shader.glsl"
        const val ShaderFilePath: String = "$SHADER_PATH/$SHADER_FILE"
        const val U_TEXTURE = "u_Texture"
        const val U_COLOR = "u_Color"

        lateinit var shader: Shader

        var textureIndex: Int = -1      // 在TextureSlot中的Index

    }
}