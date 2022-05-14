package com.focus617.app_demo.renderer.texture

import android.opengl.GLES31.*
import com.focus617.core.engine.renderer.texture.Texture
import com.focus617.core.engine.renderer.texture.TextureSlots

object XGLTextureSlots : TextureSlots() {

    override val MaxTextureSlots: Int
        get() = 16   //Opengl es can support up to 16 samplers

    override val TextureSlots: Array<Texture?> = arrayOfNulls(MaxTextureSlots)

    fun getTextureUnit(slot: Int) = when (slot) {
        0 -> GL_TEXTURE0
        1 -> GL_TEXTURE1
        2 -> GL_TEXTURE2
        3 -> GL_TEXTURE3
        4 -> GL_TEXTURE4
        5 -> GL_TEXTURE5
        6 -> GL_TEXTURE6
        7 -> GL_TEXTURE7
        8 -> GL_TEXTURE8
        9 -> GL_TEXTURE9
        10 -> GL_TEXTURE10
        11 -> GL_TEXTURE11
        12 -> GL_TEXTURE12
        13 -> GL_TEXTURE13
        14 -> GL_TEXTURE14
        else -> GL_TEXTURE15
    }
}