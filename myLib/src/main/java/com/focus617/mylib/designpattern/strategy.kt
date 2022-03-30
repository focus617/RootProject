package com.focus617.mylib.designpattern

import com.focus617.mylib.logging.WithLogging
import com.focus617.mylib.logging.unwrapCompanionClass
import java.util.*
import kotlin.collections.ArrayList

// 抽象方法类
abstract class Strategy {
    companion object : WithLogging()

    // 抽象算法方法
    abstract fun algorithmInterface(context: Context)
}

// 抽象Context类
abstract class Context {

    private var _strategies = ArrayList<Strategy>()

    companion object : WithLogging()

    fun addStrategy(strategy: Strategy) {
        this._strategies.add(strategy)
    }

    // 上下文接口
    fun contextInterface() {
        for (each in _strategies)
            each.algorithmInterface(this)
    }

    // 对 Strategy 提供的接口
    fun getInfo(strategy: Strategy) {
        LOG.info(
            "Hi,${unwrapCompanionClass(strategy.javaClass).simpleName}." +
                    " 你想知道什么？"
        )
    }
}


// 具体算法A
class ConcreteStrategyA : Strategy() {
    override fun algorithmInterface(context: Context) {
        // 向Context请求数据
        context.getInfo(this)

        // 算法A实现
        LOG.info("(算法A实现): Hello, ${unwrapCompanionClass(context.javaClass).simpleName} !\n")
    }
}

// 具体算法B
class ConcreteStrategyB : Strategy() {
    override fun algorithmInterface(context: Context) {
        // 向Context请求数据
        context.getInfo(this)

        // 算法B实现
        LOG.info("(算法B实现): Hello, ${unwrapCompanionClass(context.javaClass).simpleName} !\n")
    }
}

// 具体算法C
class ConcreteStrategyC : Strategy() {
    override fun algorithmInterface(context: Context) {
        // 向Context请求数据
        context.getInfo(this)

        // 算法C实现
        LOG.info("(算法C实现): Hello, ${unwrapCompanionClass(context.javaClass).simpleName} !\n")
    }
}

class ConcreteContext : Context() {
    fun newStrategyInstance(algorithmType: String) {
        when (algorithmType) {
            // 同时应用了简单工厂模式
            "A" -> addStrategy(ConcreteStrategyA())
            "B" -> addStrategy(ConcreteStrategyB())
            "C" -> addStrategy(ConcreteStrategyC())
            else -> LOG.info("Enter the wrong strategy.")
        }
    }

}

class ClientStrategy {
    companion object : WithLogging() {
        @JvmStatic
        fun main(args: Array<String>) {
            val context = ConcreteContext()

            println("请输入字母选择策略规则：")
            println("\tA -> 策略规则A")
            println("\tB -> 策略规则B")
            println("\tC -> 策略规则C")

            loop@ while (true) {
                println("请选择（A，B，C）或输入Q：退出:")
                try {
                    val sc = Scanner(System.`in`)
                    when (val select = sc.nextLine()) {
                        "A", "B", "C", "a", "b", "c" -> context.newStrategyInstance(select.toUpperCase())
                        "Q", "q" -> break@loop
                        else -> println("你输入有错, 请重新输入")
                    }

                } catch (e: Exception) {
                    println("你输入有错：${e.message}")
                }
            }
            println("输出结果是：")
            context.contextInterface()

        }
    }
}

