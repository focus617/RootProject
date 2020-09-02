package com.example.pomodoro2.platform.domain

import com.example.pomodoro2.domain.model.Task

/**
 * @description 为组合对象声明接口，抽象所有类成员共有的默认行为
 */
abstract class BaseSpecification<T> {
    open fun isSatisfiedBy(t: T): Boolean = true
}

/**
 * Example:
 * @description 叶子节点对象，叶子节点没有子节点
 *
 * class ChildTaskSpec(private val parent: Task):BaseSpecification<Task>() {
 *     override fun isSatisfiedBy(t: Task) = (t.getParent() == this.parent)
 * }
 */

/**
 * Composite pattern for BaseSpecification
 * @description 有枝节点对象，用来存储子部件
 *
 * @usage:
 *  specs = CompositeSpecification<Task>()
 *  specs.add(ConcreteSpecification())
 *  specs.isSatisfiedBy(t)
 *
 */
class CompositeSpecification<T>: BaseSpecification<T>() {

    private val specs = arrayListOf<BaseSpecification<T>>()

    fun getSpecs() = specs.toList()

    fun add(specification: BaseSpecification<T>){
        specs.add(specification)
    }

    override fun isSatisfiedBy(t: T): Boolean {

        // 依次比对每个Specification
        val specifications = getSpecs().iterator()
        var satisfiesAllSpecs = true

        while (specifications.hasNext()) {
            val specification = specifications.next()
            satisfiesAllSpecs = satisfiesAllSpecs && specification.isSatisfiedBy(t)
        }
        return satisfiesAllSpecs
    }
}