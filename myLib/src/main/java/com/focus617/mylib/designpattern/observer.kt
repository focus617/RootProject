package com.example.pomodoro2.platform.designpattern

import com.focus617.mylib.logging.WithLogging
import com.focus617.mylib.logging.unwrapCompanionClass

// 抽象被观察者
abstract class Observable {
    companion object : WithLogging()

    // 方式一：保存观察者的实例
    private val observers = arrayListOf<Observer>()

    var subjectState = "正常运行"

    fun register(observer: Observer) {
        LOG.info("${unwrapCompanionClass(observer.javaClass).simpleName} Registered")
        observers.add(observer)
    }

    fun unregister(observer: Observer) {
        LOG.info("${unwrapCompanionClass(observer.javaClass).simpleName} Unregistered")
        observers.remove(observer)
    }

    fun notifySubscribers() = observers.forEach { it.update() }


    // 方式二：保存观察者的更新方法
    private var eventHandler = HashMap<Int, () -> Unit>()

    fun attach(key: Int, event: () -> Unit) {
        LOG.info("$key Attached")
        eventHandler[key] = event
    }

    fun detach(key: Int) {
        LOG.info("$key Detached")
        eventHandler.remove(key)
    }

    open fun notifyObservers() = eventHandler.forEach { it.value() }

}

// 抽象观察者
abstract class Observer {
    companion object : WithLogging()

    abstract fun update()
}


// 具体的被观察者
class ConcreteObservable : Observable()


//具体的观察者A与B
class ConcreteObserverA(private val subject: Observable) : Observer() {
    init {
        subject.register(this)
    }

    // 方式一：注册实例
    override fun update() = LOG.info(
        unwrapCompanionClass(this.javaClass).simpleName +
                " receive update from ${unwrapCompanionClass(subject.javaClass).simpleName}:" +
                " subjectState=${subject.subjectState}"
    )
}

class ConcreteObserverB(private val subject: Observable) : Observer() {
    init {
        subject.register(this)
    }

    // 方式一：注册实例
    override fun update() = LOG.info(
        unwrapCompanionClass(this.javaClass).simpleName +
                " receive update from ${unwrapCompanionClass(subject.javaClass).simpleName}:" +
                " subjectState=${subject.subjectState}"
    )

}

class ConcreteObserverC(private val subject: Observable) {
    companion object : WithLogging()

    init {
        subject.attach(this.hashCode()) { this.updateObserverC() }
    }

    // 方式二：注册方法
    fun updateObserverC() = LOG.info(
        unwrapCompanionClass(this.javaClass).simpleName +
                " receive update from ${unwrapCompanionClass(subject.javaClass).simpleName}:" +
                " subjectState=${subject.subjectState}"
    )
}

class ConcreteObserverD(private val subject: Observable) {
    companion object : WithLogging()

    init {
        subject.attach(this.hashCode()) { this.updateObserverD() }
    }

    // 方式二：注册方法
    fun updateObserverD() = LOG.info(
        unwrapCompanionClass(this.javaClass).simpleName +
                " receive update from ${unwrapCompanionClass(subject.javaClass).simpleName}:" +
                " subjectState=${subject.subjectState}"
    )

}

// 测试程序
class ClientObserver {
    companion object : WithLogging() {
        @JvmStatic
        fun main(args: Array<String>) {
            val subject = ConcreteObservable()

            // 方式一：注册实例
            val observerA = ConcreteObserverA(subject)
            val observerB = ConcreteObserverB(subject)

            subject.notifySubscribers()

            subject.unregister(observerA)
            subject.notifySubscribers()

            // 方式二：注册方法
            val observerC = ConcreteObserverC(subject)
            val observerD = ConcreteObserverD(subject)

            subject.notifyObservers()

            subject.detach(observerD.hashCode())
            subject.notifyObservers()
        }
    }
}