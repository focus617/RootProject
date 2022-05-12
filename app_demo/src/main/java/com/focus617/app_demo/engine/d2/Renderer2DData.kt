package com.focus617.app_demo.engine.d2

import android.content.Context
import com.focus617.app_demo.renderer.shader.XGLShader
import com.focus617.app_demo.renderer.shader.XGLShaderBuilder
import com.focus617.app_demo.renderer.texture.XGLTextureBuilder
import com.focus617.app_demo.renderer.texture.XGLTextureSlots
import com.focus617.app_demo.renderer.vertex.XGLIndexBuffer
import com.focus617.app_demo.renderer.vertex.XGLVertexArray
import com.focus617.app_demo.renderer.vertex.XGLVertexBuffer
import com.focus617.app_demo.renderer.vertex.XGLVertexBufferBuilder
import com.focus617.core.engine.math.Point3D
import com.focus617.core.engine.math.Vector2
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.Vector4
import com.focus617.core.engine.renderer.*
import com.focus617.core.engine.resource.baseDataType.Color
import com.focus617.mylib.logging.WithLogging
import java.io.Closeable
import java.nio.LongBuffer

object Renderer2DData : WithLogging(), Closeable {
    val MaxQuads: Int = 10000
    val MaxVertices: Int = MaxQuads * 4
    val MaxIndices: Int = MaxQuads * 6

    var initialized: Boolean = false

    lateinit var QuadVertexArray: XGLVertexArray
    lateinit var QuadVertexBuffer: XGLVertexBuffer
    lateinit var TextureShader: XGLShader
    lateinit var WhiteTexture: Texture2D

    var QuadIndexCount: Int = 0

    lateinit var QuadVertexBufferBase: FloatArray
    var QuadVertexBufferPtr: Int = 0    // Index of FloatArray(记住计算size时要乘4)
    var QuadVertexNumber: Int = 0       // 当前已经填充的顶点数目

    val QuadVertexPosition = arrayOf<Vector4>(
        Vector4(-0.5f, -0.5f, 0.0f, 1.0f),
        Vector4(0.5f, -0.5f, 0.0f, 1.0f),
        Vector4(0.5f, 0.5f, 0.0f, 1.0f),
        Vector4(-0.5f, 0.5f, 0.0f, 1.0f)
    )

    val stats: Statistics = Statistics(0, 0)


    override fun close() {
        initialized = false
        QuadVertexBuffer.close()
        QuadVertexArray.close()
        TextureShader.close()
        WhiteTexture.close()
        QuadVertexBufferBase = FloatArray(1)
    }

    fun initStaticData(context: Context) {
        initShader(context)
        initVertexArray()
        initTexture()
        stats.resetStats()
        initialized = true
    }

    private val PATH = "Quad"
    private val TEXTURE_SHADER_FILE = "Texture.glsl"

    private fun initShader(context: Context) {
        TextureShader = XGLShaderBuilder.createShader(
            context,
            "${PATH}/${TEXTURE_SHADER_FILE}"
        ) as XGLShader
    }

    private fun initVertexArray() {
        QuadVertexArray = XGLVertexBufferBuilder.createVertexArray() as XGLVertexArray

        val quadVertexBufferSize = MaxVertices * QuadVertex.sizeInFloat * Float.SIZE_BYTES
        QuadVertexBuffer =
            XGLVertexBufferBuilder.createVertexBuffer(quadVertexBufferSize) as XGLVertexBuffer
        QuadVertexBuffer.setLayout(
            BufferLayout(
                listOf(
                    BufferElement("a_Position", ShaderDataType.Float3, true),
                    BufferElement("a_Color", ShaderDataType.Float4, false),
                    BufferElement("a_TexCoord", ShaderDataType.Float2, true),
                    BufferElement("a_TexIndex", ShaderDataType.Float1, false),
                    BufferElement("a_TilingFactor", ShaderDataType.Float1, false)
                )
            )
        )
        QuadVertexArray.addVertexBuffer(QuadVertexBuffer)

        // 按照MaxVertices，构造一个完整的Vertices空间
        QuadVertexBufferBase = FloatArray(MaxVertices * QuadVertex.sizeInFloat)

        // 构造一个完整的Indices，具体使用时有效范围由QuadIndexCount决定
        val quadIndices = ShortArray(MaxIndices)
        var offset: Int = 0
        for (i in 0 until MaxIndices step 6) {
            // Quad的顶点顺序是: 0左下, 1右下, 2右上, 3左上
            quadIndices[i + 0] = (offset + 0).toShort()
            quadIndices[i + 1] = (offset + 1).toShort()
            quadIndices[i + 2] = (offset + 2).toShort()

            quadIndices[i + 3] = (offset + 2).toShort()
            quadIndices[i + 4] = (offset + 3).toShort()
            quadIndices[i + 5] = (offset + 0).toShort()

            offset += 4
        }

        val indexBuffer = XGLVertexBufferBuilder.createIndexBuffer(
            quadIndices, MaxIndices
        ) as XGLIndexBuffer
        QuadVertexArray.setIndexBuffer(indexBuffer)
    }

