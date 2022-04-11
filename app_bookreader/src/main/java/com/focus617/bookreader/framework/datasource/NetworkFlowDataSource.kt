package com.focus617.bookreader.framework.datasource

import com.focus617.bookreader.ui.slideshow.IfNetworkFlowDataSource
import com.focus617.mylib.coroutine.platform.MyCoroutineLib
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.withContext
import timber.log.Timber


class NetworkFlowDataSource : IfNetworkFlowDataSource {

    override val channel = Channel<String>(
        capacity = 10,
        onBufferOverflow = BufferOverflow.DROP_OLDEST,
        onUndeliveredElement = { data ->
            Timber.i("$data dropped")
        }
    )

    //启动一个Channel生产者协程
    override suspend fun produceChannelData() {
        withContext(Dispatchers.IO) {
            (1..20).forEach {   //模拟高速生产
                val data = "DATA$it"
                channel.send(data)
                Timber.i("Producer emit: $data")
                delay(1000)      //模拟生产数据耗时
            }
        }
        Timber.i("Producer finally.")
    }


    //启动一个Flow生产者协程
    var socketflow: Flow<String> = flow {
        Timber.i("Flow Producer run on ${MyCoroutineLib.myDispatcher()}")

        (1..10).forEach {
            val data = "DATA$it"
            emit(data)
            delay(5000)     //模拟生产
        }
    }
        .onEach { Timber.i("Producer emit: $it") }
        .onCompletion {
            Timber.i("Producer finally.")
        }
}