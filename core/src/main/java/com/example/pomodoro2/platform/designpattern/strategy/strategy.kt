package com.example.pomodoro2.platform.designpattern.strategy

import com.example.pomodoro2.platform.logging.WithLogging
import com.example.pomodoro2.platform.logging.unwrapCompanionClass

// 抽象方法类
abstract class Strategy {
    companion object: WithLogging(){}
    // 算法方法
    abstract fun algorithmInterface(context: Context)
}

// 抽象Context类
abstract class Context {

    private lateinit var _strategy: Strategy
    companion object: WithLogging(){}

    fun setStrategy(strategy: Strategy){
        this._strategy = strategy
    }

    // 上下文接口
    fun contextInterface(){
        _strategy.algorithmInterface(this)
    }

    // 对 Strategy 提供的接口
    fun getInfo(){
        LOG.info("Hi,${unwrapCompanionClass(_strategy.javaClass).simpleName}." +
                " 你想知道什么？")
    }
}



// 具体算法A
class ConcreteStrategyA: Strategy(){
    override fun algorithmInterface(context: Context) {
        // 向Context请求数据
        context.getInfo()
        LOG.info("(算法A实现): Say Hello!\n")
    }
}

// 具体算法B
class ConcreteStrategyB: Strategy(){
    override fun algorithmInterface(context: Context) {
        // 向Context请求数据
        context.getInfo()
        LOG.info("(算法B实现): Say Hello!\n")
    }
}

// 具体算法C
class ConcreteStrategyC: Strategy(){
    override fun algorithmInterface(context: Context) {
        // 向Context请求数据
        context.getInfo()
        LOG.info("(算法C实现): Say Hello!\n")
    }
}

class ConcreteContext(algorithmType: String): Context(){
    init{
        when(algorithmType){
            // 同时应用了简单工厂模式
            "A" -> setStrategy(ConcreteStrategyA())
            "B" -> setStrategy(ConcreteStrategyB())
            "C" -> setStrategy(ConcreteStrategyC())
            else -> LOG.info("Enter the wrong strategy.")
        }
    }

}


fun main() {
    var context: ConcreteContext?

    context = ConcreteContext("A")
    context.contextInterface()

    context = ConcreteContext("B")
    context.contextInterface()

    context = ConcreteContext("C")
    context.contextInterface()
}

