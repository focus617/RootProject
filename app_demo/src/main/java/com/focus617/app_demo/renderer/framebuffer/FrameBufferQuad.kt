package com.focus617.app_demo.renderer.framebuffer

import com.focus617.app_demo.renderer.vertex.XGLVertexArray
import com.focus617.core.engine.objects.DrawableObject
import com.focus617.core.engine.renderer.*


class FrameBufferQuad : DrawableObject() {
    init {
        vertexArray = XGLVertexArray.buildVertexArray(this)
    }

    override fun submit(shader: Shader) {}

    fun draw() {
        shader.bind()
        shader.setInt(U_TEXTURE, screenTextureIndex)

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
        // x,   y,  TextureX, TextureY
        -1.0f, 1.0f, 0.0f, 1.0f,  //0 左上
        -1.0f, -1.0f, 0.0f, 0.0f,  //1 左下
        1.0f, -1.0f, 1.0f, 0.0f,   //2 右下
        1.0f, 1.0f, 1.0f, 1.0f    //3 右上
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
        val SHADER_PATH = "framebuffer"
        val SHADER_FILE = "shader.glsl"
        val SHADER_OUTLINING_FILE = "shaderSingleColor.glsl"

        val ShaderFilePath: String = "$SHADER_PATH/$SHADER_FILE"
        val ShaderOutliningFilePath: String = "$SHADER_PATH/$SHADER_OUTLINING_FILE"

        lateinit var shader: Shader
        lateinit var shaderOutlining: Shader
        var screenTextureIndex: Int = -1    // 在TextureSlots内的Index

        const val U_TEXTURE = "u_screenTexture"
    }
}