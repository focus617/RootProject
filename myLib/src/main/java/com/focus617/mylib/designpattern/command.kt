package com.focus617.mylib.designpattern

import com.focus617.mylib.designpattern.platform.*
import com.focus617.mylib.logging.WithLogging

/**
 * @description Command类用来将一个请求封装为一个对象，并声明执行操作的接口
 */
abstract class Command constructor(val invoker: Invoker, val receiver: Receiver) {
    companion object : WithLogging()

    /**
     * 执行操作
     * TODO: refact with map-type parameter to execute() and Response class as its return value
     */
    abstract fun execute(event: Event)
}


/**
 * @description 具体的Command 将一个接收者对象绑定于一个动作，调用该接受者相应的操作
 */
class ConcreteCommandHandler1 constructor(invoker: Invoker, receiver: Receiver) :
    Command(invoker, receiver) {

    override fun execute(event: Event) {
        LOG.info("${this::class.java.simpleName}: 处理${event.name}事件消息")
        receiver.action(event)
    }
}

class ConcreteCommandHandler2 constructor(invoker: Invoker, receiver: Receiver) :
    Command(invoker, receiver) {

    override fun execute(event: Event) {
        LOG.info("${this::class.java.simpleName}: 处理${event.name}事件消息")
        receiver.action(event)
    }
}


/**
 * @description Receiver类指定如何实施与执行一个请求相关的操作。任何一个类都可能成为一个接收者
 */
class Receiver : BaseObject() {
    companion object : WithLogging()

    /**
     * 接收者相应的操作
     */
    fun action(event: Event) {
        LOG.info("我是${this} ")
        LOG.info("${this::class.java.simpleName}: 从${event.source}收到${event.name}事件")
        LOG.info("${this::class.java.simpleName}: 执行${event.name}事件的处理程序\n")
    }
}


/**
 * @description Invoker类 要求该Command执行这个请求
 */
class Invoker : BaseObject() {
    companion object : WithLogging()

    private var handlers = HashMap<String, Command>()

    fun addHandler(eventName: String, handler: Command) {
        handlers[eventName] = handler
    }

    fun removeHandler(eventName: String) {
        handlers.remove(eventName)
    }

    private fun lookupHandlerBy(eventName: String): Command? = handlers[eventName]

    fun executeCommand(event: Event) {
        LOG.info("${this::class.java.simpleName}: 执行命令，发出${event.name}事件")
        lookupHandlerBy(event.name)?.execute(event)
    }
}


/**
 * @description Client 客户端
 */
class ClientCommand {
    companion object : WithLogging() {
        @JvmStatic
        fun main(vararg arg: String) {
            val invoker = Invoker()
            val receiver1 = Receiver()
            val receiver2 = Receiver()

            // 动态构造事件/命令处理框架
            invoker.addHandler(Event_AppStart, ConcreteCommandHandler1(invoker, receiver1))
            invoker.addHandler(Event_Wakeup, ConcreteCommandHandler2(invoker, receiver2))

            invoker.executeCommand(AppStartEvent(invoker))
            invoker.executeCommand(WakeupEvent(invoker))

            // 在运行时改变处理请求的对象，以及如何处理请求
            LOG.info("更改WakeupEvent的Receiver/Handler")
            invoker.removeHandler(Event_Wakeup)
            invoker.addHandler(Event_Wakeup, ConcreteCommandHandler1(invoker, receiver1))

            invoker.executeCommand(WakeupEvent(invoker))
        }

    }
}
