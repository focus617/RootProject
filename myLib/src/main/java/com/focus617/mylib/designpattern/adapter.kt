package com.focus617.mylib.designpattern

import com.focus617.mylib.logging.WithLogging

/**
 * @description 目标类，这是客户所期待的接口或（具体/抽象）类。
 */
open class Target {
    companion object : WithLogging()
    open fun request() {
        LOG.info("${this::class.java.simpleName}: 收到普通请求！")
    }
}

/**
 * @description 需要是适配的类
 */
class Adaptee {
    companion object : WithLogging()
    fun specificRequest(){
        LOG.info("${this::class.java.simpleName}: 收到特殊请求！")
    }
}

/**
 * @description 适配器类（目标类的实现类），通过在内部包装一个需要适配的类Adaptee对象 ，把源接口转成目标接口。
 * 简单的来说，就是当目标类调用指定的方法时，内部实现执行适配的方法，达到适配的效果。
 */
class Adapter : Target() {
    //建立一个私有的Adaptee对象
    private val adaptee = Adaptee()

    override fun request() {
        //表面是Target的request方法，实际变成调用了想要适配的类的方法。
        super.request()
        adaptee.specificRequest()
    }
}

// 测试类
class ClientAdapter {
    companion object : WithLogging() {
        @JvmStatic
        fun main(vararg args: String) {
            //初始化
            val target = Adapter()

            target.request()//达到adaptee能适合Target使用的效果。

        }
    }
}