package com.focus617.mylib.coroutine.scheduledTask

import com.focus617.mylib.coroutine.platform.MyCoroutineLib.myDispatcher
import com.focus617.mylib.logging.WithLogging
import kotlinx.coroutines.*
import java.util.concurrent.CancellationException


/**
 * 协程实现的定时轮询任务
 */
class ScheduleUpdateTask(
    private val updateApi: ScheduleUpdateApi?,
    private val updateAsyncApi: ScheduleUpdateAsyncApi?,
    private val interval: Long = 1000L
) : WithLogging() {

    class Client {
        companion object : WithLogging() {
            @JvmStatic
            fun main(vararg args: String) {

                // 测试动态代理
                val invoker = ScheduleUpdateTask(ScheduledCoroutine(), ScheduledCoroutineAsync())

                LOG.info("Client requests execute()")
                invoker.start()

                // 主线程执行3秒之后停止协程，观察协程的工作
//                Thread.sleep(3000L)
//                invoker.cancel()

                // 连续执行
                runBlocking {
                    invoker.join()
                }
            }
        }
    }

    private var job: Job? = null

    fun start() {
        cancel()
        job = CoroutineScope(Dispatchers.Default).launch {
            LOG.info("Schedule Updating Coroutine is launched")

            while (isActive) {
                LOG.info("Schedule Updating Coroutine running on ${myDispatcher()}")
                try {
                    updateApi?.scheduleUpdate().also(::println)
                    updateAsyncApi?.scheduleUpdateAsync()?.await().also(::println)
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

    suspend fun join() {
        job?.join().also {
            LOG.info("Schedule Updating Coroutine is joined}")
        }
    }

}


class ScheduledCoroutine : WithLogging(), ScheduleUpdateApi {

    override suspend fun scheduleUpdate(): String {
        return withContext(Dispatchers.IO) {
            LOG.info("Producer run on ${myDispatcher()}")
            // 模拟耗时
            delay(200)
            // 返回一个值
            "Scheduled 1 Updating finish."
        }
    }
}

class ScheduledCoroutineAsync : WithLogging(), ScheduleUpdateAsyncApi {

    override suspend fun scheduleUpdateAsync(): Deferred<String> {

        // 开启一个IO模式的线程 并返回一个Deferred，用来获取返回值
        return withContext(Dispatchers.IO) {
            LOG.info("Producer run on ${myDispatcher()}")

            async(Dispatchers.IO) {
                // 模拟耗时
                delay(200)
                // 返回一个值
                "Scheduled 2 Updating finish."
            }
        }
    }
}