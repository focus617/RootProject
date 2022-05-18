package com.focus617.core.engine.objects

import com.focus617.core.engine.math.*
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.scene_graph.GameEntity
import com.focus617.core.engine.scene_graph.IfMeshable
import com.focus617.core.engine.scene_graph.renderer.Mesh

abstract class DrawableObject : GameEntity(), IfMeshable {
    val modelMatrix = Mat4()
    private val modelMatrixInStack = Mat4()

    protected val boundingSphere = Sphere(Point3D(0f, 0f, 0f), 0.5f)

    // mesh is initialized via calling XGLVertexArray.buildVertexArray
    lateinit var mesh: Mesh

    // shaderName should be initialized by each concrete drawable object itself
    lateinit var shaderName: String

    var isSelected = false

    init {
        resetTransform()
    }

    override fun submit(shader: Shader) {
        shader.bind()
        shader.setMat4(U_MODEL_MATRIX, modelMatrix)

        mesh.draw()

        shader.unbind()
    }

    fun resetTransform() {
        modelMatrix.setIdentity()
    }

    fun scale(scaleSize: Vector3) {
        modelMatrix.scale(scaleSize)
        boundingSphere.radius *= (scaleSize.x + scaleSize.y + scaleSize.z) / 3
    }

    open fun onTransform3D(
        position: Vector3,
        scaleSize: Vector3,
        rotation: Float = 0.0f
    ) {
        modelMatrix.transform3D(position, scaleSize, rotation, 0.0f, 0.0f, 1.0f)
        //LOG.info("ModelMatrix:" + XMatrix.toString(modelMatrix))

        boundingSphere.center += position
        boundingSphere.radius *= (scaleSize.x + scaleSize.y + scaleSize.z) / 3
    }

    open fun onTransform2D(
        position: Vector2,
        scaleSize: Vector2,
        rotation: Float = 0.0f
    ) {
        modelMatrix.transform2D(Vector3(position.x, position.y, 0.0f), scaleSize, rotation)

        boundingSphere.center += Point3D(position.x, position.y, 0f)
        boundingSphere.radius *= (scaleSize.x + scaleSize.y) / 2
    }

    fun push() {
        modelMatrixInStack.setValue(modelMatrix)
    }

    fun pop() {
        modelMatrix.setValue(modelMatrixInStack)
    }

    open fun intersects(ray: Ray) {
        isSelected = !isSelected
    }

    companion object {
        const val U_MODEL_MATRIX = "u_ModelMatrix"
    }


}