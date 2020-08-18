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

import androidx.annotation.VisibleForTesting
import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.platform.functional.Result
import com.example.pomodoro2.platform.functional.Result.Success
import com.example.pomodoro2.platform.functional.Result.Error
import com.example.pomodoro2.platform.data.IRepository
import java.util.LinkedHashMap

/**
 * Implementation of a fake repository with static access to the data for easy testing on UseCases.
 */
class FakeRepository : IRepository<Task> {

    var tasksServiceData: LinkedHashMap<String, Task> = LinkedHashMap()

    private var shouldReturnError = false

    fun setReturnError(value: Boolean) {
        shouldReturnError = value
    }

    @VisibleForTesting
    fun addTasks(vararg tasks: Task) {
        for (task in tasks) {
            tasksServiceData[task.id.toString()] = task
        }
    }


    override suspend fun ofId(id: Long, forceUpdate: Boolean): Result<Task> {
        if (shouldReturnError) {
            return Error(Exception("Test exception"))
        }
        tasksServiceData[id.toString()]?.let {
            return Success(it)
        }
        return Error(Exception("Could not find task"))
    }

    override suspend fun add(task: Task) {
        tasksServiceData[task.id.toString()] = task
    }

    override suspend fun remove(task: Task) {
        tasksServiceData.remove(task.id.toString())
    }

    override suspend fun querySpecification(forceUpdate: Boolean): Result<List<Task>> {
        if (shouldReturnError) {
            return Error(Exception("Test exception"))
        }
        return Success(tasksServiceData.values.toList())
    }

    override fun setSelectedTask(task: Task) {
        // Not required for the remote data source.
        throw NotImplementedError()
    }

    override fun getSelectedTask(): Task {
        // Not required for the remote data source.
        throw NotImplementedError()
    }

    override suspend fun updateTask(task: Task) {
        var localTask = task
        tasksServiceData[task.id.toString()] = localTask

    }

    override suspend fun removeAllTask() {
        tasksServiceData.clear()
    }

    override suspend fun initializeStartingTasks() {
        TODO("Not yet implemented")
        throw NotImplementedError()
    }

    override suspend fun completeTask(task: Task) {
        val completedTask = Task(
            task.id, task.title, task.description, true,
            task.imageId, task.priority, task.createTime
        )
        tasksServiceData[task.id.toString()] = completedTask
    }


    override suspend fun activateTask(task: Task) {
        val activeTask = Task(
            task.id, task.title, task.description, false,
            task.imageId, task.priority, task.createTime
        )
        tasksServiceData[task.id.toString()] = activeTask
    }

    suspend fun clearCompletedTasks() {
        tasksServiceData = tasksServiceData.filterValues {
            !it.isCompleted
        } as LinkedHashMap<String, Task>
    }



}
