package com.focus617.core.engine.core

import com.focus617.core.platform.event.base.Event
import com.focus617.mylib.logging.ILoggable
import org.slf4j.Logger

interface IfWindow : ILoggable {
    abstract val LOG: Logger

    abstract fun setVSync(enable: Boolean)
    abstract fun isVSync(): Boolean

    abstract fun getWindowWidth(): Int
    abstract fun getWindowHeight(): Int

    abstract fun onUpdate()

    abstract fun setEventCallbackFn(callback: (Event) -> Unit)
}