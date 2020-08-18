package com.example.pomodoro2.platform.data

import com.example.pomodoro2.domain.Task
import com.example.pomodoro2.platform.domain.BaseAggregateRoot
import com.example.pomodoro2.platform.functional.Result

/**
 * Interface to the Repository of domain.
 * @param [T] BaseAggregateRoot: 领域模型的基类
 * @param [Q] BaseSpecification: 适用于较为复杂的查询场景。
 */
interface IRepository<T: BaseAggregateRoot/*, Q: BaseSpecification*/>{

    // TODO: change id type from Long to String in case of DomainEntity supporting with UUID
    suspend fun ofId(id: Long): Result<T>

    suspend fun add(t: T)

    suspend fun remove(task: Task)

    suspend fun querySpecification(/*q: Q*/): Result<List<Task>>

    fun setSelectedTask(task: Task)

    fun getSelectedTask(): Task

    //
    // TODO: remove below fun due to thought: 用集合的思想来操作聚合根
    //

    suspend fun updateTask(task: Task)

    suspend fun removeAllTask()

    suspend fun completeTask(task: Task)

    suspend fun activateTask(task: Task)

    suspend fun initializeStartingTasks()

}