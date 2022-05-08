package com.focus617.core.engine.renderer

import com.focus617.core.engine.math.Vector2

data class SubTexture2D(val mTextureAtlas: Texture2D) {
    val mTexCoords: Array<Vector2> = arrayOf(
        // Quad的顶点顺序是: 0左下, 1右下, 2右上, 3左上
        Vector2(0.0f, 0.0f),
        Vector2(1.0f, 0.0f),
        Vector2(1.0f, 1.0f),
        Vector2(0.0f, 1.0f)
    )

    constructor(textureAtlas: Texture2D, minUV: Vector2, maxUV: Vector2) : this(textureAtlas) {
        // Quad的顶点顺序是: 0左下, 1右下, 2右上, 3左上
        // 但由于Texture的Y轴方向是向下的，因此Y轴对应的纹理坐标是上小上大
        mTexCoords[0] = Vector2(minUV.x, maxUV.y)   //左下
        mTexCoords[1] = maxUV                       //右下
        mTexCoords[2] = Vector2(maxUV.x, minUV.y)   //右上
        mTexCoords[3] = minUV                       //左上
    }

    companion object {
        fun createFromCoords(
            textureAtlas: Texture2D,
            coords: Vector2,
            spriteSize: Vector2,
            cellSize: Vector2 = Vector2(1f, 1f)
        ): SubTexture2D = SubTexture2D(
            textureAtlas,
            Vector2(
                (coords.x * spriteSize.x) / textureAtlas.mWidth,
                (coords.y * spriteSize.y) / textureAtlas.mHeight
            ),
            Vector2(
                ((coords.x + cellSize.x) * spriteSize.x ) / textureAtlas.mWidth,
                ((coords.y + cellSize.y) * spriteSize.y ) / textureAtlas.mHeight
            )
        )
    }
}