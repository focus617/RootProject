package com.focus617.app_demo.engine.d2

import android.content.Context
import com.focus617.app_demo.renderer.*
import com.focus617.core.engine.math.Vector2
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.Vector4
import com.focus617.core.engine.objects.d2.Quad
import com.focus617.core.engine.renderer.*
import java.io.Closeable
import java.nio.LongBuffer

object Renderer2DData : Closeable {
    val MaxQuads: Int = 10000
    val MaxVertices: Int = MaxQuads * 4
    val MaxIndices: Int = MaxQuads * 6
    val MaxTextureSlots: Int = 16   //TODO: RenderCaps

    val WHITE = Vector4(1.0f, 1.0f, 1.0f, 1.0f)

    var initialized: Boolean = false

    lateinit var QuadVertexArray: XGLVertexArray
    lateinit var QuadVertexBuffer: XGLVertexBuffer
    lateinit var TextureShader: XGLShader
    lateinit var WhiteTexture: Texture2D

    var QuadIndexCount: Int = 0

    lateinit var QuadVertexBufferBase: FloatArray
    var QuadVertexBufferPtr: Int = 0    // Index of FloatArray(记住计算size时要乘4)

    val TextureSlots: Array<Texture2D?> = arrayOfNulls(MaxTextureSlots)
    var TextureSlotIndex: Int = 1        // 0 = white texture

    fun getId(texture: Texture2D): Int {
        for (i in 1 until TextureSlotIndex)
            if (TextureSlots[i] == texture) {
                return i
            }

        val newIndex = TextureSlotIndex
        TextureSlots[TextureSlotIndex] = texture
        TextureSlotIndex++

        return newIndex
    }

    override fun close() {
        initialized = false
        QuadVertexBuffer.close()
        QuadVertexArray.close()
        TextureShader.close()
        WhiteTexture.close()
    }

    fun initStaticData(context: Context) {
        initShader(context)
        initVertexArray()
        initTexture()
        initialized = true
    }

    private val PATH = "SquareWithTexture"
    private val TEXTURE_SHADER_FILE = "Texture.glsl"

    private fun initShader(context: Context) {
        TextureShader = XGLShaderBuilder.createShader(
            context,
            "${PATH}/${TEXTURE_SHADER_FILE}"
        ) as XGLShader
    }

