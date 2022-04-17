package com.focus617.core.engine.core

import com.focus617.core.platform.base.BaseEntity
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventType
import com.focus617.core.platform.event.base.LayerEventDispatcher
import com.focus617.core.platform.event.screenTouchEvents.TouchMovedEvent
import com.focus617.mylib.helper.DateHelper
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

open class Engine(window: IfWindow) : BaseEntity(), Runnable {

    // Game线程
    private var threadCore: Thread? = null
    private var isRunning: Boolean = true
    private val mWindow: IfWindow = window
    private val mLayerStack: LayerStack = LayerStack()

    private val eventDispatcher = LayerEventDispatcher()

    init {
        registerEventHandlers()
        window.setEventCallbackFn {
            onEvent(it)
        }
        threadCore = Thread(this)
        threadCore!!.start()
    }

    private fun registerEventHandlers() {
        eventDispatcher.register(EventType.TouchMoved) { event ->
            val e: TouchMovedEvent = event as TouchMovedEvent
            LOG.info("${e.name} from ${e.source} received")
            LOG.info("It's type is ${e.eventType}")
            LOG.info("It's was submit at ${DateHelper.timeStampAsStr(e.timestamp)}")
            LOG.info("Current position is (${e.x}, ${e.y})")
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

    // 如果事件可以被本地处理，则返回true，否则false
    private fun onEvent(event: Event): Boolean {
        var result: Boolean = false
        CoroutineScope(Dispatchers.Default).launch {
            result = eventDispatcher.dispatch(event)
            if (!result) {
                LOG.info("No event handler for $event")
            }
        }

        return result
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

    fun pushLayer(layer: Layer) {
        mLayerStack.PushLayer(layer)
        layer.onAttach()
    }

    fun pushOverLayer(layer: Layer) {
        mLayerStack.PushLayer(layer)
        layer.onAttach()
    }

    companion object {
        const val SLEEP_INTERVAL = 2000L

    }
}