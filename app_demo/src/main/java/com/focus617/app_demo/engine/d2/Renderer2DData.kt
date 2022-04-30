package com.focus617.app_demo.engine.d2

import android.content.Context
import com.focus617.app_demo.renderer.*
import com.focus617.core.engine.objects.IfDrawable
import com.focus617.core.engine.objects.d2.Quad
import com.focus617.core.engine.renderer.BufferElement
import com.focus617.core.engine.renderer.BufferLayout
import com.focus617.core.engine.renderer.ShaderDataType
import com.focus617.core.engine.renderer.Texture2D
import java.io.Closeable
import java.nio.ByteBuffer
import java.nio.LongBuffer

object Renderer2DData: Closeable {
    val MaxQuads: Int = 10000
    val MaxVertices: Int = MaxQuads * 4
    val MaxIndices: Int = MaxQuads * 6

    lateinit var QuadVertexArray: XGLVertexArray
    lateinit var QuadVertexBuffer: XGLVertexBuffer
    lateinit var TextureShader: XGLShader
    lateinit var WhiteTexture: Texture2D

    lateinit var QuadVertexBufferBase: ByteBuffer
    var QuadVertexBufferPtr: Int = 0
    var QuadIndexCount: Int = 0

    override fun close() {
        QuadVertexBuffer.close()
        QuadVertexArray.close()
        TextureShader.close()
        WhiteTexture.close()
    }

    fun initStaticData(context: Context) {
        initShader(context)
        initVertexArray(Quad())
        initWhiteTexture()
    }

    private val PATH = "SquareWithTexture"
    private val TEXTURE_SHADER_FILE = "Texture.glsl"

    private fun initShader(context: Context) {
        TextureShader = XGLShaderBuilder.createShader(
            context,
            "${PATH}/${TEXTURE_SHADER_FILE}"
        ) as XGLShader
        TextureShader.bind()
        TextureShader.setInt("u_Texture", 0)
    }

    private fun initVertexArray(drawingObject: IfDrawable) {
        QuadVertexArray = XGLBufferBuilder.createVertexArray() as XGLVertexArray

        val quadVertexBufferLayout = BufferLayout(
            listOf(
                BufferElement("a_Position", ShaderDataType.Float3, true),
                BufferElement("a_Color", ShaderDataType.Float4, false),
                BufferElement("a_TexCoord", ShaderDataType.Float2, true)
                //TODO: Texid
            )
        )
        QuadVertexBuffer = XGLBufferBuilder.createVertexBuffer(
            MaxVertices * quadVertexBufferLayout.getStride()
        ) as XGLVertexBuffer
        QuadVertexBuffer.setLayout(quadVertexBufferLayout)
        QuadVertexArray.addVertexBuffer(QuadVertexBuffer)

        val quadIndices = ShortArray(MaxIndices)
        val indexBuffer = XGLBufferBuilder.createIndexBuffer(
            quadIndices, MaxIndices
        ) as XGLIndexBuffer
        QuadVertexArray.setIndexBuffer(indexBuffer)
    }

    private fun initWhiteTexture(){
        WhiteTexture = XGLTextureBuilder.createTexture(1, 1)!!
        val whiteTextureData = longArrayOf(0xffffffff)
        WhiteTexture.setData(LongBuffer.wrap(whiteTextureData), Int.SIZE_BYTES)
    }
}

//class QuadVertex(
//    val Position: Vector3,
//    val Color: Vector4,
//    val TextCoord: Vector2
//    //TODO: Texid
//)

