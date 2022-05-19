package com.focus617.core.engine.scene_graph

import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.engine.scene_graph.scene.Scene
import com.focus617.core.platform.event.base.Event

open class NodeEntity : ParentEntity(), IfEntity {
    // Scene Graph fields.
    private lateinit var scene: Scene

    var mParent: ParentEntity? = null
        private set

    /**
     * Changes the parent node of this node.
     * If set to null, this node will be detached from its parent. The local position, rotation,
     * and scale of this node will remain the same. Therefore, the world position, rotation, and
     * scale of this node may be different after the parent changes.
     *
     * @param parent The new parent that this node will be a child of. If null, this node will be
     *     detached from its parent.
     */
    fun setParent(parent: ParentEntity?)
    {
        if (parent == this.mParent) {
            return
        }
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
        mTransform.mGlobalModelMatrix =
            if (mParent == null) {
                // Root node , world transform is local transform !
                mTransform.getLocalModelMatrix()
            } else {
                // This node has a parent ...
                mParent!!.mTransform.mGlobalModelMatrix * mTransform.getLocalModelMatrix()
            }

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
}