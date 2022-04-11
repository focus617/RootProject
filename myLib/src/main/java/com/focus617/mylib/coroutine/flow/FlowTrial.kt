package com.focus617.mylib.coroutine.flow

import com.focus617.mylib.coroutine.platform.MyCoroutineLib.myDispatcher
import com.focus617.mylib.logging.WithLogging
import kotlinx.coroutines.*
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.*

class FlowTrial : WithLogging() {

    companion object : WithLogging() {
        @JvmStatic
        @Throws(Exception::class)
        fun main(vararg args: String) {
            val testObject = FlowTrial()

            runBlocking {
                // 处理具体的逻辑
                testObject.testChannel()

                testObject.testFlow()
            }
        }
    }



    suspend fun testChannel() {
        LOG.info("Channel Testing started")
        val channel = Channel<String>(
            capacity = 2,
            onBufferOverflow = BufferOverflow.DROP_OLDEST,
            onUndeliveredElement = { data ->
                LOG.info("$data dropped")
            }
        )

        coroutineScope {
            //启动一个生产者协程
            launch {
                for (i in 1..3) {   //模拟高速生产
                    val data = "DATA$i"
                    LOG.info("$data produced")
                    channel.send(data)
                    LOG.info("$data sent")
                }
            }

            delay(500)      //模拟生产数据耗时

            //启动一个消费者协程
            launch {
                for (data in channel) {
                    LOG.info("$data received")
                    delay(200)    //模拟消费数据耗时
                    LOG.info("$data consumed")
                    cancel()
                    LOG.info("consumer cancelled")
                }
            }
        }

    }

    suspend fun testFlow() {
        var flowUnderTesting: Flow<String>

        LOG.info("Flow Testing started")

        coroutineScope {
            //启动一个生产者协程
            flowUnderTesting = flow {
                LOG.info("Producer run on ${myDispatcher()}")

                (1..10).forEach {
                    val data = "DATA$it"
                    emit(data)
                    delay(500)     //模拟生产
                }
            }
                .onEach { LOG.info("Producer emit $it") }
                .onCompletion {
                    LOG.info("Producer finally.")
                }

            //启动一个消费者协程
            launch {
                delay(200)    //模拟消费数据耗时
                LOG.info("Consumer collect")

                flowUnderTesting
                    .flowOn(Dispatchers.IO)
                    .collect { data ->
                        LOG.info("$data consumed")
                        LOG.info("Consumer run on ${myDispatcher()}")
                    }

                cancel()
                LOG.info("consumer cancelled")
            }
        }

    }
}
