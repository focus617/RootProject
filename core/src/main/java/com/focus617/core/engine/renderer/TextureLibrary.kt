package com.focus617.core.engine.renderer

import com.focus617.mylib.logging.WithLogging
import java.io.Closeable

class TextureLibrary : WithLogging(), Closeable {
    private val mTextures = HashMap<String, Texture>()

    override fun close() {
        mTextures.forEach(){
            it.value.close()
        }
        mTextures.clear()
    }

    fun add(name: String, texture: Texture) {
        if (exists(name)) {
            LOG.warn("Texture $name has already exist")
        }
        mTextures[name] = texture
    }

    fun remove(textureName: String){
        mTextures.remove(textureName)
    }

    fun get(name: String): Texture? = mTextures[name]

    private fun exists(name: String): Boolean = mTextures.containsKey(name)

}