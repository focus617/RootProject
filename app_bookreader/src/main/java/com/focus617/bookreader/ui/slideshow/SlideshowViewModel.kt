package com.focus617.bookreader.ui.slideshow

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.focus617.mylib.coroutine.platform.MyCoroutineLib
import com.focus617.mylib.netty.api.IfNorthBoundChannel
import com.focus617.platform.uicontroller.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import timber.log.Timber

class SlideshowViewModel(
    application: Application,
    private val northBound: IfNorthBoundChannel
) : BaseViewModel(application) {

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment\n"
    }
    val text: LiveData<String> = _text

    init {
//        produceFakeChannelData()
        updateChannel()
        SayHello()
    }

    private fun updateChannel() {
        viewModelScope.launch {
            Timber.i("Updating channel - running on ${MyCoroutineLib.myDispatcher()}")

            for (data in northBound.inboundChannel) {
                val str = text.value
                _text.value = StringBuilder(str!!).append(data + '\n').toString()
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




