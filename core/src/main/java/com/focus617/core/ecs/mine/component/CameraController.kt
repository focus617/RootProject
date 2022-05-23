package com.focus617.core.ecs.mine.component

import com.focus617.core.engine.math.Mat4

data class CameraController(
    var mWidth: Int = 0,            // Viewport size
    var mHeight: Int = 0,
    var mZoomLevel: Float = 0.5f
)

data class CameraProjectionMatrix(
    val projectionMatrix: Mat4 = Mat4()
)
