package com.example.pomodoro2.platform.designpattern

import com.example.pomodoro2.platform.logging.WithLogging
import com.example.pomodoro2.platform.logging.unwrapCompanionClass

// 抽象方法类
abstract class Strategy {
    companion object: WithLogging()
    // 算法方法
    abstract fun algorithmInterface(context: Context)
}

// 抽象Context类
abstract class Context {

    private var _strategies = ArrayList<Strategy>()

    companion object: WithLogging()

    fun addStrategy(strategy: Strategy){
        this._strategies.add(strategy)
    }

    // 上下文接口
    fun contextInterface(){
        for(each in _strategies )
            each.algorithmInterface(this)
    }

    // 对 Strategy 提供的接口
    fun getInfo(strategy: Strategy){
        LOG.info("Hi,${unwrapCompanionClass(strategy.javaClass).simpleName}." +
                " 你想知道什么？")
    }
}



// 具体算法A
class ConcreteStrategyA: Strategy(){
    override fun algorithmInterface(context: Context) {
        // 向Context请求数据
        context.getInfo(this)

        // 算法A实现
        LOG.info("(算法A实现): Hello, ${unwrapCompanionClass(context.javaClass).simpleName} !\n")
    }
}

// 具体算法B
class ConcreteStrategyB: Strategy(){
    override fun algorithmInterface(context: Context) {
        // 向Context请求数据
        context.getInfo(this)

        // 算法B实现
        LOG.info("(算法B实现): Hello, ${unwrapCompanionClass(context.javaClass).simpleName} !\n")
    }
}

// 具体算法C
class ConcreteStrategyC: Strategy(){
    override fun algorithmInterface(context: Context) {
        // 向Context请求数据
        context.getInfo(this)

        // 算法C实现
        LOG.info("(算法C实现): Hello, ${unwrapCompanionClass(context.javaClass).simpleName} !\n")
    }
}

class ConcreteContext: Context(){
    fun newStrategyInstance(algorithmType: String)  {
        when(algorithmType){
            // 同时应用了简单工厂模式
            "A" -> addStrategy(ConcreteStrategyA())
            "B" -> addStrategy(ConcreteStrategyB())
            "C" -> addStrategy(ConcreteStrategyC())
            else -> LOG.info("Enter the wrong strategy.")
        }
    }

}


fun main() {
    val context = ConcreteContext()

    context.newStrategyInstance("A")
    context.contextInterface()

    context.newStrategyInstance("B")
    context.newStrategyInstance("C")
    context.contextInterface()
}

