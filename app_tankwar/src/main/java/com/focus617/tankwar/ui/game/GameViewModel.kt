package com.focus617.tankwar.ui.game

import android.app.Application
import com.focus617.mylib.coroutine.platform.MyCoroutineLib
import com.focus617.mylib.netty.api.IfNorthBoundChannel
import com.focus617.platform.uicontroller.BaseViewModel
import kotlinx.coroutines.*
import timber.log.Timber

class GameViewModel(
    application: Application,
    private val northBound: IfNorthBoundChannel
) : BaseViewModel(application) {

    // Declare Job() and cancel jobs in onCleared().
    private var viewModelJob = Job()

    override fun onCleared() {
        super.onCleared()
        viewModelJob.cancel()
    }

    // Define uiScope for coroutines.
    private val viewModelScope = CoroutineScope(Dispatchers.Main + viewModelJob)


    init {
        updateChannel()
        SayHello()
    }

    private fun updateChannel() {
        viewModelScope.launch {
            Timber.i("Updating channel - running on ${MyCoroutineLib.myDispatcher()}")

            for (data in northBound.inboundChannel) {
                Timber.i("Receive $data")
            }
        }
    }

    private fun SayHello() {
        viewModelScope.launch {
            //启动一个生产者协程
            for (i in 1..10) {   //模拟高速生产
                val data = "Hello Server $i times"
                northBound.outboundChannel.send(data)
                Timber.i("$data sent")
                delay(3000L)
            }
        }
    }
}