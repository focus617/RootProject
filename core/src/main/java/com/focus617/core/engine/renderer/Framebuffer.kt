package com.focus617.core.engine.renderer

import com.focus617.core.platform.base.BaseEntity

abstract class Framebuffer(
    protected var mWidth: Int = 800,
    protected var mHeight: Int = 1000
): BaseEntity() {

    protected var mShowOnScreen: Boolean = false

    abstract fun bind()
    abstract fun unbind()
    abstract fun getColorAttachmentTexture2DId(): Int
}