    private fun initVertexArray() {
        QuadVertexArray = XGLBufferBuilder.createVertexArray() as XGLVertexArray

        val quadVertexBufferSize = MaxVertices * QuadVertex.sizeInFloat * Float.SIZE_BYTES
        QuadVertexBuffer =
            XGLBufferBuilder.createVertexBuffer(quadVertexBufferSize) as XGLVertexBuffer
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

    private fun initTexture() {
        val samplers: IntArray = IntArray(MaxTextureSlots) { i -> i }
        TextureShader.bind()
        TextureShader.setIntArray("u_Textures", samplers, MaxTextureSlots)

        WhiteTexture = XGLTextureBuilder.createTexture(1, 1)!!
        val whiteTextureData = longArrayOf(0xffffffff)
        WhiteTexture.setData(LongBuffer.wrap(whiteTextureData), Int.SIZE_BYTES)

        TextureSlots[0] = WhiteTexture
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

    fun drawQuad(position: Vector3, size: Vector2, color: Vector4) {
        val texIndex: Float = 0.0f // White Texture
        val tilingFactor: Float = 1.0f

        put(position)
        put(color)
        put(Vector2(0.0f, 0.0f))
        put(texIndex)
        put(tilingFactor)

        put(Vector3(position.x + size.x, position.y, 0.0f))
        put(color)
        put(Vector2(1.0f, 0.0f))
        put(texIndex)
        put(tilingFactor)

        put(Vector3(position.x + size.x, position.y + size.y, 0.0f))
        put(color)
        put(Vector2(1.0f, 1.0f))
        put(texIndex)
        put(tilingFactor)

        put(Vector3(position.x, position.y + size.y, 0.0f))
        put(color)
        put(Vector2(0.0f, 1.0f))
        put(texIndex)
        put(tilingFactor)

        QuadIndexCount += 6

//        Renderer2DData.TextureShader.setFloat4("u_Color", color)
//        Renderer2DData.TextureShader.setFloat("u_TilingFactor", 1.0f)
//        Renderer2DData.TextureShader.setMat4("u_ModelMatrix", getTransform(position, size))
//
//        // Bind white texture here
//        Renderer2DData.WhiteTexture.bind()
//
//        Renderer2DData.QuadVertexArray.bind()
//        RenderCommand.drawIndexed(Renderer2DData.QuadVertexArray)
    }

    fun drawQuad(position: Vector2, size: Vector2, color: Vector4) {
        drawQuad(Vector3(position.x, position.y, 0.0f), size, color)
    }

    fun drawQuad(
        position: Vector3, size: Vector2, texture: Texture2D, tilingFactor: Float = 1.0f,
        tintColor: Vector4 = WHITE
    ) {
        var textureIndex: Float = 0.0f // White Texture

        for (i in 1 until TextureSlotIndex)
            if (TextureSlots[i] == texture) {
                textureIndex = i.toFloat()
                break
            }

        if (textureIndex == 0.0f) {
            textureIndex = TextureSlotIndex.toFloat()
            TextureSlots[TextureSlotIndex] = texture
            TextureSlotIndex++
        }

        put(position)
        put(WHITE)
        put(Vector2(0.0f, 0.0f))
        put(textureIndex)
        put(tilingFactor)

        put(Vector3(position.x + size.x, position.y, 0.0f))
        put(WHITE)
        put(Vector2(1.0f, 0.0f))
        put(textureIndex)
        put(tilingFactor)

        put(Vector3(position.x + size.x, position.y + size.y, 0.0f))
        put(WHITE)
        put(Vector2(1.0f, 1.0f))
        put(textureIndex)
        put(tilingFactor)

        put(Vector3(position.x, position.y + size.y, 0.0f))
        put(WHITE)
        put(Vector2(0.0f, 1.0f))
        put(textureIndex)
        put(tilingFactor)

        QuadIndexCount += 6

//        Renderer2DData.TextureShader.setFloat4("u_Color", tintColor)
//        Renderer2DData.TextureShader.setFloat("u_TilingFactor", tilingFactor)
//        Renderer2DData.TextureShader.setMat4("u_ModelMatrix", getTransform(position, size))
//
//        // Bind texture
//        texture.bind()
//
//        // Bind VertexArray
//        Renderer2DData.QuadVertexArray.bind()
//        RenderCommand.drawIndexed(Renderer2DData.QuadVertexArray)
    }

    fun drawQuad(
        position: Vector2,
        size: Vector2,
        rotation: Float,
        texture: Texture2D,
        tilingFactor: Float = 1.0f,
        tintColor: Vector4 = Vector4(1.0f, 1.0f, 1.0f, 1.0f)
    ) {
        drawRotatedQuad(
            Vector3(position.x, position.y, 0.0f),
            size,
            rotation,
            texture,
            tilingFactor,
            tintColor
        )
    }

    fun drawRotatedQuad(position: Vector3, size: Vector2, rotation: Float, color: Vector4) {
        TextureShader.setFloat4("u_Color", color)
        TextureShader.setFloat("u_TilingFactor", 1.0f)
        TextureShader.setMat4(
            "u_ModelMatrix",
            getTransform(position, size, rotation)
        )

        // Bind white texture here
        Renderer2DData.WhiteTexture.bind()

        // Bind VertexArray
        QuadVertexArray.bind()
        RenderCommand.drawIndexed(Renderer2DData.QuadVertexArray)
    }

    fun drawRotatedQuad(position: Vector2, size: Vector2, rotation: Float, color: Vector4) {
        drawRotatedQuad(Vector3(position.x, position.y, 0.0f), size, rotation, color)
    }

    fun drawRotatedQuad(
        position: Vector3,
        size: Vector2,
        rotation: Float,
        texture: Texture2D,
        tilingFactor: Float = 1.0f,
        tintColor: Vector4 = WHITE
    ) {
        TextureShader.setFloat4("u_Color", tintColor)
        TextureShader.setFloat("u_TilingFactor", tilingFactor)
        TextureShader.setMat4(
            "u_ModelMatrix",
            getTransform(position, size, rotation)
        )

        // Bind texture
        texture.bind()

        // Bind VertexArray
        QuadVertexArray.bind()
        RenderCommand.drawIndexed(Renderer2DData.QuadVertexArray)
    }

    fun drawRotatedQuad(
        position: Vector2,
        size: Vector2,
        rotation: Float,
        texture: Texture2D,
        tilingFactor: Float = 1.0f,
        tintColor: Vector4 = Vector4(1.0f, 1.0f, 1.0f, 1.0f)
    ) {
        drawRotatedQuad(
            Vector3(position.x, position.y, 0.0f),
            size,
            rotation,
            texture,
            tilingFactor,
            tintColor
        )
    }


    private fun getTransform(
        position: Vector3,
        size: Vector2,
        rotation: Float = 0.0f
    ): FloatArray {
        val quad = Quad()
        quad.resetTransform()
        quad.onTransform2D(position, size, rotation)
        return quad.modelMatrix
    }


}


