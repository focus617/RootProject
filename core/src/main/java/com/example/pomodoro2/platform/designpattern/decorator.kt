package com.example.pomodoro2.platform.designpattern

import com.example.pomodoro2.platform.logging.WithLogging
import com.example.pomodoro2.platform.logging.unwrapCompanionClass

// 抽象被装饰类
abstract class Component {
    companion object: WithLogging()
    // 算法方法
    abstract fun operation()
}

// 抽象装饰类
abstract class Decorator: Component(){
    companion object: WithLogging()

    private lateinit var component: Component

    // 装扮方法
    fun decorate(component: Component){
        this.component = component
    }

    // 算法方法
    override fun operation() = component.operation()
}



// Test
open class Person(var name:String=""): Component(){
    override fun operation() {
        LOG.info("(原始类型${unwrapCompanionClass(this.javaClass).simpleName}的操作)"+
                "装扮的$name")
    }
}

class TShirt:Decorator(){
    // TShirt类的属性
    private var cloth = "大T恤"

    override fun operation() {
        super.operation()
        LOG.info("(具体装饰类型${unwrapCompanionClass(this.javaClass).simpleName}的操作)" +
                "上身穿$cloth")
    }
}

class BigTrousers:Decorator(){
    // BigTrousers类的方法
    private fun wear() = LOG.info("(具体装饰类型${unwrapCompanionClass(this.javaClass).simpleName}的操作)" +
            "下身配垮裤")

    override fun operation() {
        super.operation()
        wear()
    }
}

fun main(){
    val xiaoMing = Person("小明")
    // 未经装饰的功能
    xiaoMing.operation()

    val tShirt = TShirt()
    val bigTrousers = BigTrousers()

    // 装饰过程: 灵活动态地给Person添加装饰功能
    tShirt.decorate(xiaoMing)
    bigTrousers.decorate(tShirt)

    // 经过装饰之后的功能
    bigTrousers.operation()
}