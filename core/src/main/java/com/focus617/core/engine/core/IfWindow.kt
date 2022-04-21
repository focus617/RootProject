package com.focus617.core.engine.core

import com.focus617.core.engine.renderer.IfGraphicsContext
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventHandler
import com.focus617.mylib.logging.ILoggable
import org.slf4j.Logger


interface IfWindow : ILoggable {
    val LOG: Logger

    val mRenderContext: IfGraphicsContext

    fun setVSync(enable: Boolean)
    fun isVSync(): Boolean

    fun getWindowWidth(): Int
    fun getWindowHeight(): Int

    fun onUpdate()

    fun setEventCallbackFn(callback: EventHandler<Event>)
}