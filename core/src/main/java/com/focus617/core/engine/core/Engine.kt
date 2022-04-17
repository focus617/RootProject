package com.focus617.core.engine.core

import com.focus617.core.platform.base.BaseEntity
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventType
import com.focus617.core.platform.event.base.LayerEventDispatcher
import com.focus617.mylib.helper.DateHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class Engine(window: IfWindow) : BaseEntity(), Runnable {

    // Game线程
    private var threadCore: Thread? = null
    private var isRunning: Boolean = true
    private val mWindow: IfWindow = window
    private val eventDispatcher = LayerEventDispatcher()

    init {
        registerEventHandlers()
        window.setEventCallbackFn { event: Event -> onEvent(event) }

        threadCore = Thread(this)
        threadCore!!.start()
    }

    private fun registerEventHandlers() {
        eventDispatcher.register(EventType.All) { event ->
            LOG.info("${event.name} from ${event.source} received")
            LOG.info("It's type is ${event.eventType}")
            LOG.info("It's was submit at ${DateHelper.timeStampAsStr(event.timestamp)}")
            event.handleFinished()
            false
        }
    }

    /**
     * 销毁时, 停止线程的运行，防止内存泄漏
     */
    fun onDestroy() {
        threadCore?.interrupt()
        threadCore = null
    }

    private fun onEvent(event: Event) {
        CoroutineScope(Dispatchers.Default).launch {
            if (!eventDispatcher.dispatch(event)) {
                LOG.info("No event handler for $event")
            }
        }
    }

    override fun run() {
        while (isRunning) {
            mWindow.onUpdate()
            //通过线程休眠以控制刷新速度
            try {
                Thread.sleep(SLEEP_INTERVAL)
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
        }
    }

    companion object {
        const val SLEEP_INTERVAL = 1000L

    }
}