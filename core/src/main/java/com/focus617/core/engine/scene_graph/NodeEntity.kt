package com.focus617.core.engine.scene_graph

import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.platform.base.BaseEntity
import com.focus617.core.platform.event.base.Event
import java.io.Closeable

open class NodeEntity : BaseEntity(), IfEntity, Closeable {
    var mParent: NodeEntity? = null
        private set

    private val mChildren: MutableList<NodeEntity> = mutableListOf()

    private val mComponents: MutableList<IfComponent> = mutableListOf()

    var mTransform: Transform = Transform()
         protected set

    // release this object and its children/components from memory
    override fun close() {
        mChildren.forEach{
            it.close()
        }
        mComponents.forEach{
            it.close()
        }
        mChildren.clear()
        mComponents.clear()
    }

    fun resetTransform() {
        mTransform.reset()
    }

    fun addChild(child: NodeEntity) {
        mChildren.add(child)
        child.mParent = this
    }

    fun removeChild(child: NodeEntity) {
        mChildren.remove(child)
        child.mParent = null
    }

    fun addComponent(component: IfComponent) {
        mComponents.add(component)
        component.mParent = this
    }

    fun removeComponent(component: IfComponent) {
        mComponents.remove(component)
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