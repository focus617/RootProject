package com.focus617.core.engine.core

import com.focus617.core.platform.event.base.Event
import com.focus617.mylib.logging.ILoggable
import org.slf4j.Logger

interface IfWindow : ILoggable {
    abstract val LOG: Logger

    abstract var width: Int
    abstract var height: Int
    abstract var isVSync: Boolean

    abstract fun onUpdate()

    abstract fun setEventCallbackFn(callback: (Event) -> Unit)
}