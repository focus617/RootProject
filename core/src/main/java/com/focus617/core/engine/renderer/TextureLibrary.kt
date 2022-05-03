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

    fun add(texture: Texture) {
        if (mTextures.containsKey(texture.filePath)) {
            LOG.warn("Texture ${texture.filePath} has already exist")
        }
        mTextures[texture.filePath] = texture
    }

    fun remove(texture: Texture){
        mTextures.remove(texture.filePath)
    }

    fun get(name: String): Texture? = mTextures[name]

}