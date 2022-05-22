package com.focus617.opengles.renderer.texture

import android.content.Context
import android.graphics.Bitmap
import android.opengl.GLES31.*
import com.focus617.core.engine.renderer.texture.Texture
import com.focus617.opengles.renderer.texture.XGLTextureHelper.loadCubeMapIntoTexture
import com.focus617.platform.helper.BitmapHelper
import java.nio.Buffer

class XGLTextureCubeMap private constructor(filePath: String) : Texture(filePath) {
    private val textureObjectIdBuf = IntArray(1)
    override var mHandle: Int = 0

    override var mWidth: Int = 0
    override var mHeight: Int = 0

    constructor(context: Context, path: String, files: Array<String>) : this("$path/SkyBox") {
        val cubeBitmaps = arrayOfNulls<Bitmap>(6)

        for (i in 0..5) {
            cubeBitmaps[i] = BitmapHelper.bitmapLoader(context, "$path/${files[i]}")
        }

        mWidth = cubeBitmaps[0]!!.width
        mHeight = cubeBitmaps[0]!!.height
        mHandle = loadCubeMapIntoTexture(textureObjectIdBuf, cubeBitmaps)

        for (bitmap in cubeBitmaps) {
            bitmap!!.recycle()
        }
    }

    override fun setData(data: Buffer, size: Int) {

    }

    override fun bind(slot: Int) {
        glActiveTexture(GL_TEXTURE0)
        glBindTexture(GL_TEXTURE_CUBE_MAP, mHandle)
    }

    override fun close() {
        glDeleteTextures(1, textureObjectIdBuf, 0)
    }
}