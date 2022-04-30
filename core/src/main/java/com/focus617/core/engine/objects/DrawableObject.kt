package com.focus617.core.engine.objects

import com.focus617.core.engine.math.Vector2
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.XMatrix
import com.focus617.core.engine.renderer.BufferLayout
import com.focus617.core.engine.renderer.VertexArray
import com.focus617.core.platform.base.BaseEntity

interface IfDrawable{
    abstract fun beforeBuild()
    abstract fun afterBuild()
    abstract fun getVertices(): FloatArray
    abstract fun getLayout(): BufferLayout
    abstract fun getIndices(): ShortArray
}

abstract class DrawableObject : BaseEntity(), IfDrawable {
    val modelMatrix: FloatArray = FloatArray(16)
    lateinit var vertexArray: VertexArray

    init {
        resetTransform()
    }

    fun resetTransform(){
        XMatrix.setIdentityM(modelMatrix, 0)
    }

    open fun onTransform(
        position: Vector3,
        scaleSize: Vector2,
        rotation: Float = 0.0f
    ){
        XMatrix.scaleM(modelMatrix, 0, scaleSize.x, scaleSize.y, 1.0f)
        XMatrix.rotateM(modelMatrix, 0, rotation, 0.0f, 0.0f, 1.0f)
        XMatrix.translateM(modelMatrix, 0, position)
        //LOG.info("ModelMatrix:" + XMatrix.toString(modelMatrix))
    }

    open fun onTransform(
        position: Vector2,
        scaleSize: Vector2,
        rotation: Float = 0.0f
    ) {
        onTransform(Vector3(position.x, position.y, 0.0f), scaleSize, rotation)
    }

}