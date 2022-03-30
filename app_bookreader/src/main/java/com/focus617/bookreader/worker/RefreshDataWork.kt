/*
 * Copyright 2018, The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 */

package com.focus617.bookreader.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters

class RefreshDataWorker(appContext: Context, params: WorkerParameters):
        CoroutineWorker(appContext, params) {

    companion object {
        const val WORK_NAME = "RefreshDataWorker"
    }

    /**
     * A coroutine-friendly method to do your work.
     */
    override suspend fun doWork(): Result {
//        val database = BookReaderDatabase.getInstance(applicationContext)
//        val repository = BookOldRepository(database)
//        Timber.i("$WORK_NAME start.")
//        return try {
//            repository.refreshBooks()
//            Result.success()
//        } catch (e: HttpException) {
//            Result.retry()
//        }
        return Result.success()
    }
}
