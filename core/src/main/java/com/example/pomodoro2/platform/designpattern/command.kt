package com.example.pomodoro2.platform.designpattern

/**
* @description Command类用来声明执行操作的接口
*/
abstract class Command constructor(val receiver: Receiver) {
    /**
     * 执行操作
     */
    abstract fun execute()
}


/**
 * @description 具体的Command 将一个接收者对象绑定于一个动作，调用该接受者相应的操作
 */
class ConcreteCommand constructor(receiver: Receiver) : Command(receiver) {

    override fun execute() {
        receiver.action()
    }
}


/**
 * @description Receiver类指定如何实施与执行一个请求相关的操作。任何一个类都可能成为一个接收者
 */
class Receiver {
    /**
     * 接收者相应的操作
     */
    fun action() {
        println("执行请求")
    }
}

/**
 * @description Invoker类 要求该Command执行这个请求
 */
class Invoker {
    private var command: Command? = null

    fun setCommand(command: Command): Invoker{
        this.command = command
        return this
    }

    fun executeCommand() {
        command?.execute()
    }
}


/**
 * @description Client 客户端
 */
class Client {
    companion object {
        @JvmStatic
        fun main(vararg arg: String) {
            val receiver = Receiver()
            val cmd = ConcreteCommand(receiver)
            Invoker().setCommand(cmd)
                .executeCommand()
        }

    }
}
