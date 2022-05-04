package com.focus617.core.engine.scene

import com.focus617.core.engine.core.TimeStep
import com.focus617.core.platform.base.BaseEntity
import com.focus617.core.platform.event.base.Event

/**
 * CameraController抽象类
 */
abstract class CameraController : BaseEntity() {

    abstract fun onUpdate(timeStep: TimeStep)

    abstract fun onEvent(event: Event): Boolean

    abstract fun onWindowSizeChange(width: Int, height: Int)

    abstract fun setPosition(x: Float, y: Float, z: Float)
}