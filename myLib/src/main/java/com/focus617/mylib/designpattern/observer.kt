package com.focus617.mylib.designpattern

import com.focus617.mylib.designpattern.platform.AppStartEvent
import com.focus617.mylib.designpattern.platform.BaseObject
import com.focus617.mylib.designpattern.platform.Event
import com.focus617.mylib.designpattern.platform.WakeupEvent
import com.focus617.mylib.logging.WithLogging
import com.focus617.mylib.logging.unwrapCompanionClass
import java.text.SimpleDateFormat
import java.util.*

// 抽象被观察者
abstract class Observable : BaseObject() {
    companion object : WithLogging()

    // 方式一：保存观察者的实例
    private val observers = arrayListOf<Observer>()

    var subjectState = "正常运行"

    var eventWakeup: Event =
        WakeupEvent(this, subjectState)

    var startupWakeup: Event =
        AppStartEvent(this, subjectState)

    fun register(observer: Observer) {
        LOG.info("${unwrapCompanionClass(observer.javaClass).simpleName} Registered")
        observers.add(observer)
    }

    fun unregister(observer: Observer) {
        LOG.info("${unwrapCompanionClass(observer.javaClass).simpleName} Unregistered")
        observers.remove(observer)
    }

    fun notifySubscribers(e: Event) = observers.forEach { it.update(e) }


    // 方式二：事件委托机制，保存观察者需执行的响应函数，耦合在抽象的方法，而不是观察者本身
    private var eventHandler = HashMap<Int, (e: Event) -> Unit>()

    fun attach(key: Int, handler: (e: Event) -> Unit) {
        LOG.info("$key Attached")
        eventHandler[key] = handler
    }

    fun detach(key: Int) {
        LOG.info("$key Detached")
        eventHandler.remove(key)
    }

    open fun notifyObservers(e: Event) = eventHandler.forEach { it.value(e) }

}

// 抽象观察者
abstract class Observer : BaseObject() {
    companion object : WithLogging()

    abstract fun update(e: Event)
}


// 具体的被观察者
class ConcreteObservable : Observable()


//具体的观察者A与B
class ConcreteObserverA(private val subject: Observable) : Observer() {
    init {
        subject.register(this)
    }

    // 方式一：注册实例
    override fun update(e: Event) = LOG.info(
        unwrapCompanionClass(this.javaClass).simpleName +
                " receive event from ${unwrapCompanionClass(e.source.javaClass).simpleName}:\n" +
                "\tevent\t\t\t=\t${unwrapCompanionClass(e.javaClass).simpleName}\n" +
                "\ttimeStamp\t\t=\t${
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(e.timestamp)
                }\n" +
                "\tsubjectState\t=\t${e.loc}"
    )
}

class ConcreteObserverB(private val subject: Observable) : Observer() {
    init {
        subject.register(this)
    }

    // 方式一：注册实例
    override fun update(e: Event) = LOG.info(
        unwrapCompanionClass(this.javaClass).simpleName +
                " receive event from ${unwrapCompanionClass(e.source.javaClass).simpleName}:\n" +
                "\tevent\t\t\t=\t${unwrapCompanionClass(e.javaClass).simpleName}\n" +
                "\ttimeStamp\t\t=\t${
                    SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.CHINA).format(e.timestamp)
                }\n" +
                "\tsubjectState\t=\t${e.loc}"
    )
}

class ConcreteObserverC() {
    companion object : WithLogging()

    fun attach(subject: Observable) {
        subject.attach(this.hashCode()) { e ->      // 方式二：注册方法
            LOG.info(
                unwrapCompanionClass(this.javaClass).simpleName +
                        " receive event from ${unwrapCompanionClass(e.source.javaClass).simpleName}:\n" +
                        "\tevent\t\t\t=\t${unwrapCompanionClass(e.javaClass).simpleName}\n" +
                        "\ttimeStamp\t\t=\t${
                            SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss",
                                Locale.CHINA
                            ).format(e.timestamp)
                        }\n" +
                        "\tsubjectState\t=\t${e.loc}"
            )
        }
    }

    fun detach(subject: Observable) {
        subject.detach(this.hashCode())
    }
}

class ConcreteObserverD(private val subject: Observable) {
    companion object : WithLogging()

    init {
        subject.attach(this.hashCode()) { e ->      // 方式二：注册方法
            LOG.info(
                unwrapCompanionClass(this.javaClass).simpleName +
                        " receive event from ${unwrapCompanionClass(e.source.javaClass).simpleName}:\n" +
                        "\tevent\t\t\t=\t${unwrapCompanionClass(e.javaClass).simpleName}\n" +
                        "\ttimeStamp\t\t=\t${
                            SimpleDateFormat(
                                "yyyy-MM-dd HH:mm:ss",
                                Locale.CHINA
                            ).format(e.timestamp)
                        }\n" +
                        "\tsubjectState\t=\t${e.loc}"
            )
        }
    }
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

            subject.notifySubscribers(subject.startupWakeup)

            subject.unregister(observerA)
            subject.notifySubscribers(subject.eventWakeup)

            // 方式二：注册方法
            // 测试两种attach策略
            val observerC = ConcreteObserverC()
            observerC.attach(subject)

            // 在构造对象时，直接attach
            val observerD = ConcreteObserverD(subject)

            subject.notifyObservers(subject.startupWakeup)

            observerC.detach(subject)

            subject.notifyObservers(subject.eventWakeup)
        }
    }
}