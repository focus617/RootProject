package com.focus617.core.engine.scene_graph

import com.focus617.core.engine.core.TimeStep
import com.focus617.core.engine.renderer.shader.Shader
import com.focus617.core.platform.event.base.Event

open class NodeEntity : ParentEntity(), IfEntity {
    var mParent: ParentEntity? = null

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