package com.example.pomodoro2.platform.designpattern

import com.example.pomodoro2.platform.logging.WithLogging
import com.example.pomodoro2.platform.logging.unwrapCompanionClass

// 抽象方法类
abstract class Subject {
    companion object : WithLogging()

    // 抽象方法
    abstract fun request()
}

class RealSubject: Subject(){
    override fun request() {
        LOG.info("${unwrapCompanionClass(this.javaClass).simpleName}:" +
                "收到请求，开始真实的操作")
    }

}

class Proxy: Subject(){
    private lateinit var realSubject : RealSubject

    override fun request() {
        if(!this::realSubject.isInitialized) {
            LOG.info("${unwrapCompanionClass(this.javaClass).simpleName}:" +
                    "真实对象还不存在，开始构造...")
            realSubject = RealSubject()
        }

        // Proxy可以在这里引入附加处理
        LOG.info("${unwrapCompanionClass(this.javaClass).simpleName}:" +
                "进行实际操作前的附加处理...")

        // 调用真实对象的方法
        LOG.info("${unwrapCompanionClass(this.javaClass).simpleName}:" +
                "对真实对象进行实际操作")
        realSubject!!.request()
    }
}


fun main(vararg args: String) {
    val proxy = Proxy()

    // 通过代理，访问真实对象
    proxy.request()

    // 测试第二次操作
    proxy.request()
}
