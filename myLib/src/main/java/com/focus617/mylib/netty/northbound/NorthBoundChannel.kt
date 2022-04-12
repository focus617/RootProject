package com.focus617.mylib.netty.northbound

import com.focus617.mylib.coroutine.platform.MyCoroutineLib
import com.focus617.mylib.logging.ILoggable
import com.focus617.mylib.netty.api.IfNorthBoundChannel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import javax.inject.Inject


class NorthBoundChannel @Inject constructor() : IfNorthBoundChannel, ILoggable {
    val LOG = logger()

    override val inboundChannel = Channel<String>(
        capacity = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        onUndeliveredElement = { data ->
            LOG.info("$data dropped")
        }
    )
    override val outboundChannel = Channel<String>(
        capacity = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        onUndeliveredElement =
        { data ->
            LOG.info("$data dropped")
        }
    )

    //启动一个Channel生产者协程
    suspend fun produceChannelData() {
        withContext(Dispatchers.IO) {
            (1..20).forEach {   //模拟高速生产
                val data = "DATA$it"
                inboundChannel.send(data)
                LOG.info("Producer emit: $data")
                delay(1000)      //模拟生产数据耗时
            }
        }
        LOG.info("Producer finally.")
    }


    //启动一个Flow生产者协程
    var socketflow: Flow<String> = flow {
        LOG.info("Flow Producer run on ${MyCoroutineLib.myDispatcher()}")

        (1..10).forEach {
            val data = "DATA$it"
            emit(data)
            delay(5000)     //模拟生产
        }
    }
        .onEach { LOG.info("Producer emit: $it") }
        .onCompletion {
            LOG.info("Producer finally.")
        }
}