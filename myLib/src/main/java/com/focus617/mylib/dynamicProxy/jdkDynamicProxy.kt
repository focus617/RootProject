package com.focus617.mylib.dynamicProxy

import com.focus617.mylib.designpattern.platform.BaseObject
import com.focus617.mylib.logging.WithLogging
import com.focus617.mylib.logging.unwrapCompanionClass
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

// 抽象方法类
interface ExecutorInterface {
    // 抽象方法
    fun execute()
    fun requestB()
}

// 被代理对象
open class Executor : BaseObject(), ExecutorInterface {

    override fun execute() {
        LOG.info(
            "${unwrapCompanionClass(this.javaClass).simpleName}:" +
                    "收到execute请求，开始真实的操作\n"
        )
    }

    override fun requestB() {
        LOG.info(
            "${unwrapCompanionClass(this.javaClass).simpleName}:" +
                    "收到requestB请求，开始真实的操作\n"
        )
    }
}

// DynamicProxy类
class DynamicProxy(
    private val `object`: Any   //传入被代理类的实例引用
) : BaseObject(), InvocationHandler {

    @Throws(Throwable::class)
    override operator fun invoke(proxy: Any?, method: Method, args: Array<out Any>?): Any? {
        before(method, args)

        val result =
            if (isAllowed) method.invoke(`object`, *(args ?: emptyArray()))
            else {
                LOG.info("很抱歉，你还没有取得访问权限。")
                "Failed"
            }

        after(method, result)
        return result
    }

    // 访问权限
    private var isAllowed = true

    private fun before(method: Method, args: Array<out Any>?) {
        LOG.info("DynamicProxy: I can do something before Real Executor implement execute()")
        LOG.info("DynamicProxy: method ${method.name} will be invoked")
        LOG.info("args: ${args.toString()} ")

        // 这里可以分析被调用的method和参数，加入自己的代理逻辑(例如权限控制)
        isAllowed = when (method.name) {
            "requestB" -> false
            else -> true
        }
    }

    private fun after(method: Method, result: Any?) {
        LOG.info("DynamicProxy: method ${method.name} has been invoked")
        LOG.info("DynamicProxy: I can do something after Real Executor implement execute()")
        LOG.info("result: ${result.toString()} \n")
    }

}

// 如果Executor新增了任何方法，那么Invoker和DynamicProxy将不需要任何改动就可以支持新增方法
class Invoker : BaseObject() {
    // Proxy.newProxyInstance方法动态构造一个代理中介，拦截真实对象的操作
    // 需要传入被代理类的ClassLoader、共同接口集合和dynamicProxy实例对象
    // 采用Reflection机制，通过二进制字节码分析Executor类的属性和方法
    private val executor: ExecutorInterface = Proxy.newProxyInstance(
        Executor::class.java.classLoader,
        arrayOf(ExecutorInterface::class.java),
        DynamicProxy(Executor())
    ) as ExecutorInterface

    operator fun invoke() {
        executor.execute()
        executor.requestB()
    }
}

class Client {
    companion object : WithLogging() {
        @JvmStatic
        fun main(vararg args: String) {

            // 测试动态代理
            val invoker = Invoker()

            LOG.info("Client requests execute()")
            invoker()
        }
    }
}





