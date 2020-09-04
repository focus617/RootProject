/*
 * Copyright (C) 2019 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.example.pomodoro2.data

import com.example.pomodoro2.domain.model.Task
import com.example.pomodoro2.platform.data.IDbLikeDataSource
import com.example.pomodoro2.platform.functional.Result
import com.example.pomodoro2.platform.functional.Result.Success
import com.example.pomodoro2.platform.functional.Result.Error
import java.util.LinkedHashMap

/**
 * Implementation of a remote data source with static access to the data for easy testing.
 */
object FakeTasksDataSource  : IDbLikeDataSource<Task> {

    var TASKS_SERVICE_DATA: LinkedHashMap<String, Task> = LinkedHashMap()


    override suspend fun createOrUpdateTask(task: Task) {
        TASKS_SERVICE_DATA[task.id] = task
    }

    override suspend fun retrieveTask(taskId: String): Result<Task> {
        TASKS_SERVICE_DATA[taskId]?.let {
            return Success(it)
        }
        return Error(Exception("Could not find task"))
    }

    override suspend fun retrieveTasks(): Result<List<Task>> {
        return Success(TASKS_SERVICE_DATA.values.toList())
    }

/*    override suspend fun updateTask(t: Task) {
        TODO("Not yet implemented")
    }*/

    override suspend fun deleteTask(task: Task) {
        TASKS_SERVICE_DATA.remove(task.id)
    }

    override suspend fun deleteAllTasks() {
        TASKS_SERVICE_DATA.clear()
    }

    override suspend fun initializeTutorialTasks() : Result<List<Task>>{
        TODO("Not yet implemented")
    }

    override suspend fun completeTask(task: Task) {
        val completedTask = Task(
            task.id, task.title, task.description, true,
            task.imageId, task.priority, task.createTime
        )
        TASKS_SERVICE_DATA[task.id] = completedTask
    }

    override suspend fun clearCompletedTasks() {
        TASKS_SERVICE_DATA = TASKS_SERVICE_DATA.filterValues {
            it.isActive
        } as LinkedHashMap<String, Task>
    }

    override suspend fun activateTask(task: Task) {
        val activeTask = Task(
            task.id, task.title, task.description, false,
            task.imageId, task.priority, task.createTime
        )
        TASKS_SERVICE_DATA[task.id] = activeTask
    }


}
