package com.focus617.mylib.designpattern

import com.focus617.mylib.logging.WithLogging
import java.util.*

/**
 * 相较于 Type One
 * 优点：
 *   1）可以随意添加副链
 * 缺点：
 *
 */

/**
 * @description Handler 处理者接口
 */
interface IfHandler {
    /**
     * 处理请求的方法
     * 返回:
     * true，表示对象已被处理（销毁），也就没有必要再进行下一步处理
     * false，表示本次未完成处理，需要继续
     */
    fun handleRequest(requestCode: Int): Boolean
}

/**
 * @description 另一种责任链实现方式：具有统一的处理者，
 * 将请求依次传递给持有的处理者
 * 处理者负责如果可处理该请求，就处理之，否则返回不能处理的通知；
 * 再由本类将该请求转发给它的后继者。
 */
class HandlerChain : IfHandler {
    companion object : WithLogging()

    private val handlerChain = LinkedList<IfHandler>()

    fun add(handler: IfHandler) {
        handlerChain.add(handler)
    }

    override fun handleRequest(requestCode: Int): Boolean {
        for (handler in handlerChain) {
            if(handler.handleRequest(requestCode))
                // 如果某个handler具备处理该请求的能力，就可以退出
                return true
        }
        return false
    }
}

/**
 * @description 具体处理者类，处理它所负责的请求，可访问它的后继者，
 * 如果可处理该请求，就处理之，否则就将该请求转发给它的后继者。
 */
class ConcreteRespHandler1 : IfHandler {
    companion object : WithLogging()

    override fun handleRequest(requestCode: Int): Boolean = when (requestCode) {
        in 0 until 10 -> {
            LOG.info("${this::class.java.simpleName}: 我来处理{$requestCode}请求\n")
            true
        }
        else -> {
            LOG.info("${this::class.java.simpleName}: 无法处理的{$requestCode}请求，交给上级\n")
            false
        }
    }
}


class ConcreteRespHandler2 : IfHandler {
    companion object : WithLogging()

    override fun handleRequest(requestCode: Int): Boolean = when (requestCode) {
        in 10 until 20 -> {
            LOG.info("${this::class.java.simpleName}: 我来处理{$requestCode}请求\n")
            true
        }
        else -> {
            LOG.info("${this::class.java.simpleName}: 无法处理的{$requestCode}请求，交给上级\n")
            false
        }
    }
}

class ConcreteRespHandler3 : IfHandler {
    companion object : WithLogging()

    override fun handleRequest(requestCode: Int): Boolean = when (requestCode) {
        in 20 until 30 -> {
            LOG.info("${this::class.java.simpleName}: 我来处理{$requestCode}请求\n")
            true
        }
        else -> {
            LOG.info("${this::class.java.simpleName}: 无法处理的{$requestCode}请求，交给上级\n")
            false
        }
    }
}

class ConcreteRespHandler4 : IfHandler {
    companion object : WithLogging()

    override fun handleRequest(requestCode: Int): Boolean = when (requestCode) {
        in 30 until 40 -> {
            LOG.info("${this::class.java.simpleName}: 我来处理{$requestCode}请求\n")
            true
        }
        else -> {
            LOG.info("${this::class.java.simpleName}: 无法处理的{$requestCode}请求，交给上级\n")
            false
        }
    }
}

/**
 * @description 最初处理者，必须处理
 */
class ConcreteFinalRespHandler : IfHandler {
    companion object : WithLogging()

    override fun handleRequest(requestCode: Int): Boolean {
        LOG.info("${this::class.java.simpleName}: 我是boss，必须处理{$requestCode}请求\n")
        return true
    }
}

/**
 * @description Client 客户端
 */
class ClientChainOfResponsibilityTypeTwo {
    companion object : WithLogging() {
        @JvmStatic
        fun main(vararg arg: String) {
            //构造主责任链
            val handlerChain = HandlerChain()
            handlerChain.add(ConcreteRespHandler1())
            handlerChain.add(ConcreteRespHandler2())

            //构造副责任链
            val subHandlerChain = HandlerChain()
            subHandlerChain.add(ConcreteRespHandler3())
            subHandlerChain.add(ConcreteRespHandler4())
            handlerChain.add(subHandlerChain)

            handlerChain.add(ConcreteFinalRespHandler())

            //构建一个请求数组并开始进行请求
            for (requestCode in 0..50 step 5)
                handlerChain.handleRequest(requestCode)
        }

    }
}