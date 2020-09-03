package com.example.pomodoro2.platform.designpattern

import com.example.pomodoro2.platform.logging.WithLogging

/**
* @description Command类用来声明执行操作的接口
*/
abstract class Command constructor(val invoker: Invoker, val receiver: Receiver) {
    companion object : WithLogging()

    /**
     * 执行操作
     */
    abstract fun execute()
}


/**
 * @description 具体的Command 将一个接收者对象绑定于一个动作，调用该接受者相应的操作
 */
class ConcreteCommand1 constructor(invoker: Invoker, receiver: Receiver) : Command(invoker, receiver) {

    override fun execute() {
        LOG.info("${this::class.java.simpleName}: 处理事件消息")
        receiver.action()
    }
}

class ConcreteCommand2 constructor(invoker: Invoker, receiver: Receiver) : Command(invoker, receiver) {

    override fun execute() {
        LOG.info("${this::class.java.simpleName}: 处理事件消息")
        receiver.action()
    }
}


/**
 * @description Receiver类指定如何实施与执行一个请求相关的操作。任何一个类都可能成为一个接收者
 */
class Receiver {
    companion object : WithLogging()
    /**
     * 接收者相应的操作
     */
    fun action() {
        LOG.info("${this::class.java.simpleName}: 执行请求")
    }
}

/**
 * @description Invoker类 要求该Command执行这个请求
 */
class Invoker {

    private var handlers = HashMap<String, Command>()

    fun addHandler(eventName:String, handler: Command){
        handlers[eventName] = handler
    }

    fun removeHandler(eventName: String){
        handlers.remove(eventName)
    }

    private fun lookupHandlerBy(eventName: String):Command? = handlers.get(eventName)

    fun executeCommand(eventName: String) {
        lookupHandlerBy(eventName)?.execute()
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
            val receiver = Receiver()

            invoker.addHandler("Event1", ConcreteCommand1(invoker,receiver))
            invoker.addHandler("Event2", ConcreteCommand2(invoker,receiver))
            invoker.executeCommand("Event1")
            invoker.executeCommand("Event2")
        }

    }
}
