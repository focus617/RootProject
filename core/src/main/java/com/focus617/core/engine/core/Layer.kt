package com.focus617.core.engine.core

import com.focus617.core.engine.scene_graph.DrawableEntity
import com.focus617.core.platform.base.BaseEntity
import com.focus617.core.platform.event.base.Event
import java.io.Closeable

abstract class Layer(
    protected var mDebugName: String = "Layer"
) : BaseEntity(), Closeable {

    val gameObjectList = mutableListOf<DrawableEntity>()

    abstract fun onAttach()
    abstract fun onDetach()

    // 负责更新 本Layer 负责管理的Game Objects
    abstract fun onUpdate(timeStep: TimeStep)

    abstract fun beforeDrawFrame()
    abstract fun afterDrawFrame()

    // 负责接收事件，并完成对应的转发
    abstract fun onEvent(event: Event): Boolean

    // 需要在EGL环境下进行初始化的资源
    abstract fun initOpenGlResource()
}