    private fun initTexture() {
        val samplers: IntArray = IntArray(XGLTextureSlots.MaxTextureSlots) { i -> i }
        TextureShader.bind()
        TextureShader.setIntArray("u_Textures", samplers, XGLTextureSlots.MaxTextureSlots)

        WhiteTexture = XGLTextureBuilder.createTexture(1, 1)!!
        val whiteTextureData = longArrayOf(0xffffffff)
        WhiteTexture.setData(LongBuffer.wrap(whiteTextureData), Int.SIZE_BYTES)

        XGLTextureSlots.TextureSlots[0] = WhiteTexture
    }

    fun resetVertexBuffer() {
        QuadVertexBufferPtr = 0
        QuadVertexNumber = 0
        QuadIndexCount = 0
    }

    fun putQuadVertex(
        position: Point3D,
        size: Vector2,
        color: Color,
        texCoords: Vector2,
        texIndex: Float = 0.0f,         // White Texture
        tilingFactor: Float = 1.0f
    ) {
        putVertex(   //0左下
            position, color, Vector2(0.0f, 0.0f), texIndex, tilingFactor
        )
        putVertex(   //1右下
            Point3D(position.x + size.x, position.y, 0.0f),
            color,
            Vector2(1.0f, 0.0f),
            texIndex,
            tilingFactor
        )
        putVertex(   //2右上
            Point3D(position.x + size.x, position.y + size.y, 0.0f),
            color,
            Vector2(1.0f, 1.0f),
            texIndex,
            tilingFactor
        )
        putVertex(   //3左上
            Point3D(position.x, position.y + size.y, 0.0f),
            color,
            Vector2(0.0f, 1.0f),
            texIndex,
            tilingFactor
        )
    }

    fun putQuadVertex(
        position: Point3D,
        size: Vector2,
        color: Color,
        subTexCoords: SubTexture2D,
        texIndex: Float = 0.0f,         // White Texture
        tilingFactor: Float = 1.0f
    ) {
        putVertex(   //0左下
            position, color, subTexCoords.mTexCoords[0], texIndex, tilingFactor
        )
        putVertex(   //1右下
            Point3D(position.x + size.x, position.y, 0.0f),
            color,
            subTexCoords.mTexCoords[1],
            texIndex,
            tilingFactor
        )
        putVertex(   //2右上
            Point3D(position.x + size.x, position.y + size.y, 0.0f),
            color,
            subTexCoords.mTexCoords[2],
            texIndex,
            tilingFactor
        )
        putVertex(   //3左上
            Point3D(position.x, position.y + size.y, 0.0f),
            color,
            subTexCoords.mTexCoords[3],
            texIndex,
            tilingFactor
        )
    }

    fun putVertex(
        position: Point3D,
        color: Color,
        texCoords: Vector2,
        texIndex: Float,
        tilingFactor: Float
    ) {
        if (QuadVertexNumber >= MaxVertices) {
            LOG.error("Reach max vertices limitation.")
            return
        }
        put(position)
        put(color)
        put(texCoords)
        put(texIndex)
        put(tilingFactor)
        QuadVertexNumber++
    }

    private fun put(position: Point3D) {
        QuadVertexBufferBase[QuadVertexBufferPtr++] = position.x
        QuadVertexBufferBase[QuadVertexBufferPtr++] = position.y
        QuadVertexBufferBase[QuadVertexBufferPtr++] = position.z
    }

    fun put(value: Float) {
        QuadVertexBufferBase[QuadVertexBufferPtr++] = value
    }

    fun put(value: Vector2) {
        QuadVertexBufferBase[QuadVertexBufferPtr++] = value.x
        QuadVertexBufferBase[QuadVertexBufferPtr++] = value.y
    }

    fun put(value: Vector3) {
        QuadVertexBufferBase[QuadVertexBufferPtr++] = value.x
        QuadVertexBufferBase[QuadVertexBufferPtr++] = value.y
        QuadVertexBufferBase[QuadVertexBufferPtr++] = value.z
    }

    fun put(value: Vector4) {
        QuadVertexBufferBase[QuadVertexBufferPtr++] = value.x
        QuadVertexBufferBase[QuadVertexBufferPtr++] = value.y
        QuadVertexBufferBase[QuadVertexBufferPtr++] = value.z
        QuadVertexBufferBase[QuadVertexBufferPtr++] = value.w
    }

    fun put(color: Color){
        put(color.toVector4())
    }

}


