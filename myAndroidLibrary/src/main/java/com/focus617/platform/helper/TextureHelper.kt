package com.focus617.platform.helper

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.opengl.GLES31
import android.opengl.GLUtils
import timber.log.Timber
import java.io.IOException

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
     * Loads a texture from a resource ID, returning the OpenGL ID for that
     * texture. Returns 0 if the load failed.
     *
     * @param context
     * @param resourceId
     * @return
     */
    fun loadTextureFromResource(context: Context, resourceId: Int): Bitmap? {
        Timber.i("${TAG}: load texture from resource: $resourceId")

        var bitmap: Bitmap? = null
        val options = BitmapFactory.Options()
        options.inScaled = false

        // Read in the resource
        bitmap = BitmapFactory.decodeResource(
            context.resources, resourceId, options
        )
        if (bitmap == null) {
            Timber.e("$TAG: Resource ID $resourceId could not be decoded.")
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
    fun loadTextureFromFile(context: Context, filePath: String): Bitmap? {
        Timber.i("${TAG}: load texture from file: $filePath")

        var bitmap: Bitmap? = null

        val options = BitmapFactory.Options()
        options.inScaled = false

        try {
            val inputStream = context.resources.assets.open(filePath)
            // Read in the resource
            bitmap = BitmapFactory.decodeStream(inputStream)
            if (bitmap == null) {
                Timber.e("$TAG: $filePath could not be decoded.")
            }
        } catch (e: IOException) {
            throw RuntimeException("Could not open shader file: $filePath $ e")
        }
        return bitmap
    }

    //TODO: define XGLTexture3D class
    var skyboxTexture: Int = 0

    /**
     * Loads a cubemap texture from the provided resources and returns the
     * texture ID. Returns 0 if the load failed.
     *
     * @param context
     * @param path
     * An array of texture file name corresponding to the cube map. Should be
     * provided in this order: left, right, bottom, top, front, back.
     * @return
     */
    fun loadCubeMap(
        context: Context,
        path: String,
        files: Array<String>
    ): Int {
        val textureObjectIds = IntArray(1)
        GLES31.glGenTextures(1, textureObjectIds, 0)
        if (textureObjectIds[0] == 0) {
            Timber.w("$TAG: Could not generate a new OpenGL texture object.")
            return 0
        }

        val cubeBitmaps = arrayOfNulls<Bitmap>(6)

        for (i in 0..5) {
            cubeBitmaps[i] = loadTextureFromFile(context, "$path/${files[i]}")
            if (cubeBitmaps[i] == null) {
                Timber.w("$TAG: Resource ID ${files[i]} could not be decoded.")
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