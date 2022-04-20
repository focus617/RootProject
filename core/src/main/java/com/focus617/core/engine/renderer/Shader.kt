package com.focus617.core.engine.renderer

import com.focus617.core.platform.base.BaseEntity

/**
 * 着色器类 ShaderProgram
 * 1. 储存了着色器程序的ID [mRendererId]
 * 2. 它的构造器需要顶点着色器和片段着色器的源代码
 */
abstract class Shader(
    vertexShader: String,
    fragmentShader: String
) : BaseEntity() {

    abstract var mRendererId: Int

    abstract fun bind()
    abstract fun unbind()


}