package com.focus617.mylib.coroutine.coroutine_scheduled_task

import com.focus617.mylib.logging.WithLogging
import kotlinx.coroutines.*
import java.util.concurrent.CancellationException

/**
 * 协程实现的定时轮询任务
 */
class CoroutineUpdateTask(
    private val updateApi: ScheduleUpdateApi,
    private val interval: Long = 1000L
): WithLogging() {

    private var job: Job? = null

    fun scheduleUpdate() {
        cancel()
        job = CoroutineScope(Dispatchers.IO).launch {
            while (isActive) {
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
        job?.cancel()
        job = null
    }
}