package com.focus617.core.engine.renderer

import com.focus617.core.platform.base.BaseEntity
import java.io.Closeable

abstract class Framebuffer(
    protected var mWidth: Int = 1024,
    protected var mHeight: Int = 2048
): BaseEntity(), Closeable {
    // 后续的引擎还会添加RenderPass的概念
    // RenderPass在OpenGL里更像是一个抽象的概念，但在Vulkan里却是一个实实在在存在的类，
    // 它会有一个Framebuffer，和一个target。
    // 像下面的mShowOnScreen为false的Framebuffer，其实就是一个渲染到屏幕上的RenderPass
    protected var mShowOnScreen: Boolean = false

    abstract fun bind()
    abstract fun unbind()
    abstract fun getColorAttachmentTexture2DId(): Int
}