package com.focus617.mylib.coroutine.scheduledTask

import kotlinx.coroutines.Deferred

interface ScheduleUpdateApi {

    suspend fun scheduleUpdate(): String
}


interface ScheduleUpdateAsyncApi {

    suspend fun scheduleUpdateAsync(): Deferred<String>

}

