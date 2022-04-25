package com.focus617.core.engine.renderer

import com.focus617.mylib.logging.WithLogging

class ShaderLibrary : WithLogging() {
    private val mShaders = HashMap<String, Shader>()

    fun add(name: String, shader: Shader) {
        if (exists(name)) {
            LOG.warn("Shader $name has already exist")
        }
        mShaders[name] = shader
    }

    fun add(shader: Shader) {
        val name = shader.getName()
        if (exists(name)) {
            LOG.warn("Shader $name has already exist")
        }
        mShaders[name] = shader
    }

    fun remove(shaderName: String){
        mShaders.remove(shaderName)
    }

    fun remove(shader: Shader){
        mShaders.remove(shader.getName())
    }

    fun get(name: String): Shader? = mShaders[name]

    private fun exists(name: String): Boolean = mShaders.containsKey(name)
}