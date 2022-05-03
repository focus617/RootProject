package com.focus617.app_demo.renderer

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES20
import android.opengl.GLES31
import com.focus617.core.engine.renderer.Texture
import com.focus617.platform.helper.TextureHelper
import com.focus617.platform.helper.TextureHelper.loadCubeMapIntoTexture
import java.nio.Buffer

class XGLTextureCubeMap private constructor(filePath: String) : Texture(filePath) {
    private val textureObjectIdBuf = IntArray(1)
    private var textureObjectId: Int = 0

    override var mWidth: Int = 0
    override var mHeight: Int = 0

    constructor(context: Context, path: String, files: Array<String>) : this("$path/$files") {
        val cubeBitmaps = arrayOfNulls<Bitmap>(6)

        for (i in 0..5) {
            cubeBitmaps[i] = TextureHelper.loadTextureFromFile(context, "$path/${files[i]}")
        }

        mWidth = cubeBitmaps[0]!!.width
        mHeight = cubeBitmaps[0]!!.height
        textureObjectId = loadCubeMapIntoTexture(textureObjectIdBuf, cubeBitmaps)

        for (bitmap in cubeBitmaps) {
            bitmap!!.recycle()
        }
    }

    override fun setData(data: Buffer, size: Int) {

    }

    override fun bind(slot: Int) {
        GLES20.glActiveTexture(GLES20.GL_TEXTURE0)
        GLES31.glBindTexture(GLES31.GL_TEXTURE_CUBE_MAP, textureObjectId)
    }

    override fun close() {
        GLES31.glDeleteTextures(1, textureObjectIdBuf, 0)
    }
}