package com.example.pomodoro2.platform.domain

abstract class BaseSpecification<T> {
    open fun isSatisfiedBy(t:T):Boolean = true
}

class CompositeSpecification<T>(private val specs: List<BaseSpecification<T>>){
    fun getSpecs() = specs
}