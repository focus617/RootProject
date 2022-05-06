package com.focus617.platform.helper

import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLES30
import android.opengl.GLES31
import android.opengl.GLUtils
import timber.log.Timber
import java.nio.ByteBuffer

object TextureHelper {
    val TAG = "TextureHelper"

    var samplers = IntArray(1) //存放Samplers id的成员变量数组

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
     * Loads a texture from a file, returning the OpenGL ID for that
     * texture. Returns 0 if the load failed.
     *
     * @param bitmap
     * @return textureObjectId
     */
    fun loadImageIntoTexture(
        textureObjectIdBuf: IntArray,
        bitmap: Bitmap
    ): Int {
        GLES31.glGenTextures(1, textureObjectIdBuf, 0)
        if (textureObjectIdBuf[0] == 0) {
            Timber.e("$TAG: Could not generate a new OpenGL texture object.")
            return 0
        }

        // Bind to the texture in OpenGL
        GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, textureObjectIdBuf[0])

        // Set wrapping mode
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_S, GLES20.GL_REPEAT)
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_WRAP_T, GLES20.GL_REPEAT)

        // Set filtering: a default must be set, or the texture will be black.
        GLES20.glTexParameteri(
            GLES20.GL_TEXTURE_2D,
            GLES20.GL_TEXTURE_MIN_FILTER,
            GLES20.GL_LINEAR_MIPMAP_LINEAR
        )
        GLES20.glTexParameteri(GLES20.GL_TEXTURE_2D, GLES20.GL_TEXTURE_MAG_FILTER, GLES20.GL_LINEAR)

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

        return textureObjectIdBuf[0]
    }

    /**
     * Loads a cubemap texture from the provided resources and returns the
     * texture ID. Returns 0 if the load failed.
     *
     * @param context
     * @param cubeBitmaps
     * An array of texture file name corresponding to the cube map. Should be
     * provided in this order: left, right, bottom, top, front, back.
     * @return
     */
    fun loadCubeMapIntoTexture(
        textureObjectIdBuf: IntArray,
        cubeBitmaps: Array<Bitmap?>
    ): Int {
        GLES31.glGenTextures(1, textureObjectIdBuf, 0)
        if (textureObjectIdBuf[0] == 0) {
            Timber.e("$TAG: Could not generate a new OpenGL texture object.")
            return 0
        }

        for (i in 0..5) {
            if (cubeBitmaps[i] == null) {
                Timber.w("$TAG: No.$i Cube Bitmap is null.")
                GLES31.glDeleteTextures(1, textureObjectIdBuf, 0)
                return 0
            }
        }

        // Linear filtering for minification and magnification
        GLES31.glBindTexture(GLES31.GL_TEXTURE_CUBE_MAP, textureObjectIdBuf[0])
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

        //GLES31.glBindTexture(GLES31.GL_TEXTURE_2D, 0)
        GLES31.glBindTexture(GLES31.GL_TEXTURE_CUBE_MAP, 0)

        return textureObjectIdBuf[0]
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