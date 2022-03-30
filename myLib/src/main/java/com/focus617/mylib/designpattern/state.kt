package com.focus617.mylib.designpattern

import com.focus617.mylib.logging.WithLogging

// 持有当前状态，以及状态变迁已知条件的类
class StateContext(var state: State) {
    companion object : WithLogging()
    
    // 一个用来计算状态切换条件的临时变量
    var count = 0

    fun request() {
        LOG.info("${this::class.java.simpleName}: 状态变迁")
        // 将Context自身作为参数，传递给各个State类
        state.handle(this)
    }
}

// 抽象对象：表示状态的类，定义状态类的基本接口
abstract class State {
    companion object : WithLogging()
    init{
        LOG.info("进入${this::class.java.simpleName}状态")
    }

    // 根据本状态的状态改变逻辑进行处理的抽象方法
    abstract fun handle(ctx: StateContext)

}

// 具体的状态类，实现具体的行为。
class ConcreteState1 : State() {

    override fun handle(ctx: StateContext) {
        LOG.info("${this::class.java.simpleName}处理请求完毕，切换状态")

        // 从Context获取状态变迁的已知因素
        ctx.count++

        // 处理状态变迁逻辑，决定下一步迁移的状态
        if (ctx.count % 2 == 0) {
            //将状态改成状态2
            ctx.state = ConcreteState2()
        } else {
            //将状态改成状态3
            ctx.state = ConcreteState3()
        }
    }

}

class ConcreteState2 : State() {

    override fun handle(ctx: StateContext) {
        LOG.info("${this::class.java.simpleName}处理请求完毕，切换状态")
        //将状态改成状态1
        ctx.state = ConcreteState1()
    }
}

class ConcreteState3 : State() {

    override fun handle(ctx: StateContext) {
        LOG.info("${this::class.java.simpleName}处理请求完毕，切换状态")
        //将状态改成状态1
        ctx.state = ConcreteState1()
    }
}



// 测试类
class ClientState {
    companion object : WithLogging() {
        @JvmStatic
        fun main(vararg args: String) {
            //初始化状态为状态1
            val context = StateContext(ConcreteState1())

            //不断的请求，并改变状态
            var i = 0
            while (i<10) {
                i++
                context.request()
            }
            //此例比较简单，还可以应用在通过传入不同的条件来决定使用哪个状态或是否要切换到哪个状态进行处理

        }
    }
}
