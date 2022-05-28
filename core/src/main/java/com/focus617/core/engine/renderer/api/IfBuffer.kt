package com.focus617.core.engine.renderer.api

import com.focus617.core.engine.renderer.vertex.BufferLayout

interface IfBuffer {
    fun bind()
    fun unbind()
}

interface IfBufferLayout{
    fun getLayout(): BufferLayout?
    fun setLayout(layout: BufferLayout)
}

