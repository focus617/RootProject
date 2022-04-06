package com.focus617.mylib.designpattern

import com.focus617.mylib.designpattern.platform.BaseObject
import com.focus617.mylib.logging.WithLogging
import com.focus617.mylib.logging.unwrapCompanionClass
import java.lang.reflect.InvocationHandler
import java.lang.reflect.Method
import java.lang.reflect.Proxy

// 抽象方法类
interface ICommonAct {
    // 抽象方法
    fun requestA()
    fun requestB()
}

open class RealSubject : BaseObject(), ICommonAct {

    override fun requestA() {
        LOG.info(
            "${unwrapCompanionClass(this.javaClass).simpleName}:" +
                    "收到请求a，开始真实的操作\n"
        )
    }

    override fun requestB() {
        LOG.info(
            "${unwrapCompanionClass(this.javaClass).simpleName}:" +
                    "收到请求b，开始真实的操作\n"
        )
    }

}

class Proxy1 : RealSubject() {

    private lateinit var realSubject: RealSubject

    override fun requestA() {
        if (!this::realSubject.isInitialized) {
            LOG.info(
                "${unwrapCompanionClass(this.javaClass).simpleName}:" +
                        "真实对象还不存在，开始构造..."
            )
            realSubject = RealSubject()
        }

        // Proxy可以在这里引入附加处理
        LOG.info(
            "${unwrapCompanionClass(this.javaClass).simpleName}:" +
                    "进行实际操作前的附加处理..."
        )

        // 然后再调用真实对象的方法
        LOG.info(
            "${unwrapCompanionClass(this.javaClass).simpleName}:" +
                    "对真实对象进行实际操作"
        )
        realSubject.requestA()
    }

    override fun requestB() {
        if (!this::realSubject.isInitialized) {
            LOG.info(
                "${unwrapCompanionClass(this.javaClass).simpleName}:" +
                        "真实对象还不存在，开始构造..."
            )
            realSubject = RealSubject()
        }

        // 直接调用真实对象的方法
        LOG.info(
            "${unwrapCompanionClass(this.javaClass).simpleName}:" +
                    "直接转发给真实对象，进行实际操作"
        )
        realSubject.requestB()
    }

}


//通过Kotlin by关键字实现代理，省略大量的代理类中的样板代码
class Proxy2(private val realSubject: RealSubject) : BaseObject(), ICommonAct by realSubject {

    // 访问权限
    var isAllowed = false

    // 只需要重写需要改变的个别方法即可,
    // 其它未变更的方法将会直接调用被代理对象的相应方法，这个非常方便
    override fun requestB() {
        if (isAllowed) realSubject.requestB()
        else LOG.info("很抱歉，你还没有取得访问权限。\n")
    }

}

// 第三种方法支持嵌套
class Proxy3(private val subject: ICommonAct) : BaseObject(), ICommonAct {
    // 访问权限
    var isAllowed = false

    override fun requestA() {
        if (isAllowed) subject.requestA()
        else LOG.info("很抱歉，你还没有取得访问权限。\n")
    }

    override fun requestB() {
        LOG.info("Proxy3 logging: start requestB")
        subject.requestB()
        LOG.info("Proxy3 logging: end requestB\n")
    }

}


/**
 * DynamicProxy类
 * 可以动态生成任意个代理对象，不需要开发者手动编写代理类代码。
 */
class DynamicProxy(
    private val `object`: Any   //传入被代理类的实例引用
) : BaseObject(), InvocationHandler {
    // 访问权限
    private var isAllowed = true

    private fun before(method: Method, args: Array<out Any>?) {
        LOG.info("DynamicProxy: I can do something before RealSubject")
        LOG.info("DynamicProxy: method ${method.name} will be invoked")
        LOG.info("args: ${args.toString()} ")

        // 这里可以分析被调用的method和参数，加入自己的代理逻辑(例如权限控制)
        isAllowed = when (method.name) {
            "requestB" -> false
            else -> true
        }
    }

    private fun after(method: Method, result: Any?) {
        LOG.info("DynamicProxy: I can do something after RealSubject")
        LOG.info("DynamicProxy: method ${method.name} has been invoked")
        LOG.info("result: ${result.toString()} \n")
    }

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
}

class ClientProxy {
    companion object : WithLogging() {
        @JvmStatic
        fun main(vararg args: String) {
            val subject = RealSubject()

            // 测试第一种代理方法
            val proxy1 = Proxy1()
            // 通过代理，访问真实对象
            LOG.info("Proxy1 start requestA")
            proxy1.requestA()
            // 测试第二次操作
            LOG.info("Proxy1 start requestB")
            proxy1.requestB()

            // 测试第二种代理方法
            Proxy2(subject).run {
                isAllowed = false
                LOG.info("Proxy2 start requestA")
                requestA()
                LOG.info("Proxy2 start requestB")
                requestB()
            }

            // 测试第三种方法：嵌套Proxy1
            Proxy3(Proxy1()).run {
                isAllowed = false
                LOG.info("Proxy3 start requestA")
                requestA()
                LOG.info("Proxy3 start requestB")
                requestB()
            }

            // 测试动态代理
            val invocationHandler = DynamicProxy(subject)

            // Proxy.newProxyInstance方法动态构造一个代理中介，拦截真实对象的操作
            // 需要传入被代理类的ClassLoader、共同接口集合和dynamicProxy实例对象
            // 采用Reflection机制，通过二进制字节码分析类的属性和方法
            val proxy4 = Proxy.newProxyInstance(
                subject.javaClass.classLoader,
                arrayOf(ICommonAct::class.java),    //RealSubject::class.java.interfaces,
                invocationHandler
            ) as ICommonAct

            LOG.info("Proxy4 start requestA")
            proxy4.requestA()
            LOG.info("Proxy4 start requestB")
            proxy4.requestB()
        }
    }
}





