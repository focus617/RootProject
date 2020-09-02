package com.example.pomodoro2.platform.data

import com.example.pomodoro2.platform.domain.BaseAggregateRoot
import com.example.pomodoro2.platform.domain.BaseSpecification
import com.example.pomodoro2.platform.functional.Result

/**
 * Interface to the Repository of domain.
 * @param [T] BaseAggregateRoot: 领域模型的基类
 * @param [Q] BaseSpecification: 适用于较为复杂的查询场景。
 */
interface IRepository<T: BaseAggregateRoot >{

    // TODO: change id type from Long to String in case of DomainEntity supporting with UUID
    suspend fun ofId(id: String, forceUpdate: Boolean = false): Result<T>

    suspend fun add(t: T)

    suspend fun remove(t: T)

    suspend fun querySpecification(forceUpdate: Boolean = false, q: BaseSpecification<T>? = null): Result<List<T>>

    fun setSelectedTask(t: T)

    fun getSelectedTask(): T

    suspend fun completeTask(t: T)

    suspend fun activateTask(t: T)

    suspend fun initializeStartingTasks()

    //
    // TODO: remove below fun due to thought: 用集合的思想来操作聚合根
    //
    suspend fun update(t: T)

    suspend fun removeAll()


}