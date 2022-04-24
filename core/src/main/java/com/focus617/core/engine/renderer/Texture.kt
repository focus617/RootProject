package com.focus617.core.engine.renderer

import com.focus617.core.platform.base.BaseEntity
import java.io.Closeable

/**
 * 纹理类 Texture
 * 1. 储存了纹理的基本属性 [mWidth] [mHeight]
 * 2. 它的构造器需要纹理的图片资源或文件
 */
abstract class Texture : BaseEntity(), Closeable {
    protected abstract var mWidth: Int
    protected abstract var mHeight: Int

    abstract fun bind(slot: Int = 0)
}

abstract class Texture2D : Texture()
