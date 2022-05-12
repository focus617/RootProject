package com.focus617.core.engine.renderer.shader

import com.focus617.mylib.logging.WithLogging

abstract class ShaderBuilder : WithLogging() {
    abstract fun createShader(
        name: String,
        vertexShaderSrc: String,
        fragmentShaderSrc: String
    ): Shader?
}