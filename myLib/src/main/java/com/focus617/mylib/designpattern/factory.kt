package com.example.pomodoro2.platform.designpattern

import com.focus617.mylib.logging.WithLogging
import com.focus617.mylib.logging.unwrapCompanionClass


// 抽象产品
abstract class AbstractProduct {
    companion object : WithLogging()

    init {
        LOG.info("当前构建的产品是：${unwrapCompanionClass(this.javaClass).simpleName}")
    }
}

// 简单工厂
interface ISimpleFactory {
    // 提供一个创建产品的方法
    fun make(productType: String): AbstractProduct?
}

// 抽象工厂
interface IFactory {
    // 提供一个创建产品的方法
    fun makeProduct(): AbstractProduct
}


// 产品类A
class ProductA : AbstractProduct()
class ProductB : AbstractProduct()
class ProductC : AbstractProduct()

// 简单工厂
class SimpleFactory : ISimpleFactory {
    companion object : WithLogging()

    init {
        LOG.info("构建工厂：${unwrapCompanionClass(this.javaClass).simpleName}")
    }

    // 每次需要更改此处，以添加新产品
    override fun make(productType: String): AbstractProduct? = when (productType) {
        "A" -> ProductA()
        "B" -> ProductB()
        "C" -> ProductC()
        else -> null
    }

}

// 产品A的工厂
class ProductAFactory : IFactory {
    companion object : WithLogging()

    init {
        LOG.info("构建工厂：${unwrapCompanionClass(this.javaClass).simpleName}")
    }

    override fun makeProduct() = ProductA()

}

class ProductBFactory : IFactory {
    companion object : WithLogging()

    init {
        LOG.info("构建工厂：${unwrapCompanionClass(this.javaClass).simpleName}")
    }

    override fun makeProduct() = ProductB()

}


class ClientFactory {
    companion object : WithLogging() {
        @JvmStatic
        fun main(args: Array<String>) {

            // 测试简单工厂
            val factory = SimpleFactory()
            val productA = factory.make("A")
            val productB = factory.make("B")
            val productC = factory.make("C")

            // 测试抽象工厂
            // 添加新产品的责任赋予了Client
            val productA1 = ProductAFactory().makeProduct()
            val productB1 = ProductBFactory().makeProduct()
        }
    }
}