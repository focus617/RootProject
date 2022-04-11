package com.focus617.mylib.coroutine.platform

import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.currentCoroutineContext

object MyCoroutineLib {

    @OptIn(ExperimentalStdlibApi::class)
    suspend fun myDispatcher() =
        currentCoroutineContext()[CoroutineDispatcher]

}