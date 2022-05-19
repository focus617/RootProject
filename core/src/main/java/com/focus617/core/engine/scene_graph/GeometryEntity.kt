package com.focus617.core.engine.scene_graph

import com.focus617.core.engine.math.*

// 包含了ModelMatrix栈， 包围球，碰撞检测， 被选择， Shader查询Key（ShaderName）
abstract class GeometryEntity : NodeEntity() {
    private var modelMatrixInStack = mTransform

    protected val boundingSphere = Sphere(Point3D(0f, 0f, 0f), 0.5f)

    // shaderName should be initialized by each concrete drawable object itself
    lateinit var shaderName: String

    var isSelected = false

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
}