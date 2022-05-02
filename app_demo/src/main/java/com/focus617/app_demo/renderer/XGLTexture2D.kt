package com.focus617.app_demo.renderer

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES20.*
import android.opengl.GLES30
import android.opengl.GLES30.GL_RGBA8
import android.opengl.GLES31
import android.opengl.GLUtils
import com.focus617.core.engine.renderer.Texture2D
import java.io.IOException
import java.nio.Buffer
import java.nio.ByteBuffer

/**
 * OpenGL纹理类 XGLTexture
 * 1. 储存了纹理的基本属性 [mWidth] [mHeight]
 * 2. 它的构造器需要纹理的图片资源或文件
 */
class XGLTexture2D private constructor() : Texture2D() {
    private val textureObjectIdBuf = IntArray(1)
    private var textureObjectId: Int = 0

    private var mInternalFormat: Int = GL_RGBA8
    private var mDataFormat: Int = GL_RGBA

    override var mWidth: Int = 0
    override var mHeight: Int = 0

    override fun equals(other: Any?): Boolean =
        if (other !is XGLTexture2D) false
        else textureObjectId == other.textureObjectId

    /** 基于Assets中的文件构造 */
    constructor(context: Context, filePath: String) : this() {
        val bitmap = loadTextureFromFile(context, filePath)
        bitmap?.apply { initTexture(bitmap) }
    }

    /** 基于Resource/raw中的文件构造 */
    constructor(context: Context, resourceId: Int) : this() {
        val bitmap = loadTextureFromResource(context, resourceId)
        bitmap?.apply { initTexture(bitmap) }
    }

    /** 程序编程构造 */
    constructor(width: Int, height: Int) : this() {
        mWidth = width
        mHeight = height

        mInternalFormat = GL_RGBA8
        mDataFormat = GL_RGBA

        GLES31.glGenTextures(1, textureObjectIdBuf, 0)
        if (textureObjectIdBuf[0] == 0) {
            LOG.error("Could not generate a new OpenGL texture object.")
        }
        textureObjectId = textureObjectIdBuf[0]

        // Bind to the texture in OpenGL
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureObjectId)

        // Allocate texture storage
        GLES31.glTexStorage2D(GL_TEXTURE_2D, 1, mInternalFormat, mWidth, mHeight)

        glTexParameteri(textureObjectId, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(textureObjectId, GL_TEXTURE_MAG_FILTER, GL_NEAREST)

        glTexParameteri(textureObjectId, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(textureObjectId, GL_TEXTURE_WRAP_T, GL_REPEAT)
    }

    private fun initTexture(bitmap: Bitmap) {
        bitmap.apply {
            mWidth = bitmap.width
            mHeight = bitmap.height
            loadImageIntoTexture(bitmap)
        }
        // Recycle the bitmap, since its data has been loaded into OpenGL.
        bitmap.recycle()
    }

    override fun setData(data: Buffer, size: Int) {
        val bpp = if (mDataFormat == GL_RGBA) 4 else 3
        require(size == (mWidth * mHeight * bpp)) { "Data must be entire texture!" }

        // Bind to the texture in OpenGL
        glBindTexture(GL_TEXTURE_2D, textureObjectId)
        glTexSubImage2D(
            GL_TEXTURE_2D,
            0,
            0,
            0,
            mWidth,
            mHeight,
            mDataFormat,
            GL_UNSIGNED_BYTE,
            data
        )
        // Unbind from the texture.
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, 0)
    }

