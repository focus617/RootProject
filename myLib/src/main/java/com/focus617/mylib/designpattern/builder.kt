package com.focus617.mylib.designpattern

import com.focus617.mylib.logging.WithLogging
import com.focus617.mylib.logging.unwrapCompanionClass

// 被构建的产品
class Product {
    companion object : WithLogging()

    private val parts = ArrayList<String>()

    fun add(part: String) {
        LOG.info("${unwrapCompanionClass(this.javaClass).simpleName}: 装配部件$part\n")
        parts.add(part)
    }

    fun show() {
        LOG.info("显示产品状态：")
        LOG.info("产品\t\t已构建并装配好的部件")
        for (each in parts)
            LOG.info("${unwrapCompanionClass(this.javaClass).simpleName}\t\t$each")
    }
}

// 定义对各个部件的构建方法的抽象
abstract class Builder {
    companion object : WithLogging()

    // 构建方法的抽象
    abstract fun buildPartA()
    abstract fun buildPartB()

    // 提供一个检索产品的接口
    abstract fun getResult(): Product
}

// 对建造流程的抽象
open class Director {
    fun construct(builder: Builder) {
        builder.buildPartA()
        builder.buildPartB()
    }
}

// 负责具体构建的实现类
class ConcreteBuilder1 : Builder() {
    private val product = Product()

    override fun buildPartA() {
        LOG.info("${unwrapCompanionClass(this.javaClass).simpleName}: 开始建造部件A")
        product.add("部件A")
    }

    override fun buildPartB() {
        LOG.info("${unwrapCompanionClass(this.javaClass).simpleName}: 开始建造部件B")
        product.add("部件B")
    }

    override fun getResult(): Product {
        return product
    }

}

class ConcreteBuilder2 : Builder() {
    private val product = Product()

    override fun buildPartA() {
        LOG.info("${unwrapCompanionClass(this.javaClass).simpleName}: 开始建造部件X")
        product.add("部件X")
    }

    override fun buildPartB() {
        LOG.info("${unwrapCompanionClass(this.javaClass).simpleName}: 开始建造部件Y")
        product.add("部件Y")
    }

    override fun getResult(): Product {
        return product
    }

}

class ClientBuilder {
    companion object : WithLogging() {
        @JvmStatic
        fun main(vararg args: String) {
            val director = Director()
            val builder1 = ConcreteBuilder1()
            val builder2 = ConcreteBuilder2()

            director.construct(builder1)
            val product1 = builder1.getResult()
            product1.show()

            director.construct(builder2)
            val product2 = builder2.getResult()
            product2.show()
        }
    }
}
