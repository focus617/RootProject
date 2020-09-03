package com.example.pomodoro2.platform.domain

import com.example.pomodoro2.domain.model.Task

/**
 * @description 抽象规格类，声明接口，抽象所有类成员共有的默认行为
 */
abstract class BaseSpecification<T> {

    open fun isSatisfiedBy(t: T): Boolean = true
}

/**
 * @description 具体规格类 (Concrete Specification)
 * 它通过构造函数接收作为判决条件的参数值，并提供对这个值的访问方法
 *
 * @Example:
 * class ChildTaskSpec(private val parent: Task):BaseSpecification<Task>() {
 *     override fun isSatisfiedBy(t: Task) = (t.getParent() == this.parent)
 * }
 */

/**
 * @description 组合规格类（Composite Specification）
 * 可以进一步细化为 AndSpec, OrSpec 和 NotSpec(是否需要？)
 *
 * @usage:
 *  andSpecs = AndSpecification<Task>()
 *  andSpecs.add(ConcreteSpecification1())
 *  andSpecs.add(ConcreteSpecification2())
 *  orSpecs = OrSpecification<Task>()
 *  orSpecs.add(andSpecs)
 *  orSpecs.add(ConcreteSpecification3())
 *  orSpecs.isSatisfiedBy(t)
 *
 */
class AndSpecification<T>: BaseSpecification<T>() {

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

class OrSpecification<T>: BaseSpecification<T>() {

    private val specs = arrayListOf<BaseSpecification<T>>()

    fun getSpecs() = specs.toList()

    fun add(specification: BaseSpecification<T>){
        specs.add(specification)
    }

    override fun isSatisfiedBy(t: T): Boolean {

        // 依次比对每个Specification
        val specifications = getSpecs().iterator()
        var satisfiesAllSpecs = false

        while (specifications.hasNext()) {
            val specification = specifications.next()
            satisfiesAllSpecs = satisfiesAllSpecs || specification.isSatisfiedBy(t)
        }
        return satisfiesAllSpecs
    }
}