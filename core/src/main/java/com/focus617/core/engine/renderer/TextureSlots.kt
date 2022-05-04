package com.focus617.core.engine.renderer

import com.focus617.mylib.logging.WithLogging
import java.io.Closeable

object TextureSlots : WithLogging(), Closeable {
    private const val MaxTextureSlots: Int = 16   //TODO: RenderCaps

    val TextureSlots: Array<Texture?> = arrayOfNulls(MaxTextureSlots)
    var TextureSlotIndex: Int = 1        // 0 = skybox's CubeMap texture

    override fun close() {
        TextureSlots.forEach() {
            it?.close()
        }
    }

    fun flush() {
        for (i in 0 until TextureSlotIndex) TextureSlots[i]?.bind(i)
    }

    fun getId(texture: Texture): Int {
        for (i in 1 until TextureSlotIndex)
            if (TextureSlots[i] == texture) {
                return i
            }

        val newIndex = TextureSlotIndex
        if(newIndex< MaxTextureSlots) {
            TextureSlots[TextureSlotIndex] = texture
            TextureSlotIndex++
            return newIndex
        }
        else{
            val str = "Texture already reached to Max Texture Slot!"
            LOG.error(str)
            throw IllegalMonitorStateException(str)
        }
    }
}