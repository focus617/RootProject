package com.focus617.mylib.dynamicProxy

import com.focus617.mylib.logging.WithLogging
import com.focus617.mylib.logging.unwrapCompanionClass
import net.sf.cglib.proxy.Enhancer
import net.sf.cglib.proxy.MethodInterceptor
import net.sf.cglib.proxy.MethodProxy
import java.lang.reflect.Method

open class RealObject {
    companion object : WithLogging()

    fun requestA() {
        LOG.info(
            "${unwrapCompanionClass(this.javaClass).simpleName}:" +
                    "收到请求a，开始真实的操作\n"
        )
    }

    fun requestB() {
        LOG.info(
            "${unwrapCompanionClass(this.javaClass).simpleName}:" +
                    "收到请求b，开始真实的操作\n"
        )
    }
}

class TimeMethodInterceptor : MethodInterceptor {
    companion object : WithLogging()

    @Throws(Throwable::class)
    override fun intercept(
        obj: Any?, method: Method?, args: Array<out Any>?, proxy: MethodProxy?
    ): Any {
        LOG.info("before intercept")
        val result = proxy?.invokeSuper(obj, args) ?: false
        LOG.info("after intercept")

        return result
    }

}

class CglibDynamicProxy {
    companion object : WithLogging() {
        @JvmStatic
        fun main(vararg args: String) {

            // 测试动态代理
            val enhancer = Enhancer()
            enhancer.setSuperclass(RealObject::class.java)
            //回调方法的参数为代理类对象
            enhancer.setCallback(TimeMethodInterceptor())

            //增强过的目标类
            val obj: RealObject = enhancer.create() as RealObject
            obj.requestA()
            obj.requestB()
        }
    }
}
