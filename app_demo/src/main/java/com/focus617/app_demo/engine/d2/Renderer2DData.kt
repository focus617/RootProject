package com.focus617.app_demo.engine.d2

import android.content.Context
import com.focus617.app_demo.renderer.*
import com.focus617.core.engine.math.Vector2
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.Vector4
import com.focus617.core.engine.renderer.BufferElement
import com.focus617.core.engine.renderer.BufferLayout
import com.focus617.core.engine.renderer.ShaderDataType
import com.focus617.core.engine.renderer.Texture2D
import java.io.Closeable
import java.nio.LongBuffer

object Renderer2DData : Closeable {
    val MaxQuads: Int = 10000
    val MaxVertices: Int = MaxQuads * 4
    val MaxIndices: Int = MaxQuads * 6

    lateinit var QuadVertexArray: XGLVertexArray
    lateinit var QuadVertexBuffer: XGLVertexBuffer
    lateinit var TextureShader: XGLShader
    lateinit var WhiteTexture: Texture2D

    lateinit var QuadVertexBufferBase: FloatArray
    var QuadVertexBufferPtr: Int = 0    // Index of FloatArray(记住计算size时要乘4)

    var QuadIndexCount: Int = 0
    var QuadVertexSize: Int = 0         // QuadVertex的字节数

    override fun close() {
        QuadVertexBuffer.close()
        QuadVertexArray.close()
        TextureShader.close()
        WhiteTexture.close()
    }

    fun initStaticData(context: Context) {
        initShader(context)
        initVertexArray()
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

    private fun initVertexArray() {
        QuadVertexArray = XGLBufferBuilder.createVertexArray() as XGLVertexArray

        val quadVertexBufferLayout = BufferLayout(
            listOf(
                BufferElement("a_Position", ShaderDataType.Float3, true),
                BufferElement("a_Color", ShaderDataType.Float4, false),
                BufferElement("a_TexCoord", ShaderDataType.Float2, true)
                //TODO: Texid
            )
        )
        QuadVertexSize = quadVertexBufferLayout.getStride()
        val quadVertexBufferSize = MaxVertices * QuadVertexSize
        QuadVertexBuffer =
            XGLBufferBuilder.createVertexBuffer(quadVertexBufferSize) as XGLVertexBuffer
        QuadVertexBuffer.setLayout(quadVertexBufferLayout)
        QuadVertexArray.addVertexBuffer(QuadVertexBuffer)

        // 按照MaxVertices，构造一个完整的Vertices空间
        QuadVertexBufferBase = FloatArray(MaxVertices * QuadVertex.sizeInFloat)

        // 构造一个完整的Indices，具体使用时有效范围由QuadIndexCount决定
        val quadIndices = ShortArray(MaxIndices)
        var offset: Int = 0
        for (i in 0 until MaxIndices step 6) {
            quadIndices[i + 0] = (offset + 0).toShort()
            quadIndices[i + 1] = (offset + 1).toShort()
            quadIndices[i + 2] = (offset + 2).toShort()

            quadIndices[i + 3] = (offset + 2).toShort()
            quadIndices[i + 4] = (offset + 3).toShort()
            quadIndices[i + 5] = (offset + 0).toShort()

            offset += 4
        }

        val indexBuffer = XGLBufferBuilder.createIndexBuffer(
            quadIndices, MaxIndices
        ) as XGLIndexBuffer
        QuadVertexArray.setIndexBuffer(indexBuffer)
    }

    private fun initWhiteTexture() {
        WhiteTexture = XGLTextureBuilder.createTexture(1, 1)!!
        val whiteTextureData = longArrayOf(0xffffffff)
        WhiteTexture.setData(LongBuffer.wrap(whiteTextureData), Int.SIZE_BYTES)
    }

    fun put(value: Vector2){
        QuadVertexBufferBase[QuadVertexBufferPtr++] = value.x
        QuadVertexBufferBase[QuadVertexBufferPtr++] = value.y
    }

    fun put(value: Vector3){
        QuadVertexBufferBase[QuadVertexBufferPtr++] = value.x
        QuadVertexBufferBase[QuadVertexBufferPtr++] = value.y
        QuadVertexBufferBase[QuadVertexBufferPtr++] = value.z
    }

    fun put(value: Vector4){
        QuadVertexBufferBase[QuadVertexBufferPtr++] = value.x
        QuadVertexBufferBase[QuadVertexBufferPtr++] = value.y
        QuadVertexBufferBase[QuadVertexBufferPtr++] = value.z
        QuadVertexBufferBase[QuadVertexBufferPtr++] = value.w
    }

}


