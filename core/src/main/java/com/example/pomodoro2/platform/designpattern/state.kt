package com.example.pomodoro2.platform.designpattern

import com.example.pomodoro2.platform.logging.WithLogging


// 抽象状态类，定义状态类的基本接口
abstract class State {
    companion object : WithLogging()
    init{
        LOG.info("进入${this::class.java.simpleName}状态")
    }

    // 根据不同的状态进去处理
    abstract fun handle(ctx: StateContext)

}

// 具体的状态类，实现具体的行为。
class ConcreteState1 : State() {

    override fun handle(ctx: StateContext) {
        LOG.info("${this::class.java.simpleName}处理请求完毕，切换状态")
        ctx.state = ConcreteState2()//将状态改成状态2
    }

}

class ConcreteState2 : State() {

    override fun handle(ctx: StateContext) {
        LOG.info("${this::class.java.simpleName}处理请求完毕，切换状态")
        ctx.state = ConcreteState1()//将状态改成状态1
    }
}

// 持有状态的类
class StateContext(var state: State) {
    companion object : WithLogging()

    fun request() {
        LOG.info("${this::class.java.simpleName}: 状态变迁")
        state.handle(this)
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
            context.request()
            context.request()
            context.request()
            //此例比较简单，还可以应用在通过传入不同的条件来决定使用哪个状态或是否要切换到哪个状态进行处理

        }
    }
}
