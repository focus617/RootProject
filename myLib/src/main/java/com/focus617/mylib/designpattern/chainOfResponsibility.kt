package com.focus617.mylib.designpattern

import com.focus617.mylib.logging.WithLogging


/**
 * @description Handler 处理者类
 */
abstract class Handler {
    companion object : WithLogging()

    protected var mSuccessor: Handler? = null

    /**
     * 设置继承者
     */
    fun setSuccessor(successor: Handler): Handler {
        this.mSuccessor = successor
        return this
    }

    /**
     * 处理请求的方法
     */
    abstract fun handlerRequest(requestCode: Int)
}

/**
 * @description 具体处理者类，处理它所负责的请求，可访问它的后继者，
 * 如果可处理该请求，就处理之，否则就将该请求转发给它的后继者。
 */
class ConcreteHandler1 : Handler() {

    override fun handlerRequest(requestCode: Int) {
        when (requestCode) {
            in 0 until 10 -> LOG.info("${this::class.java.simpleName}: 可处理的{$requestCode}请求\n")
            else -> {
                LOG.info("${this::class.java.simpleName}: 无法处理的{$requestCode}请求，交给上级")
                mSuccessor?.handlerRequest(requestCode)
            }
        }
    }
}

class ConcreteHandler2 : Handler() {

    override fun handlerRequest(requestCode: Int) {
        when (requestCode) {
            in 10 until 20 -> LOG.info("${this::class.java.simpleName}: 可处理的{$requestCode}请求\n")
            else -> {
                LOG.info("${this::class.java.simpleName}: 无法处理的{$requestCode}请求，交给上级")
                mSuccessor?.handlerRequest(requestCode)
            }
        }
    }
}

class ConcreteHandler3 : Handler() {

    override fun handlerRequest(requestCode: Int) {
        when (requestCode) {
            in 20 until 30 -> LOG.info("${this::class.java.simpleName}: 可处理的{$requestCode}请求\n")
            else -> {
                LOG.info("${this::class.java.simpleName}: 无法处理的{$requestCode}请求，交给上级")
                mSuccessor?.handlerRequest(requestCode)
            }
        }
    }
}

/**
 * @description 最初处理者，必须处理
 */
class ConcreteFinalHandler : Handler() {

    override fun handlerRequest(requestCode: Int) {
        LOG.info("${this::class.java.simpleName}: 是boss处理{$requestCode}请求\n")
    }
}

/**
 * @description Client 客户端
 */
class ClientChainOfResponsibility {
    companion object : WithLogging() {
        @JvmStatic
        fun main(vararg arg: String) {
            val handler1 = ConcreteHandler1()
            val handler2 = ConcreteHandler2()
            val handler3 = ConcreteHandler3()
            val handlerFinal = ConcreteFinalHandler()

            //设置继承者（上级）
            handler1.setSuccessor(handler2)
            handler2.setSuccessor(handler3)
            handler3.setSuccessor(handlerFinal)

            //构建一个请求数组并开始进行请求
            for (requestCode in 0..40 step 5)
                handler1.handlerRequest(requestCode)
        }

    }
}