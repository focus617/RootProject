package com.example.pomodoro2.platform.domain

abstract class BaseSpecification<T> {
    open fun isSatisfiedBy(t: T): Boolean = true
}

/**
 * Composite pattern for BaseSpecification
 *
 * @usage:
 *  specs = CompositeSpecification()
 *  specs.add(ConcreteSpecification())
 *  specs.isSatisfiedBy(t)
 */
class CompositeSpecification<T>: BaseSpecification<T>() {

    private val specs = ArrayList<BaseSpecification<T>>()

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