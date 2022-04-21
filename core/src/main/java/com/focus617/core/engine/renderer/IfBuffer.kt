package com.focus617.core.engine.renderer

interface IfBuffer {
    fun bind()
    fun unbind()
}

interface IfBufferLayout{
    fun getLayout(): BufferLayout?
    fun setLayout(layout: BufferLayout)
}

