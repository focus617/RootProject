package com.focus617.core.engine.scene_graph

import com.focus617.core.platform.base.BaseEntity
import java.io.Closeable

/**
 * Base class for all classes that can contain a set of nodes as children.
 *
 * <p>The classes {@link NodeEntity} and {@link Scene} are both NodeParents.
 * To make a {@link NodeEntity} the child of another {@link NodeEntity} or a {@link Scene},
 * use {@link NodeEntity#setParent(NodeParent)}.
 */
abstract class ParentEntity : BaseEntity(), Closeable {

    protected val mChildren: MutableList<NodeEntity> = mutableListOf()

    protected val mComponents: MutableList<IfComponent> = mutableListOf()

    var mTransform: Transform = Transform()
        protected set

    // release this object and its children/components from memory
    override fun close() {
        mChildren.forEach {
            it.close()
        }
        mComponents.forEach {
            it.close()
        }
        mChildren.clear()
        mComponents.clear()
    }

    fun resetTransform() {
        mTransform.reset()
    }

    /**
     * Adds a node as a child of this Node.
     * If the node already has a parent, it is removed from its old parent.
     * If the node is already a direct child of this Node, no change is made.
     *
     * @param child the node to add as a child
     */
    fun addChild(child: NodeEntity) {
        // Return early if the parent hasn't changed.
        if (child.mParent === this) {
            return
        }
        // If the node already has a parent, it is removed from its old parent.
        val previousParent = child.mParent
        previousParent?.removeChild(child)

        mChildren.add(child)
        child.setParent(this)
    }

    /**
     * Removes a node from the children of this Node.
     * If the node is not a direct child of this Node, no change is made.
     *
     * @param child the node to remove from the children
     */
    fun removeChild(child: NodeEntity) {
        // Return early if this parent doesn't contain the child.

        // Return early if this parent doesn't contain the child.
        if (!mChildren.contains(child)) {
            return
        }

        mChildren.remove(child)
        child.setParent(null)
    }

    fun addComponent(component: IfComponent) {
        mComponents.add(component)
        component.mParent = this
    }

    fun removeComponent(component: IfComponent) {
        mComponents.remove(component)
    }


}