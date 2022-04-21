package com.focus617.platform.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES31.*
import android.opengl.GLUtils
import timber.log.Timber
import java.io.IOException


object TextureHelper {

    private var samplers = IntArray(1) //存放Samplers id的成员变量数组

    init {
        initSampler()
    }

    private fun initSampler() {        //初始化Sampler对象的方法
        glGenSamplers(1, samplers, 0) //生成Samplers id
        glSamplerParameterf(
            samplers[0],
            GL_TEXTURE_MIN_FILTER,
            GL_LINEAR_MIPMAP_LINEAR.toFloat()
        ) //设置MIN采样方式
        glSamplerParameterf(samplers[0], GL_TEXTURE_MAG_FILTER, GL_LINEAR.toFloat()) //设置MAG采样方式
        glSamplerParameterf(samplers[0], GL_TEXTURE_WRAP_S, GL_REPEAT.toFloat()) //设置S轴拉伸方式
        glSamplerParameterf(samplers[0], GL_TEXTURE_WRAP_T, GL_REPEAT.toFloat()) //设置T轴拉伸方式
    }

    /**
     * Loads a texture from a resource ID, returning the OpenGL ID for that
     * texture. Returns 0 if the load failed.
     *
     * @param context
     * @param resourceId
     * @return
     */
    fun loadTexture(context: Context, resourceId: Int): Int {

        Timber.d("loadTexture($resourceId)")

        val textureObjectIds = IntArray(1)
        glGenTextures(1, textureObjectIds, 0)
        if (textureObjectIds[0] == 0) {
            Timber.w("Could not generate a new OpenGL texture object.")
            return 0
        }
        val options = BitmapFactory.Options()
        options.inScaled = false

        // Read in the resource
        val bitmap = BitmapFactory.decodeResource(
            context.resources, resourceId, options
        )
        if (bitmap == null) {
            Timber.w("Resource ID $resourceId could not be decoded.")
            glDeleteTextures(1, textureObjectIds, 0)
            return 0
        }
        // Bind to the texture in OpenGL
        glBindTexture(GL_TEXTURE_2D, textureObjectIds[0])

        //绑定纹理单元与sampler
        glBindSampler(0, samplers[0])
        // Set filtering: a default must be set, or the texture will be black.
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
//        glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)

        // Load the bitmap into the bound texture.
        GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0)

        // Note: Following code may cause an error to be reported in the
        // ADB log as follows: E/IMGSRV(20095): :0: HardwareMipGen:
        // Failed to generate texture mipmap levels (error=3)
        // No OpenGL error will be encountered (glGetError() will return
        // 0). If this happens, just squash the source image to be
        // square. It will look the same because of texture coordinates,
        // and mipmap generation will work.
        glGenerateMipmap(GL_TEXTURE_2D)

        // Recycle the bitmap, since its data has been loaded into
        // OpenGL.
        bitmap.recycle()

        // Unbind from the texture.
        glBindTexture(GL_TEXTURE_2D, 0)

        return textureObjectIds[0]
    }

    /**
     * Loads a texture from a file, returning the OpenGL ID for that
     * texture. Returns 0 if the load failed.
     *
     * @param context
     * @param filePath
     * @return
     */
    fun loadTextureFromFile(context: Context, filePath: String): Int {

        Timber.d("loadTextureFromFile(): $filePath")

        val textureObjectIds = IntArray(1)
        glGenTextures(1, textureObjectIds, 0)
        if (textureObjectIds[0] == 0) {
            Timber.w("Could not generate a new OpenGL texture object.")
            return 0
        }
        val options = BitmapFactory.Options()
        options.inScaled = false

        try {
            val inputStream = context.resources.assets.open(filePath)
            // Read in the resource
            val bitmap = BitmapFactory.decodeStream(inputStream)
            if (bitmap == null) {
                Timber.w("$filePath could not be decoded.")
                glDeleteTextures(1, textureObjectIds, 0)
                return 0
            }
            // Bind to the texture in OpenGL
            glBindTexture(GL_TEXTURE_2D, textureObjectIds[0])

            //绑定纹理单元与sampler
            glBindSampler(0, samplers[0])

            // Load the bitmap into the bound texture.
            GLUtils.texImage2D(GL_TEXTURE_2D, 0, bitmap, 0)

            // Note: Following code may cause an error to be reported in the
            // ADB log as follows: E/IMGSRV(20095): :0: HardwareMipGen:
            // Failed to generate texture mipmap levels (error=3)
            // No OpenGL error will be encountered (glGetError() will return
            // 0). If this happens, just squash the source image to be
            // square. It will look the same because of texture coordinates,
            // and mipmap generation will work.
            glGenerateMipmap(GL_TEXTURE_2D)

            // Recycle the bitmap, since its data has been loaded into
            // OpenGL.
            bitmap.recycle()

        } catch (e: IOException) {
            throw RuntimeException("Could not open shader file: $filePath $ e")
        }

        // Unbind from the texture.
        glBindTexture(GL_TEXTURE_2D, 0)

        return textureObjectIds[0]
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
        glGenTextures(1, textureObjectIds, 0)
        if (textureObjectIds[0] == 0) {
            Timber.w("Could not generate a new OpenGL texture object.")
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
                Timber.w("Resource ID ${cubeResources[i]} could not be decoded.")
                glDeleteTextures(1, textureObjectIds, 0)
                return 0
            }
        }

        // Linear filtering for minification and magnification
        glBindTexture(GL_TEXTURE_CUBE_MAP, textureObjectIds[0])
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
        glTexParameteri(GL_TEXTURE_CUBE_MAP, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
        GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_X, 0, cubeBitmaps[0], 0)
        GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_X, 0, cubeBitmaps[1], 0)
        GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Y, 0, cubeBitmaps[2], 0)
        GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Y, 0, cubeBitmaps[3], 0)
        GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_NEGATIVE_Z, 0, cubeBitmaps[4], 0)
        GLUtils.texImage2D(GL_TEXTURE_CUBE_MAP_POSITIVE_Z, 0, cubeBitmaps[5], 0)
        glBindTexture(GL_TEXTURE_2D, 0)

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
        glBindTexture(GL_TEXTURE_2D, textureId)

        if (supportsAnisotropicFiltering) {
            if (filterMode == FilterMode.ANISOTROPIC) {
                glTexParameterf(GL_TEXTURE_2D, TEXTURE_MAX_ANISOTROPY_EXT, maxAnisotropy)
            } else {
                glTexParameterf(GL_TEXTURE_2D, TEXTURE_MAX_ANISOTROPY_EXT, 1.0f)
            }
        }

        when (filterMode) {
            FilterMode.NEAREST_NEIGHBOUR -> {
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_NEAREST)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_NEAREST)
            }
            FilterMode.BILINEAR -> {
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
            }
            FilterMode.BILINEAR_WITH_MIPMAPS -> {
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_NEAREST)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
            }
            FilterMode.TRILINEAR -> {
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
            }
            FilterMode.ANISOTROPIC -> {
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MIN_FILTER, GL_LINEAR_MIPMAP_LINEAR)
                glTexParameteri(GL_TEXTURE_2D, GL_TEXTURE_MAG_FILTER, GL_LINEAR)
            }
        }

        glBindTexture(GL_TEXTURE_2D, 0)
    }

}
