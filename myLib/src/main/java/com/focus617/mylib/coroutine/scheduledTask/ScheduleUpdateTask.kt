package com.focus617.mylib.coroutine.scheduledTask

import com.focus617.mylib.coroutine.platform.MyCoroutineLib.myDispatcher
import com.focus617.mylib.logging.WithLogging
import kotlinx.coroutines.*
import java.util.concurrent.CancellationException

class ScheduledCoroutine : WithLogging(), ScheduleUpdateApi {

    override suspend fun scheduleUpdate(): String {
        delay(100L)
        LOG.info("Producer run on ${myDispatcher()}")
        return "Scheduled Updating finish."
    }
}

/**
 * 协程实现的定时轮询任务
 */
class ScheduleUpdateTask(
    private val updateApi: ScheduleUpdateApi,
    private val interval: Long = 1000L
) : WithLogging() {

    private var job: Job? = null

    fun start() {
        cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
            LOG.info("Schedule Updating Coroutine is launched")
            LOG.info("Producer run on ${myDispatcher()}")

            while (isActive) {
                LOG.info("Schedule Updating Coroutine running")
                try {
                    updateApi.scheduleUpdate().let(::println)
                } catch (e: Exception) {
                    e.printStackTrace()
                    if (e is CancellationException) throw e
                }
                delay(interval)
            }
        }
        job!!.invokeOnCompletion {
            LOG.info("Schedule Updating Coroutine completed")
        }

    }

    fun cancel() {
        job?.cancel().also { job = null }
    }

    class Client {
        companion object : WithLogging() {
            @JvmStatic
            fun main(vararg args: String) {

                // 测试动态代理
                val invoker = ScheduleUpdateTask(ScheduledCoroutine())

                LOG.info("Client requests execute()")
                invoker.start()
                // 主线程Block，观察协程的工作
                Thread.sleep(3000L)
                invoker.cancel()
            }
        }
    }
}