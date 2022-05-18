package com.focus617.core.engine.renderer.texture

import com.focus617.mylib.logging.WithLogging
import java.io.Closeable

abstract class TextureSlots : WithLogging(), Closeable {
    abstract var MaxTextureSlots: Int
    //TODO: RenderCaps

    abstract val TextureSlots: Array<Texture?>
    var TextureSlotIndex: Int = 1         // 0 = 3D:skybox's CubeMap texture Or 2D:WhiteTexture

    override fun close() {
        TextureSlots.forEach() {
            it?.close()
        }
    }

    fun resetTextureSlot(){
        TextureSlotIndex = 1
    }

    fun flush() {
        for (i in 0 until TextureSlotIndex) TextureSlots[i]?.bind(i)
    }

    fun requestIndex(texture: Texture): Int {
        for (i in 1 until TextureSlotIndex)
            if (TextureSlots[i] == texture) {
                return i
            }

        val newIndex = TextureSlotIndex
        if (newIndex < MaxTextureSlots) {
            TextureSlots[TextureSlotIndex] = texture
            TextureSlotIndex++
            return newIndex
        } else {
            val str = "Texture already reached to Max Texture Slot!"
            LOG.error(str)
            throw IllegalMonitorStateException(str)
        }
    }
}