package com.focus617.core.engine.renderer

import com.focus617.mylib.logging.WithLogging

abstract class ShaderBuilder : WithLogging() {
    abstract fun createShader(vertexShaderSrc: String, fragmentShaderSrc: String): Shader?
}