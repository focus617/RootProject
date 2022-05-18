package com.focus617.app_demo.renderer.framebuffer

import com.focus617.app_demo.engine.XGLDrawableObject
import com.focus617.app_demo.renderer.vertex.XGLVertexArray
import com.focus617.core.engine.objects.DrawableObject
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.renderer.vertex.BufferElement
import com.focus617.core.engine.renderer.vertex.BufferLayout
import com.focus617.core.engine.renderer.vertex.ShaderDataType
import com.focus617.core.engine.scene_graph.renderer.Mesh


open class FrameBufferQuad : DrawableObject(), XGLDrawableObject {


    override fun initOpenGlResource() {
        mesh = Mesh(XGLVertexArray.buildVertexArray(this))
    }

    override fun submit(shader: Shader) {
    }

    // 用于本对象作为FrameBuffer绘制时
    fun draw(screenTextureIndex: Int) {
        shader.bind()
        shader.setInt(U_TEXTURE, screenTextureIndex)

//        vertexArray.bind()
//        RenderCommand.drawIndexed(vertexArray)
//
//        // 下面这两行可以省略，以节约GPU的运行资源；
//        // 在下个submit，会bind其它handle，自然会实现unbind
//        vertexArray.unbind()
        mesh.draw()

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
        const val SHADER_PATH = "framebuffer"
        const val SHADER_FILE = "shader.glsl"
        const val SHADER_OUTLINING_FILE = "shaderSingleColor.glsl"

        const val ShaderFilePath: String = "$SHADER_PATH/$SHADER_FILE"
        const val ShaderOutliningFilePath: String = "$SHADER_PATH/$SHADER_OUTLINING_FILE"

        lateinit var shader: Shader
        lateinit var shaderOutlining: Shader

        const val U_TEXTURE = "u_screenTexture"
    }
}