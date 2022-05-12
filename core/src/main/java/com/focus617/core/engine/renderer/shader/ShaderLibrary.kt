package com.focus617.core.engine.renderer.shader

import com.focus617.mylib.logging.WithLogging
import java.io.Closeable

class ShaderLibrary : WithLogging(), Closeable {
    private val mShaders = HashMap<String, Shader>()

    override fun close() {
        mShaders.forEach(){
            it.value.close()
        }
        mShaders.clear()
    }

    fun add(shader: Shader) {
        val name = shader.getName()
        if (exists(name)) {
            LOG.warn("Shader $name has already exist")
            return
        }
        mShaders[name] = shader
    }

    fun remove(shader: Shader){
        mShaders.remove(shader.getName())
    }

    fun get(name: String): Shader? = mShaders[name]

    private fun exists(name: String): Boolean = mShaders.containsKey(name)

}