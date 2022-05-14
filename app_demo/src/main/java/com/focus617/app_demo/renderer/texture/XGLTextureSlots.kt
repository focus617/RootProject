package com.focus617.app_demo.renderer.texture

import android.opengl.GLES31.GL_TEXTURE0
import com.focus617.core.engine.renderer.texture.Texture
import com.focus617.core.engine.renderer.texture.TextureSlots

object XGLTextureSlots : TextureSlots() {

    //textureUnit has to be a value from a range [0, GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS - 1]
    override val MaxTextureSlots: Int
        get() = 16   // TODO: retrieve it from opengl
    //Opengl es can support up to 16 samplers

    override val TextureSlots: Array<Texture?> = arrayOfNulls(MaxTextureSlots)

    fun getTextureUnit(slot: Int) =
        if (slot < 0 || slot > MaxTextureSlots - 1) -1
        else GL_TEXTURE0 + slot
}