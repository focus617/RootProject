package com.example.pomodoro2.platform.designpattern

import com.example.pomodoro2.platform.logging.WithLogging
import com.example.pomodoro2.platform.logging.unwrapCompanionClass

// 抽象方法类
abstract class Subject {
    companion object : WithLogging()

    // 抽象方法
    abstract fun request()
}

open class RealSubject: Subject(){
    override fun request() {
        LOG.info("对${unwrapCompanionClass(this.javaClass).simpleName}" +
                "的真实的操作")
    }

}

open class Proxy: Subject(){
    private lateinit var realSubject : RealSubject

    override fun request() {
        LOG.info("${unwrapCompanionClass(this.javaClass).simpleName}:")

        if(!this::realSubject.isInitialized) realSubject = RealSubject()
        realSubject!!.request()
    }
}


fun main(vararg args: String) {
    val proxy = Proxy()
    
    // 通过代理，访问真实对象
    proxy.request()
}
