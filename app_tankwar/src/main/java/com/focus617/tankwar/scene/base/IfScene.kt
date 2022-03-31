package com.focus617.tankwar.scene.base

import android.graphics.Bitmap

interface IfScene {
    val rootNode: RootNode
    val bitmapRepository: LinkedHashMap<String, Bitmap>
}