package com.focus617.core.engine.renderer.framebuffer

import com.focus617.core.platform.base.BaseEntity
import com.focus617.mylib.logging.ILoggable

enum class FrameBufferTextureFormat : ILoggable {
    None,
    RGBA8,              // Color
    DEPTH24STENCIL8;    // Depth/stencil

    val LOG = logger()
}

data class FrameBufferTextureSpecification(
    val textureFormat: FrameBufferTextureFormat
): BaseEntity()

class FrameBufferAttachmentSpecification(): BaseEntity()  {
    val attachments = mutableListOf<FrameBufferTextureSpecification>()

    constructor(initializerList: List<FrameBufferTextureSpecification>): this(){
        initializerList.forEach(){
            attachments.add(it)
        }
    }
}

class FrameBufferSpecification: BaseEntity() {
    var mWidth: Int = 0
    var mHeight: Int = 0

    lateinit var attachment: FrameBufferAttachmentSpecification
    val samples: Int = 1
    var swapChainTarget: Boolean = false
}