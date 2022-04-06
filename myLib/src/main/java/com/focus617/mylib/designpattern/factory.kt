package com.focus617.mylib.designpattern

import com.focus617.mylib.designpattern.platform.BaseObject
import com.focus617.mylib.logging.WithLogging
import com.focus617.mylib.logging.unwrapCompanionClass


// 抽象产品
abstract class AbstractProduct : BaseObject() {
    init {
        LOG.info("当前构建的产品是：${unwrapCompanionClass(this.javaClass).simpleName}")
    }
}

// 产品类A
class ProductA : AbstractProduct()
class ProductB : AbstractProduct()
class ProductC : AbstractProduct()

// 简单工厂
interface ISimpleFactory {
    // 提供一个创建产品的方法
    fun make(productType: String): AbstractProduct?
}

// 简单工厂
class SimpleFactory : BaseObject(), ISimpleFactory {

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

// 工厂方法
interface IFactory {
    // 提供一个创建产品的方法
    fun makeProduct(): AbstractProduct
}

// 产品A的工厂
class ProductAFactory : BaseObject(), IFactory {

    init {
        LOG.info("构建工厂：${unwrapCompanionClass(this.javaClass).simpleName}")
    }

    override fun makeProduct() = ProductA()

}

class ProductBFactory : BaseObject(), IFactory {

    init {
        LOG.info("构建工厂：${unwrapCompanionClass(this.javaClass).simpleName}")
    }

    override fun makeProduct() = ProductB()

}

// 抽象工厂
// 产品族One
class ProductAFamilyOne : AbstractProduct()
class ProductBFamilyOne : AbstractProduct()
class ProductCFamilyOne : AbstractProduct()

// 产品族Two
class ProductAFamilyTwo : AbstractProduct()
class ProductBFamilyTwo : AbstractProduct()
class ProductCFamilyTwo : AbstractProduct()

class FamilyOneFactory : BaseObject(), ISimpleFactory {

    init {
        LOG.info("构建工厂：${unwrapCompanionClass(this.javaClass).simpleName}")
    }

    // 每次需要更改此处，以添加新产品
    override fun make(productType: String): AbstractProduct? = when (productType) {
        "A" -> ProductAFamilyOne()
        "B" -> ProductBFamilyOne()
        "C" -> ProductCFamilyOne()
        else -> null
    }
}

class FamilyTwoFactory : BaseObject(), ISimpleFactory {

    init {
        LOG.info("构建工厂：${unwrapCompanionClass(this.javaClass).simpleName}")
    }

    // 每次需要更改此处，以添加新产品
    override fun make(productType: String): AbstractProduct? = when (productType) {
        "A" -> ProductAFamilyTwo()
        "B" -> ProductBFamilyTwo()
        "C" -> ProductCFamilyTwo()
        else -> null
    }
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

            LOG.info("\n")

            // 测试工厂方法
            // 添加新产品的责任赋予了Client
            val productA1 = ProductAFactory().makeProduct()
            val productB1 = ProductBFactory().makeProduct()

            LOG.info("\n")

            // 测试抽象工厂
            var factoryNew: ISimpleFactory = FamilyOneFactory()
            factoryNew.make("A")
            factoryNew.make("B")
            factoryNew.make("C")

            LOG.info("\n")

            LOG.info("切换到新主题")
            // 更改产品族，只需要改下面这一行
            factoryNew = FamilyTwoFactory()
            factoryNew.make("A")
            factoryNew.make("B")
            factoryNew.make("C")
        }
    }
}