package com.focus617.app_xgame.engine

import com.focus617.core.engine.core.IfWindow
import com.focus617.core.engine.core.WindowProps
import com.focus617.core.platform.event.base.Event

class AndroidWindow private constructor(): IfWindow {
    override val LOG = logger()

    override var width: Int = 0
    override var height: Int = 0
    override var isVSync: Boolean = false

    override fun onUpdate() {
        LOG.info("onUpdate")
    }

    override fun setEventCallbackFn(callback: (Event) -> Unit) {
        LOG.info("callback func is set")
    }

    companion object{
        private val window = AndroidWindow()

        fun createWindow(props: WindowProps = WindowProps()): AndroidWindow {
            with(window){
                width = props.width
                height = props.height
            }
            return window
        }
    }
}