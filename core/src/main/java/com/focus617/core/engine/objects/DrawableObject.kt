package com.focus617.core.engine.objects

import com.focus617.core.engine.math.Mat4
import com.focus617.core.engine.math.Vector2
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.renderer.BufferLayout
import com.focus617.core.engine.renderer.RenderCommand
import com.focus617.core.engine.renderer.Shader
import com.focus617.core.engine.renderer.VertexArray
import com.focus617.core.platform.base.BaseEntity

interface IfDrawable {
    abstract fun beforeBuild()
    abstract fun afterBuild()
    abstract fun getVertices(): FloatArray
    abstract fun getLayout(): BufferLayout
    abstract fun getIndices(): ShortArray
    abstract fun submit(shader: Shader)
}

abstract class DrawableObject : BaseEntity(), IfDrawable {
    //val modelMatrix: FloatArray = FloatArray(16)
    val modelMatrix = Mat4()

    // vertexArray is initialized by Scene via calling XGLVertexArray.buildVertexArray
    lateinit var vertexArray: VertexArray

    // shaderName should be initialized by each concrete drawable object itself
    lateinit var shaderName: String

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

    fun resetTransform() {
        modelMatrix.setIdentity()
    }

    open fun onTransform3D(
        position: Vector3,
        scaleSize: Vector3,
        rotation: Float = 0.0f
    ) {
        modelMatrix.transform3D(position, scaleSize, rotation, 0.0f, 0.0f, 1.0f)
        //LOG.info("ModelMatrix:" + XMatrix.toString(modelMatrix))
    }

    open fun onTransform2D(
        position: Vector3,
        scaleSize: Vector2,
        rotation: Float = 0.0f
    ) {
        modelMatrix.transform2D(position, scaleSize, rotation)
        //LOG.info("ModelMatrix:" + XMatrix.toString(modelMatrix))
    }

    open fun onTransform2D(
        position: Vector2,
        scaleSize: Vector2,
        rotation: Float = 0.0f
    ) {
        modelMatrix.transform2D(Vector3(position.x, position.y, 0.0f), scaleSize, rotation)
    }

}