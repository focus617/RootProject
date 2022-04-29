package com.focus617.core.engine.objects

import com.focus617.core.engine.math.Vector2
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.XMatrix
import com.focus617.core.engine.renderer.BufferLayout
import com.focus617.core.platform.base.BaseEntity

abstract class DrawableObject : BaseEntity() {
    var transform: FloatArray = FloatArray(16)

    abstract fun getVertices(): FloatArray
    abstract fun getLayout(): BufferLayout
    abstract fun getIndices(): ShortArray

    init {
        resetTransform()
    }

    fun resetTransform(){
        XMatrix.setIdentityM(transform, 0)
    }

    open fun onTransform(
        position: Vector3,
        size: Vector2,
        rotation: Float = 0.0f
    ){
        XMatrix.scaleM(transform, 0, size.x, size.y, 1.0f)
        XMatrix.rotateM(transform, 0, rotation, 0.0f, 0.0f, 1.0f)
        XMatrix.translateM(transform, 0, position)
        //LOG.info("scaleM" + XMatrix.toString(transform))
    }

    open fun onTransform(
        position: Vector2,
        size: Vector2,
        rotation: Float = 0.0f
    ) {
        onTransform(Vector3(position.x, position.y, 0.0f), size, rotation)
    }

}