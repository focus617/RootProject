package com.focus617.bookreader.ui.slideshow

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.focus617.mylib.coroutine.platform.MyCoroutineLib
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber

class SlideshowViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is slideshow Fragment"
    }
    val text: LiveData<String> = _text

    val dataSource = FakeFlowDataSource()

    init {
        viewModelScope.launch {
            dataSource.flowUnderTesting.collect { value ->
                val str = text.value
                _text.value = StringBuilder(str!!).append(value).toString()
            }
        }
    }
}

class FakeFlowDataSource {
    //启动一个生产者协程
    var flowUnderTesting: Flow<String> = flow {
        Timber.i("Flow Producer run on ${MyCoroutineLib.myDispatcher()}")

        (1..10).forEach {
            val data = "\nDATA$it"
            emit(data)
            delay(5000)     //模拟生产
        }
    }
        .onEach { Timber.i("Producer emit $it") }
        .onCompletion {
            Timber.i("Producer finally.")
        }
}
