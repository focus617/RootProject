package com.focus617.core.engine.scene_graph.core

import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.renderer.Material
import com.focus617.core.engine.renderer.Renderable
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.scene_graph.components.MeshRenderer
import com.focus617.core.engine.scene_graph.scene.Scene
import com.focus617.core.platform.event.base.Event

open class NodeEntity : ParentEntity(), IfEntity {
    // Scene Graph fields.
    private lateinit var scene: Scene

    var mParent: ParentEntity? = null   // Used for ParentEntity Only
        private set

    // Stores the parent as a node (if the parent is a node) to avoid casting.
    var parentAsNode: NodeEntity? = null

    var mTransform: Transform = Transform(this)
        protected set

    // Rendering fields.
    private var renderableInstance: Renderable? = null

    /**
     * Changes the parent node of this node.(Used for ParentEntity)
     * If set to null, this node will be detached from its parent. The local position, rotation,
     * and scale of this node will remain the same. Therefore, the world position, rotation, and
     * scale of this node may be different after the parent changes.
     *
     * @param parent The new parent that this node will be a child of. If null, this node will be
     *     detached from its parent.
     */
    fun setParent(parent: ParentEntity?) {
        if (parent == this.mParent) {
            return
        }
        mParent = parent
    }

    override fun addChild(child: NodeEntity) {
        super.addChild(child)
        child.parentAsNode = this
        child.markTransformChangedRecursively(Transform.WORLD_DIRTY_FLAGS, child)
    }

    override fun onEvent(event: Event) {
        mComponents.forEach {
            it.onEvent(event)
        }
        mChildren.forEach {
            it.onEvent(event)
        }
    }

    override fun onUpdate(timeStep: TimeStep) {
        // 更新本地和世界变换矩阵
        mTransform.onUpdate()

        mComponents.forEach {
            it.onUpdate(timeStep, mTransform)
        }

        mChildren.forEach {
            it.onUpdate(timeStep)
        }
    }

    override fun onRender(shader: Shader) {
        mComponents.forEach {
            it.onRender(shader, mTransform)
        }

        mChildren.forEach {
            it.onRender(shader)
        }
    }

    fun resetTransform() {
        mTransform.reset()
    }

    /**
     * Sets the position of this node relative to its parent (local-space).
     *
     * @see .getLocalPosition
     * @param position The position to apply.
     */
    open fun setLocalPosition(position: Vector3) {
        mTransform.setLocalPosition(position)
        markTransformChangedRecursively(Transform.WORLD_POSITION_DIRTY, this)
    }

    /**
     * Sets the rotation of this node relative to its parent (local-space).
     *
     * @see #getLocalRotation()
     * @param rulerRotation The rotation to apply.
     */
    fun setLocalRotation(rulerRotation: Vector3) {
        mTransform.setLocalRotation(rulerRotation)
        markTransformChangedRecursively(Transform.WORLD_ROTATION_DIRTY, this)
    }

    /**
     * Sets the scale of this node relative to its parent (local-space).
     *
     * @see #getLocalScale()
     * @param scale The scale to apply.
     */
    fun setLocalScale(scale: Vector3) {
        mTransform.setLocalScale(scale)
        markTransformChangedRecursively(Transform.WORLD_SCALE_DIRTY, this)
    }

    // 当对本地变换矩阵进行更改后，需要通知子节点
    private fun markTransformChangedRecursively(flagsToMark: Int, originatingNode: NodeEntity) {
        val needsRecursion = true

        if (needsRecursion) {
            mChildren.forEach {
                it.mTransform.setWorldFlags(flagsToMark)
                // 递归到Children Node
                it.markTransformChangedRecursively(flagsToMark, originatingNode)
            }
        }
    }

    /**
     * Sets the {@link Renderable} to display for this node.
     *
     * @see Renderable
     * @param renderable Usually a 3D model. If null, this node's current renderable will be removed.
     */
    fun setRenderable(renderable: Renderable) {
        renderableInstance = renderable
        var material: Material? = null

        for ((key, mesh) in renderableInstance!!.mMeshes) {
            if (key == "Default") {
                for ((key, value) in renderableInstance!!.mMaterials) {
                    material = value
                    break
                }
            } else material = renderableInstance!!.mMaterials[key]

            val meshRenderer = MeshRenderer(mesh, material)
            addComponent(meshRenderer)
        }
    }
}