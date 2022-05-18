package com.focus617.core.engine.scene_graph

import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.platform.base.BaseEntity
import com.focus617.core.platform.event.base.Event

open class GameEntity : BaseEntity(), IfEntity {
    private var mParent: GameEntity? = null
    private val mChildren: MutableList<GameEntity> = mutableListOf()

    private val mComponents: MutableList<IfComponent> = mutableListOf()

    protected var mTransform: Transform = Transform()

    fun getParent() = mParent
    fun getTransform() = mTransform

    fun resetTransform() {
        mTransform.reset()
    }

    fun addChild(child: GameEntity) {
        mChildren.add(child)
        child.mParent = this
    }

    fun removeChild(child: GameEntity) {
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