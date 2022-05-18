package com.focus617.core.engine.renderer.texture

import com.focus617.mylib.logging.WithLogging
import java.io.Closeable

abstract class TextureSlots : WithLogging(), Closeable {
    abstract var MaxTextureSlots: Int
    //TODO: RenderCaps

    abstract val TextureSlots: Array<Texture?>
    // 0 = 3D:skybox's CubeMap texture Or 2D:WhiteTexture

    override fun close() {
        TextureSlots.forEach() {
            it?.close()
        }
    }

    open fun resetTextureSlot() {
        for (i in 1 until MaxTextureSlots)
            TextureSlots[i] = null
    }

    open fun flush() {
        for (i in 0 until MaxTextureSlots)
            TextureSlots[i]?.bind(i)
    }

    open fun releaseIndex(index: Int) {
        TextureSlots[index] = null
    }

    open fun requestIndex(texture: Texture): Int {
        var newIndex: Int = MaxTextureSlots

        for (i in 1 until MaxTextureSlots) {
            // 如果这个texture对象已经绑定到纹理单元
            if (TextureSlots[i] == texture) {
                return i
            }
            // 同时查找未绑定的最小的纹理单元
            if ((TextureSlots[i] == null) && (i < newIndex)) {
                newIndex = i
            }
        }
        // 将找到的最小空白纹理单元分配给这个新的纹理对象
        if (newIndex < MaxTextureSlots) {
            TextureSlots[newIndex] = texture
            return newIndex
        } else {
            val str = "Texture already reached to Max Texture Slot!"
            LOG.error(str)
            throw IllegalMonitorStateException(str)
        }
    }
}