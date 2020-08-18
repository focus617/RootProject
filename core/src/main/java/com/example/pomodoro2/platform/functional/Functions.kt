package com.example.pomodoro2.platform.functional

typealias Supplier<T> = () -> T

interface Consumer<T> {

    fun accept(t: T)
}