    override fun bind(slot: Int) {
        when (slot) {
            0 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE0)
            1 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE1)
            2 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE2)
            3 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE3)
            4 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE4)
            5 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE5)
            6 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE6)
            7 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE7)
            8 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE8)
            9 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE9)
            10 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE10)
            11 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE11)
            12 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE12)
            13 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE13)
            14 -> GLES31.glActiveTexture(GLES31.GL_TEXTURE14)
            else -> GLES31.glActiveTexture(GLES31.GL_TEXTURE15)
        }

        // Bind to the texture in OpenGL
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureObjectId)
    }

    override fun close() {
        GLES31.glDeleteTextures(1, textureObjectIdBuf, 0)
    }

    /**
     * Loads a texture from a file, returning the OpenGL ID for that
     * texture. Returns 0 if the load failed.
     *
     * @param bitmap
     * @return textureObjectId
     */
    private fun loadImageIntoTexture(bitmap: Bitmap): Int {

        GLES31.glGenTextures(1, textureObjectIdBuf, 0)
        if (textureObjectIdBuf[0] == 0) {
            LOG.error("Could not generate a new OpenGL texture object.")
            return 0
        }
        textureObjectId = textureObjectIdBuf[0]

        // Bind to the texture in OpenGL
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureObjectId)

        //绑定纹理单元与sampler
        GLES31.glBindSampler(0, samplers[0])

        // Set
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_S, GL_REPEAT)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_WRAP_T, GL_REPEAT)

        // Set filtering: a default must be set, or the texture will be black.
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        val byteBuf = ByteBuffer.allocate(bitmap.width * bitmap.height * 4)
        bitmap.copyPixelsToBuffer(byteBuf)
        byteBuf.position(0)

        // Load the bitmap into the bound texture.
        GLES31.glTexImage2D(
            GLES30.GL_TEXTURE_2D,
            0,
            GLES30.GL_RGBA,
            bitmap.width,
            bitmap.height,
            0,
            GLES30.GL_RGBA,
            GLES30.GL_UNSIGNED_BYTE,
            byteBuf
        )
        //  GLUtils.texImage2D(GLES31.GL_TEXTURE_2D, 0, bitmap, 0)

        // Note: Following code may cause an error to be reported in the
        // ADB log as follows: E/IMGSRV(20095): :0: HardwareMipGen:
        // Failed to generate texture mipmap levels (error=3)
        // No OpenGL error will be encountered (glGetError() will return
        // 0). If this happens, just squash the source image to be
        // square. It will look the same because of texture coordinates,
        // and mipmap generation will work.
        GLES31.glGenerateMipmap(GLES31.GL_TEXTURE_2D)

        // Unbind from the texture.
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, 0)

        return textureObjectId
    }


    companion object TextureHelper {
        val TAG = "XGLShader"
        private var samplers = IntArray(1) //存放Samplers id的成员变量数组

        init {
            initSampler()
        }

        private fun initSampler() {        //初始化Sampler对象的方法
            GLES31.glGenSamplers(1, samplers, 0) //生成Samplers id
            GLES31.glSamplerParameterf(
                samplers[0],
                GLES31.GL_TEXTURE_MIN_FILTER,
                GLES31.GL_LINEAR_MIPMAP_LINEAR.toFloat()
            ) //设置MIN采样方式
            GLES31.glSamplerParameterf(
                samplers[0],
                GLES31.GL_TEXTURE_MAG_FILTER,
                GLES31.GL_LINEAR.toFloat()
            ) //设置MAG采样方式
            GLES31.glSamplerParameterf(
                samplers[0],
                GLES31.GL_TEXTURE_WRAP_S,
                GLES31.GL_REPEAT.toFloat()
            ) //设置S轴拉伸方式
            GLES31.glSamplerParameterf(
                samplers[0],
                GLES31.GL_TEXTURE_WRAP_T,
                GLES31.GL_REPEAT.toFloat()
            ) //设置T轴拉伸方式
        }

        /**
         * Loads a texture from a resource ID, returning the OpenGL ID for that
         * texture. Returns 0 if the load failed.
         *
         * @param context
         * @param resourceId
         * @return
         */
        private fun loadTextureFromResource(context: Context, resourceId: Int): Bitmap? {
            LOG.info("${TAG}: load texture from file: $resourceId")

            var bitmap: Bitmap? = null
            val options = BitmapFactory.Options()
            options.inScaled = false

            // Read in the resource
            bitmap = BitmapFactory.decodeResource(
                context.resources, resourceId, options
            )
            if (bitmap == null) {
                LOG.error("Resource ID $resourceId could not be decoded.")
            }
            return bitmap
        }

        /**
         * Loads a texture from a file, returning the OpenGL ID for that
         * texture. Returns 0 if the load failed.
         *
         * @param context
         * @param filePath
         * @return
         */
        private fun loadTextureFromFile(context: Context, filePath: String): Bitmap? {
            LOG.info("${TAG}: load texture from file: $filePath")

            var bitmap: Bitmap? = null

            val options = BitmapFactory.Options()
            options.inScaled = false

            try {
                val inputStream = context.resources.assets.open(filePath)
                // Read in the resource
                bitmap = BitmapFactory.decodeStream(inputStream)
                if (bitmap == null) {
                    LOG.error("$filePath could not be decoded.")
                }
            } catch (e: IOException) {
                throw RuntimeException("Could not open shader file: $filePath $ e")
            }
            return bitmap
        }

        /**
         * Loads a cubemap texture from the provided resources and returns the
         * texture ID. Returns 0 if the load failed.
         *
         * @param context
         * @param cubeResources
         * An array of resources corresponding to the cube map. Should be
         * provided in this order: left, right, bottom, top, front, back.
         * @return
         */
        fun loadCubeMap(context: Context, cubeResources: IntArray): Int {
            val textureObjectIds = IntArray(1)
            GLES31.glGenTextures(1, textureObjectIds, 0)
            if (textureObjectIds[0] == 0) {
                LOG.warn("Could not generate a new OpenGL texture object.")
                return 0
            }
            val options = BitmapFactory.Options()
            options.inScaled = false

            val cubeBitmaps = arrayOfNulls<Bitmap>(6)

            for (i in 0..5) {
                cubeBitmaps[i] = BitmapFactory.decodeResource(
                    context.resources,
                    cubeResources[i], options
                )
                if (cubeBitmaps[i] == null) {
                    LOG.warn("Resource ID ${cubeResources[i]} could not be decoded.")
                    GLES31.glDeleteTextures(1, textureObjectIds, 0)
                    return 0
                }
            }

            // Linear filtering for minification and magnification
            GLES31.glBindTexture(GLES31.GL_TEXTURE_CUBE_MAP, textureObjectIds[0])
            // 因为每个立方体贴图的纹理都总是从同一个视点被观察，不必使用MIP
            // 所以我们只使用常规的双线性过滤，也能节省纹理内存。
            GLES31.glTexParameteri(
                GLES31.GL_TEXTURE_CUBE_MAP,
                GLES31.GL_TEXTURE_MIN_FILTER,
                GLES31.GL_LINEAR
            )
            GLES31.glTexParameteri(
                GLES31.GL_TEXTURE_CUBE_MAP,
                GLES31.GL_TEXTURE_MAG_FILTER,
                GLES31.GL_LINEAR
            )
            // 立方体贴图的惯例是： 在立方体内部使用左手坐标系统，而在立方体外部使用右手坐标系统
            // 要以左、右、下、上、前和后的顺序把每张图像与其对应的立方体贴图的面关联起来
            GLUtils.texImage2D(GLES31.GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, cubeBitmaps[0], 0)
            GLUtils.texImage2D(GLES31.GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, cubeBitmaps[1], 0)
            GLUtils.texImage2D(GLES31.GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, cubeBitmaps[2], 0)
            GLUtils.texImage2D(GLES31.GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, cubeBitmaps[3], 0)
            GLUtils.texImage2D(GLES31.GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, cubeBitmaps[4], 0)
            GLUtils.texImage2D(GLES31.GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, cubeBitmaps[5], 0)
            GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, 0)

            for (bitmap in cubeBitmaps) {
                bitmap!!.recycle()
            }

            return textureObjectIds[0]
        }

        // 多重纹理(multitexturing)
        enum class FilterMode {
            NEAREST_NEIGHBOUR,
            BILINEAR,
            BILINEAR_WITH_MIPMAPS,
            TRILINEAR,
            ANISOTROPIC
        }

        private const val TEXTURE_MAX_ANISOTROPY_EXT = 0x84FE

        fun adjustTextureFilters(
            textureId: Int,
            filterMode: FilterMode,
            supportsAnisotropicFiltering: Boolean,
            maxAnisotropy: Float
        ) {
            GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureId)

            if (supportsAnisotropicFiltering) {
                if (filterMode == FilterMode.ANISOTROPIC) {
                    GLES31.glTexParameterf(
                        GLES31.GL_TEXTURE_2D,
                        TEXTURE_MAX_ANISOTROPY_EXT,
                        maxAnisotropy
                    )
                } else {
                    GLES31.glTexParameterf(GLES31.GL_TEXTURE_2D, TEXTURE_MAX_ANISOTROPY_EXT, 1.0f)
                }
            }

            when (filterMode) {
                FilterMode.NEAREST_NEIGHBOUR -> {
                    GLES31.glTexParameteri(
                        GLES31.GL_TEXTURE_2D,
                        GLES31.GL_TEXTURE_MIN_FILTER,
                        GLES31.GL_NEAREST
                    )
                    GLES31.glTexParameteri(
                        GLES31.GL_TEXTURE_2D,
                        GLES31.GL_TEXTURE_MAG_FILTER,
                        GLES31.GL_NEAREST
                    )
                }
                FilterMode.BILINEAR -> {
                    GLES31.glTexParameteri(
                        GLES31.GL_TEXTURE_2D,
                        GLES31.GL_TEXTURE_MIN_FILTER,
                        GLES31.GL_LINEAR
                    )
                    GLES31.glTexParameteri(
                        GLES31.GL_TEXTURE_2D,
                        GLES31.GL_TEXTURE_MAG_FILTER,
                        GLES31.GL_LINEAR
                    )
                }
                FilterMode.BILINEAR_WITH_MIPMAPS -> {
                    GLES31.glTexParameteri(
                        GLES31.GL_TEXTURE_2D,
                        GLES31.GL_TEXTURE_MIN_FILTER,
                        GLES31.GL_LINEAR_MIPMAP_NEAREST
                    )
                    GLES31.glTexParameteri(
                        GLES31.GL_TEXTURE_2D,
                        GLES31.GL_TEXTURE_MAG_FILTER,
                        GLES31.GL_LINEAR
                    )
                }
                FilterMode.TRILINEAR -> {
                    GLES31.glTexParameteri(
                        GLES31.GL_TEXTURE_2D,
                        GLES31.GL_TEXTURE_MIN_FILTER,
                        GLES31.GL_LINEAR_MIPMAP_LINEAR
                    )
                    GLES31.glTexParameteri(
                        GLES31.GL_TEXTURE_2D,
                        GLES31.GL_TEXTURE_MAG_FILTER,
                        GLES31.GL_LINEAR
                    )
                }
                FilterMode.ANISOTROPIC -> {
                    GLES31.glTexParameteri(
                        GLES31.GL_TEXTURE_2D,
                        GLES31.GL_TEXTURE_MIN_FILTER,
                        GLES31.GL_LINEAR_MIPMAP_LINEAR
                    )
                    GLES31.glTexParameteri(
                        GLES31.GL_TEXTURE_2D,
                        GLES31.GL_TEXTURE_MAG_FILTER,
                        GLES31.GL_LINEAR
                    )
                }
            }

            GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, 0)
        }

    }
}