package com.example.pomodoro2.platform.domain

abstract class BaseSpecification<T> {
    fun isSatisfiedBy(t:T):Boolean = true
}