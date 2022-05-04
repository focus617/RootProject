package com.focus617.core.engine.objects

import com.focus617.core.engine.math.Vector2
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.XMatrix
import com.focus617.core.engine.renderer.BufferLayout
import com.focus617.core.engine.renderer.RenderCommand
import com.focus617.core.engine.renderer.Shader
import com.focus617.core.engine.renderer.VertexArray
import com.focus617.core.platform.base.BaseEntity

interface IfDrawable{
    abstract fun beforeBuild()
    abstract fun afterBuild()
    abstract fun getVertices(): FloatArray
    abstract fun getLayout(): BufferLayout
    abstract fun getIndices(): ShortArray
    abstract fun submit(shader: Shader)
}

abstract class DrawableObject : BaseEntity(), IfDrawable {
    val modelMatrix: FloatArray = FloatArray(16)
    lateinit var vertexArray: VertexArray
    lateinit var shaderName: String
    lateinit var textureName: String

    init {
        resetTransform()
    }

    companion object {
        const val U_MODEL_MATRIX = "u_ModelMatrix"
    }

    override fun submit(shader: Shader) {
        shader.bind()
        shader.setMat4(U_MODEL_MATRIX, modelMatrix)

        vertexArray.bind()
        RenderCommand.drawIndexed(vertexArray)

        // 下面这两行可以省略，以节约GPU的运行资源；
        // 在下个submit，会bind其它handle，自然会实现unbind
        vertexArray.unbind()
        shader.unbind()
    }

    fun resetTransform(){
        XMatrix.setIdentityM(modelMatrix, 0)
    }

    open fun onTransform3D(
        position: Vector3,
        scaleSize: Vector3,
        rotation: Float = 0.0f
    ){
        XMatrix.scaleM(modelMatrix, 0, scaleSize.x, scaleSize.y, scaleSize.z)
        XMatrix.rotateM(modelMatrix, 0, rotation, 0.0f, 0.0f, 1.0f)
        XMatrix.translateM(modelMatrix, 0, position)
        //LOG.info("ModelMatrix:" + XMatrix.toString(modelMatrix))
    }

    open fun onTransform2D(
        position: Vector3,
        scaleSize: Vector2,
        rotation: Float = 0.0f
    ){
        XMatrix.scaleM(modelMatrix, 0, scaleSize.x, scaleSize.y, 1.0f)
        XMatrix.rotateM(modelMatrix, 0, rotation, 0.0f, 0.0f, 1.0f)
        XMatrix.translateM(modelMatrix, 0, position)
        //LOG.info("ModelMatrix:" + XMatrix.toString(modelMatrix))
    }

    open fun onTransform2D(
        position: Vector2,
        scaleSize: Vector2,
        rotation: Float = 0.0f
    ) {
        onTransform2D(Vector3(position.x, position.y, 0.0f), scaleSize, rotation)
    }

}