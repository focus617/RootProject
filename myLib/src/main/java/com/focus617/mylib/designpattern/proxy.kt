package com.focus617.mylib.designpattern

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

open class RealSubject : ICommonAct {
    companion object : WithLogging()

    override fun requestA() {
        LOG.info(
            "${unwrapCompanionClass(this.javaClass).simpleName}:" +
                    "收到请求a，开始真实的操作"
        )
    }

    override fun requestB() {
        LOG.info(
            "${unwrapCompanionClass(this.javaClass).simpleName}:" +
                    "收到请求b，开始真实的操作"
        )
    }

}

class Proxy1 : RealSubject() {
    companion object : WithLogging()

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


// //通过by关键字实现代理，省略大量的代理类中的样板代码
class Proxy2(private val realSubject: RealSubject) : ICommonAct by realSubject {
    companion object : WithLogging()

    // 访问权限
    var isAllowed = false

    override fun requestB() {
        if (isAllowed) realSubject.requestB()
        else LOG.info("很抱歉，你还没有取得访问权限。")
    }

}


/**
 * DynamicProxy类
 * 可以动态生成任意个代理对象，不需要开发者手动编写代理类代码。
 */
internal class DynamicProxy(   //传入被代理类的实例引用
    private val `object`: Any   //被代理类的引用
) : InvocationHandler {

    @Throws(Throwable::class)
    override operator fun invoke(proxy: Any?, method: Method, args: Array<Any?>?): Any {
        // 这里还未完成
        return method.invoke(`object`, args)
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
            proxy1.requestA()
            // 测试第二次操作
            proxy1.requestB()

            // 测试第二种代理方法
            Proxy2(subject).run {
                isAllowed = false
                requestA()
                requestB()
            }

            // 测试动态代理
            val invocationHandler  = DynamicProxy(subject)

            // Proxy.newProxyInstance方法动态构造一个代理中介，
            // 需要传入被代理类的ClassLoader、共同接口集合和dynamicProxy实例对象
            val proxy3 = Proxy.newProxyInstance(
                subject.javaClass.classLoader,
                RealSubject::class.java.interfaces,
                invocationHandler
            ) as ICommonAct
            proxy3.requestA()
            proxy3.requestB()
        }
    }
}
