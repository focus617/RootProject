package com.focus617.mylib.designpattern

import com.focus617.mylib.logging.WithLogging


/**
 * @description 抽象访问者
 */
abstract class Visitor {
    companion object : WithLogging()
    abstract fun visitorConcreteElementA(concreteElementA: ConcreteElementA)
    abstract fun visitorConcreteElementB(concreteElementB: ConcreteElementB)
}


/**
 * @description 具体访问者A
 */
class ConcreteVisitorA : Visitor() {
    override fun visitorConcreteElementA(concreteElementA: ConcreteElementA) {
        LOG.info("${concreteElementA::class.java.simpleName}被${this::class.java.simpleName}访问")
    }

    override fun visitorConcreteElementB(concreteElementB: ConcreteElementB) {
        LOG.info("${concreteElementB::class.java.simpleName}被${this::class.java.simpleName}访问")
    }
}

/**
 * @description 具体访问者B
 */
class ConcreteVisitorB : Visitor() {
    override fun visitorConcreteElementA(concreteElementA: ConcreteElementA) {
        LOG.info("${concreteElementA::class.java.simpleName}被${this::class.java.simpleName}访问")
    }

    override fun visitorConcreteElementB(concreteElementB: ConcreteElementB) {
        LOG.info("${concreteElementB::class.java.simpleName}被${this::class.java.simpleName}访问")
    }
}


/**
 * @description 抽象元素
 */
abstract class Element {
    //接受访问
    abstract fun accept(visitor: Visitor)
}


/**
 * @description 具体元素A
 */
class ConcreteElementA: Element() {
    override fun accept(visitor: Visitor) {
        visitor.visitorConcreteElementA(this)
    }
    /**
     * 具体的其他方法
     */
    fun operationA(){

    }
}


/**
 * @description 具体元素B
 */
class ConcreteElementB : Element() {
    override fun accept(visitor: Visitor) {
        visitor.visitorConcreteElementB(this)
    }

    /**
     * 具体的其他方法
     */
    fun operationB(){

    }
}


/**
 * @description 对象结构
 * 能枚举它的元素，可以提供一个高层的接口以允许访问者访问它的元素。
 */
class ObjectStructure {
    private val elements = arrayListOf<Element>()
    fun attach(element: Element) {
        elements.add(element)
    }

    fun detach(element: Element) {
        elements.remove(element)
    }

    fun accept(visitor: Visitor) {
        elements.forEach { element ->
            element.accept(visitor)
        }
    }
}

    // 测试类
class ClientVisitor {
    companion object : WithLogging() {
        @JvmStatic
        fun main(vararg args: String) {
            // 构造内部稳定的结构
            val objectStructure = ObjectStructure()
            objectStructure.attach(ConcreteElementA())
            objectStructure.attach(ConcreteElementB())

            //VisitorA的遍历访问
            objectStructure.accept(ConcreteVisitorA())
            //VisitorB的遍历访问
            objectStructure.accept(ConcreteVisitorB())

        }
    }
}
