package com.focus617.core.engine.renderer

import com.focus617.core.engine.math.Vector2

data class SubTexture2D(val mTextureAtlas: Texture2D) {
    val mTexCoords: Array<Vector2> = arrayOf(
        // 顺序是: 左下, 右下, 左上, 右上
        Vector2(0.0f, 0.0f),
        Vector2(1.0f, 0.0f),
        Vector2(0.0f, 1.0f),
        Vector2(1.0f, 1.0f)
    )

    constructor(textureAtlas: Texture2D, minUV: Vector2, maxUV: Vector2) : this(textureAtlas) {
        mTexCoords[0] = minUV
        mTexCoords[1] = Vector2(maxUV.x, minUV.y)
        mTexCoords[2] = Vector2(minUV.x, maxUV.y)
        mTexCoords[3] = maxUV
    }

    companion object {
        fun createFromCoords(
            textureAtlas: Texture2D,
            coords: Vector2,
            spriteSize: Vector2
        ): SubTexture2D = SubTexture2D(
            textureAtlas,
            Vector2(
                (coords.x * spriteSize.x) / textureAtlas.mWidth,
                (coords.y * spriteSize.y) / textureAtlas.mHeight
            ),
            Vector2(
                ((coords.x + 1) * spriteSize.x) / textureAtlas.mWidth,
                ((coords.y + 1) * spriteSize.y) / textureAtlas.mHeight
            )
        )
    }
}