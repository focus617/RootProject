package com.focus617.core.engine.objects

import com.focus617.core.engine.math.*
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.scene_graph.GameEntity
import com.focus617.core.engine.scene_graph.IfMeshable
import com.focus617.core.engine.scene_graph.renderer.Mesh

abstract class DrawableObject : GameEntity(), IfMeshable {
    private var modelMatrixInStack = getTransform()

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
        shader.setMat4(U_MODEL_MATRIX, mTransform.getLocalModelMatrix())

        mesh.draw()

        shader.unbind()
    }

    fun resetTransform() {
       mTransform.reset()
    }

    fun scale(scaleSize: Vector3) {
        mTransform.setLocalScale(scaleSize)
        boundingSphere.radius *= (scaleSize.x + scaleSize.y + scaleSize.z) / 3
    }

    open fun onTransform3D(
        position: Vector3,
        scaleSize: Vector3,
        rotationZInDegree: Float = 0.0f
    ) {
        mTransform.setLocalPosition(position)
        mTransform.setLocalScale(scaleSize)
        mTransform.setLocalRotation(Vector3(0.0f, 0.0f, rotationZInDegree))

        boundingSphere.center += position
        boundingSphere.radius *= (scaleSize.x + scaleSize.y + scaleSize.z) / 3
    }

    open fun onTransform2D(
        position: Vector2,
        scaleSize: Vector2,
        rotationZInDegree: Float = 0.0f
    ) {
        mTransform.setLocalPosition(Vector3(position.x, position.y, 0.0f))
        mTransform.setLocalScale(Vector3(scaleSize.x, scaleSize.y, 1f))
        mTransform.setLocalRotation(Vector3(0.0f, 0.0f, rotationZInDegree))

        boundingSphere.center += Point3D(position.x, position.y, 0f)
        boundingSphere.radius *= (scaleSize.x + scaleSize.y) / 2
    }

    fun push() {
        modelMatrixInStack = mTransform
    }

    fun pop() {
        mTransform = modelMatrixInStack
    }

    open fun intersects(ray: Ray) {
        isSelected = !isSelected
    }

    companion object {
        const val U_MODEL_MATRIX = "u_ModelMatrix"
    }


}