package com.focus617.app_demo.renderer.texture

import android.opengl.GLES20.GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS
import android.opengl.GLES20.glGetIntegerv
import android.opengl.GLES31.GL_TEXTURE0
import com.focus617.core.engine.renderer.texture.Texture
import com.focus617.core.engine.renderer.texture.TextureSlots
import java.nio.IntBuffer

object XGLTextureSlots : TextureSlots() {
    //textureUnit has to be a value from a range [0, GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS - 1]
    override var MaxTextureSlots: Int = 16  //Opengl es can support up to 16 samplers

    override lateinit var TextureSlots: Array<Texture?>

    fun initUnderOpenGl() {
        MaxTextureSlots = getMaxTextureSlotNumber()
        LOG.info("Max Texture Slot Number = $MaxTextureSlots")

        TextureSlots = arrayOfNulls(MaxTextureSlots)
    }

    // Must be called in opengl env, such as scene.initOpenGlResource()
    fun getMaxTextureSlotNumber(): Int {
        val numberBuf = IntBuffer.allocate(1)
        glGetIntegerv(GL_MAX_COMBINED_TEXTURE_IMAGE_UNITS, numberBuf)
        return numberBuf[0]
    }

    fun getTextureUnit(slot: Int) =
        if (slot < 0 || slot > MaxTextureSlots - 1) -1
        else GL_TEXTURE0 + slot
}