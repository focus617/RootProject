package com.focus617.bookreader.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.focus617.bookreader.framework.datasource.NetworkFlowDataSource
import com.focus617.mylib.coroutine.platform.MyCoroutineLib
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.launch
import timber.log.Timber


interface IfNetworkFlowDataSource {
    val channel: Channel<String>
    suspend fun produceChannelData()
}

class SlideshowViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment\n"
    }
    val text: LiveData<String> = _text

    val dataSource: IfNetworkFlowDataSource = NetworkFlowDataSource()

    init {
        produceFakeChannelData()
        updateChannel()
    }

    private fun produceFakeChannelData() {
        viewModelScope.launch {
            dataSource.produceChannelData()
        }
    }

    private fun updateChannel() {
        viewModelScope.launch {
            Timber.i("Updating channel - running on ${MyCoroutineLib.myDispatcher()}")

            for (data in dataSource.channel) {
                val str = text.value
                _text.value = StringBuilder(str!!).append(data + '\n').toString()
            }
        }
    }

}




