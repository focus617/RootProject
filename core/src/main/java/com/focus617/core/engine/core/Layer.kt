package com.focus617.core.engine.core

import com.focus617.core.platform.base.BaseEntity
import com.focus617.core.platform.event.base.Event

abstract class Layer(
    protected var mDebugName: String = "Layer"
) : BaseEntity() {
    abstract fun onAttach()
    abstract fun onDetach()

    // 负责更新Layer
    abstract fun onUpdate()

    // 负责接收事件，并完成对应的转发
    abstract fun onEvent(event: Event): Boolean
}