package com.example.pomodoro2.platform.designpattern

import com.example.pomodoro2.platform.logging.WithLogging
import com.example.pomodoro2.platform.logging.unwrapCompanionClass

// 抽象被观察者
abstract class Observable {
    companion object : WithLogging()

    // 方式一：保存观察者的实例
    private val observers = arrayListOf<Observer>()

    fun register(observer: Observer) {
        LOG.info("${unwrapCompanionClass(this.javaClass).simpleName} Registered")
        observers.add(observer)
    }

    fun unregister(observer: Observer) {
        LOG.info("${unwrapCompanionClass(this.javaClass).simpleName} Unregistered")
        observers.remove(observer)
    }

    fun notifySubscriber() {
        observers.forEach {
            it.update()
        }
    }


    // 方式二：保存观察者的更新方法
    private var eventHandler = HashMap<Int, ()->Unit>()

    fun attach(key:Int, event:()->Unit){
        LOG.info("$key Attached")
        eventHandler[key]=event
    }

    fun detach(key:Int){
        LOG.info("$key detached")
        eventHandler.remove(key)
    }

    open fun notifyObserver() = eventHandler.forEach{event -> event.value}

}

// 抽象观察者
abstract class Observer {
    companion object : WithLogging()

    abstract fun update()
}



// 具体的被观察者
class ConcreteObservable: Observable()


//具体的观察者A与B
class ConcreteObserverA: Observer() {

    // 方式一：注册实例
    override fun update() {
        LOG.info("A--->updateObserverA")
    }

    // 方式二：注册方法
    fun updateObserverA(){
        LOG.info("A--->updateObserverA")
    }
}

class ConcreteObserverB: Observer() {

    // 方式一：注册实例
    override fun update() {
        LOG.info("B--->updateObserverB")
    }

    // 方式二：注册方法
    fun updateObserverB(){
        LOG.info("B--->updateObserverB")
    }

}

// 测试程序
fun main(vararg args: String) {
    val subject = ConcreteObservable()
    val observerA = ConcreteObserverA()
    val observerB = ConcreteObserverB()

    // 方式一：注册实例
    subject.register(observerA)
    subject.register(observerB)
    subject.notifySubscriber()

    // 方式二：注册方法
    subject.attach(observerA.hashCode()){observerA.updateObserverA()}
    subject.attach(observerB.hashCode()){observerB.updateObserverB()}

    subject.notifyObserver()
}