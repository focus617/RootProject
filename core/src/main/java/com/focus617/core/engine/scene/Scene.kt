package com.focus617.core.engine.scene

import com.focus617.core.engine.renderer.Texture
import com.focus617.core.platform.base.BaseEntity
import java.io.Closeable

class Scene(val mCamera: Camera) : BaseEntity(), Closeable {
    private val textureSet = HashMap<String, Texture>()

    fun sizeForTest() = textureSet.size

    fun register(name: String, texture: Texture) {
        textureSet[name] = texture
    }

    fun unRegister(name: String) {
        textureSet.remove(name)
    }

    fun texture(name: String) = textureSet[name]

    override fun close() {
        textureSet.forEach() {
            it.value.close()
        }
    }

}