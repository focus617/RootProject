package com.focus617.core.engine.core

import com.focus617.core.platform.event.base.Event

abstract class Layer(
    protected var mDebugName: String = "Layer"
){
    abstract fun onAttach()
    abstract fun onDetach()
    abstract fun onUpdate()
    abstract fun onEvent(event: Event): Boolean
}