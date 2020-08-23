package com.example.pomodoro2.designpattern.strategy

// 抽象方法类
abstract class Strategy {
    // 算法方法
    abstract fun algorithmInterface()
}

// 抽象Context类
abstract class StrategyContext {

    private lateinit var _strategy: Strategy

    fun setStrategy(strategy: Strategy){
        this._strategy = strategy
    }

    // 上下文接口
    fun contextInterface(){
        _strategy.algorithmInterface()
    }
}



// 具体算法A
class ConcreteStrategyA: Strategy(){
    override fun algorithmInterface() {
        println("算法A实现")
    }
}

// 具体算法B
class ConcreteStrategyB: Strategy(){
    override fun algorithmInterface() {
        println("算法B实现")
    }
}

// 具体算法C
class ConcreteStrategyC: Strategy(){
    override fun algorithmInterface() {
        println("算法C实现")
    }
}

class Context(algorithmType: String): StrategyContext(){
    init{
        when(algorithmType){
            "A" -> setStrategy(ConcreteStrategyA())
            "B" -> setStrategy(ConcreteStrategyB())
            "C" -> setStrategy(ConcreteStrategyC())
            else -> println("Enter the wrong strategy.")
        }
    }

}


fun main() {
    var context = Context("A")
    context.contextInterface()

    context = Context("B")
    context.contextInterface()

    context = Context("C")
    context.contextInterface()
}

