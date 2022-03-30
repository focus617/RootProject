package com.focus617.mylib.designpattern

import com.focus617.mylib.logging.WithLogging
import com.focus617.mylib.logging.unwrapCompanionClass

// 抽象被装饰类
abstract class Component {
    // 提供 Logger
    companion object : WithLogging()

    // 将被装饰的抽象方法
    abstract fun operation()
}

// 抽象装饰类
abstract class Decorator : Component() {

    // 被装饰者
    private lateinit var component: Component

    // 装扮方法
    fun decorate(component: Component) {
        this.component = component
    }

    // 算法方法
    override fun operation() {
        if (this::component.isInitialized)
            component.operation()
    }
}


// Test
open class Person(var name: String = "") : Component() {
    override fun operation() {
        LOG.info(
            "(原始类型${unwrapCompanionClass(this.javaClass).simpleName}的操作)" +
                    "${name}今天的装扮:"
        )
    }
}

class TShirt : Decorator() {
    // TShirt类的属性
    private var cloth = "大T恤"

    override fun operation() {
        super.operation()
        LOG.info(
            "(具体装饰类型${unwrapCompanionClass(this.javaClass).simpleName}的操作)" +
                    "上身穿$cloth"
        )
    }
}

class BigTrousers : Decorator() {
    // BigTrousers类的方法
    private fun wear() = LOG.info(
        "(具体装饰类型${unwrapCompanionClass(this.javaClass).simpleName}的操作)" +
                "下身配垮裤"
    )

    override fun operation() {
        super.operation()
        wear()
    }
}

class Shoes : Decorator() {
    // Shoes类的方法
    private fun wear() = LOG.info(
        "(具体装饰类型${unwrapCompanionClass(this.javaClass).simpleName}的操作)" +
                "脚穿一双运动鞋"
    )

    override fun operation() {
        super.operation()
        wear()
    }
}

class ClientDecorator {
    companion object : WithLogging() {
        @JvmStatic
        fun main(vararg args: String) {
            val xiaoMing = Person("小明")

            val tShirt = TShirt()
            val bigTrousers = BigTrousers()
            val shoes = Shoes()

            // 装饰过程: 灵活动态地给Person添加装饰功能
            tShirt.decorate(xiaoMing)
            bigTrousers.decorate(tShirt)
            shoes.decorate(bigTrousers)


            // 经过装饰之后的功能
            print("小明的套装:\n")
            shoes.operation()
        }
    }
}