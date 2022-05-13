package com.focus617.app_demo.renderer.texture

import android.graphics.Bitmap
import android.opengl.GLES31.*
import android.opengl.GLUtils
import com.focus617.core.platform.base.BaseEntity
import java.nio.ByteBuffer

object XGLTextureHelper : BaseEntity() {

    var samplers = IntArray(1) //存放Samplers id的成员变量数组

    init {
        initSampler()
    }

    private fun initSampler() {        //初始化Sampler对象的方法
        glGenSamplers(1, samplers, 0) //生成Samplers id
        //设置MIN采样方式
        glSamplerParameterf(
            samplers[0], GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR.toFloat()
        )
        //设置MAG采样方式
        glSamplerParameterf(samplers[0], GL_TEXTURE_MAG_FILTER, GL_LINEAR.toFloat())
        //设置S轴拉伸方式
        glSamplerParameterf(samplers[0], GL_TEXTURE_WRAP_S, GL_REPEAT.toFloat())
        //设置T轴拉伸方式
        glSamplerParameterf(samplers[0], GL_TEXTURE_WRAP_T, GL_REPEAT.toFloat())
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
        glGenTextures(1, textureObjectIdBuf, 0)
        if (textureObjectIdBuf[0] == 0) {
            LOG.error("Could not generate a new OpenGL texture object.")
            return 0
        }

        // Bind to the texture in OpenGL
        glBindTexture(GL_TEXTURE_2D, textureObjectIdBuf[0])

        //绑定纹理单元与sampler
        glBindSampler(textureObjectIdBuf[0], samplers[0])

        val byteBuf = ByteBuffer.allocate(bitmap.width * bitmap.height * 4)
        bitmap.copyPixelsToBuffer(byteBuf)
        byteBuf.position(0)

        // Load the bitmap into the bound texture.
        glTexImage2D(
            GL_TEXTURE_2D,
            0,
            GL_RGBA,
            bitmap.width,
            bitmap.height,
            0,
            GL_RGBA,
            GL_UNSIGNED_BYTE,
            byteBuf
        )
        //  GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0)

        // Note: Following code may cause an error to be reported in the
        // ADB log as follows: E/IMGSRV(20095): :0: HardwareMipGen:
        // Failed to generate texture mipmap levels (error=3)
        // No OpenGL error will be encountered (glGetError() will return
        // 0). If this happens, just squash the source image to be
        // square. It will look the same because of texture coordinates,
        // and mipmap generation will work.
        glGenerateMipmap(GL_TEXTURE_2D)

        // Unbind from the texture.
        glBindTexture(GL_TEXTURE_2D, 0)

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
        glGenTextures(1, textureObjectIdBuf, 0)
        if (textureObjectIdBuf[0] == 0) {
            LOG.error("Could not generate a new OpenGL texture object.")
            return 0
        }

        for (i in 0..5) {
            if (cubeBitmaps[i] == null) {
                LOG.warn("No.$i Cube Bitmap is null.")
                glDeleteTextures(1, textureObjectIdBuf, 0)
                return 0
            }
        }

        // Linear filtering for minification and magnification
        glBindTexture(GL_TEXTURE_CUBE_MAP, textureObjectIdBuf[0])
        // 因为每个立方体贴图的纹理都总是从同一个视点被观察，不必使用MIP
        // 所以我们只使用常规的双线性过滤，也能节省纹理内存。
        glTexParameteri(
            GL_TEXTURE_CUBE_MAP,
            GL_TEXTURE_MIN_FILTER,
            GL_LINEAR
        )
        glTexParameteri(
            GL_TEXTURE_CUBE_MAP,
            GL_TEXTURE_MAG_FILTER,
            GL_LINEAR
        )
        // 立方体贴图的惯例是： 在立方体内部使用左手坐标系统，而在立方体外部使用右手坐标系统
        // 要以左、右、下、上、前和后的顺序把每张图像与其对应的立方体贴图的面关联起来
        GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, cubeBitmaps[0], 0)
        GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, cubeBitmaps[1], 0)
        GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, cubeBitmaps[2], 0)
        GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, cubeBitmaps[3], 0)
        GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, cubeBitmaps[4], 0)
        GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, cubeBitmaps[5], 0)

        glBindTexture(GL_TEXTURE_CUBE_MAP, 0)

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
        glBindTexture(GL_TEXTURE_2D, textureId)

        if (supportsAnisotropicFiltering) {
            if (filterMode == FilterMode.ANISOTROPIC) {
                glTexParameterf(
                    GL_TEXTURE_2D,
                    TEXTURE_MAX_ANISOTROPY_EXT,
                    maxAnisotropy
                )
            } else {
                glTexParameterf(GL_TEXTURE_2D, TEXTURE_MAX_ANISOTROPY_EXT, 1.0f)
            }
        }

        when (filterMode) {
            FilterMode.NEAREST_NEIGHBOUR -> {
                glTexParameteri(
                    GL_TEXTURE_2D,
                    GL_TEXTURE_MIN_FILTER,
                    GL_NEAREST
                )
                glTexParameteri(
                    GL_TEXTURE_2D,
                    GL_TEXTURE_MAG_FILTER,
                    GL_NEAREST
                )
            }
            FilterMode.BILINEAR -> {
                glTexParameteri(
                    GL_TEXTURE_2D,
                    GL_TEXTURE_MIN_FILTER,
                    GL_LINEAR
                )
                glTexParameteri(
                    GL_TEXTURE_2D,
                    GL_TEXTURE_MAG_FILTER,
                    GL_LINEAR
                )
            }
            FilterMode.BILINEAR_WITH_MIPMAPS -> {
                glTexParameteri(
                    GL_TEXTURE_2D,
                    GL_TEXTURE_MIN_FILTER,
                    GL_LINEAR_MIPMAP_NEAREST
                )
                glTexParameteri(
                    GL_TEXTURE_2D,
                    GL_TEXTURE_MAG_FILTER,
                    GL_LINEAR
                )
            }
            FilterMode.TRILINEAR -> {
                glTexParameteri(
                    GL_TEXTURE_2D,
                    GL_TEXTURE_MIN_FILTER,
                    GL_LINEAR_MIPMAP_LINEAR
                )
                glTexParameteri(
                    GL_TEXTURE_2D,
                    GL_TEXTURE_MAG_FILTER,
                    GL_LINEAR
                )
            }
            FilterMode.ANISOTROPIC -> {
                glTexParameteri(
                    GL_TEXTURE_2D,
                    GL_TEXTURE_MIN_FILTER,
                    GL_LINEAR_MIPMAP_LINEAR
                )
                glTexParameteri(
                    GL_TEXTURE_2D,
                    GL_TEXTURE_MAG_FILTER,
                    GL_LINEAR
                )
            }
        }

        glBindTexture(GL_TEXTURE_2D, 0)
    }

}