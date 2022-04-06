package com.focus617.mylib.designpattern

import com.focus617.mylib.designpattern.platform.BaseObject
import com.focus617.mylib.logging.WithLogging

/**
 * @description 所有具体享元类的超类，通过这个接口，Flyweight可以接受并作用于外部状态
 * @param [extrinsicState] 外部状态
 */
abstract class Flyweight : BaseObject(){
    abstract fun operation(extrinsicState: String)
}

/**
 * @description 具体的flyweight
 */
class ConcreteFlyweight(val name: String) : Flyweight() {
    override fun operation(extrinsicState: String) {
        LOG.info("${this::class.java.simpleName}-$name: $extrinsicState")
    }
}

/**
 * @description 指那些不需要共享的Flyweight子类
 */
class UnsharedConcreteFlyweight(val name: String) : Flyweight() {
    override fun operation(extrinsicState: String) {
        LOG.info("${this::class.java.simpleName}-$name: $extrinsicState")
    }
}

/**
 * @description 享元工常，用来创建并管理Flyweight对象
 * 当用户请求一个Flyweight时， FlyweightFactory对象提供一个己创建的实例,
 * 或者创建一个（如果不存在的话）。
 */
class FlyweightFactory {
    private val flyweights = hashMapOf<String, Flyweight>()

    /**
     * 初始化工厂，先生成 3个共享实例
     */
    init {
        flyweights["1"] = ConcreteFlyweight("1")
        flyweights["2"] = ConcreteFlyweight("2")
        flyweights["3"] = ConcreteFlyweight("3")
    }

    /**
     * 根据客户端请求，获得已生成的实例
     * 若对象实例还不存在，自动生成
     */
    fun getFlyweight(key: String): Flyweight {
        return flyweights[key]?: ConcreteFlyweight(key).apply {flyweights[key] = this}
    }

    /**
     * 获得网站分类总数
     */
    fun getFlyWeightCount() = flyweights.size

}

/**
 * @description 客户端类
 * 测试类
 */
class ClientFlyWeight {
    companion object : WithLogging() {
        @JvmStatic
        fun main(vararg args: String) {
            //外部状态
            val factory = FlyweightFactory()

            val fx= factory.getFlyweight("1")
            fx.operation("张三")
            fx.operation("李四")

            val fy= factory.getFlyweight("2")
            fy.operation("王五")

            val fz= factory.getFlyweight("3")
            fz.operation("王五")

            // 申请新资源
            val fw= factory.getFlyweight("4")
            fw.operation("王五")

            val fu = UnsharedConcreteFlyweight("5")
            fu.operation("王五")

            LOG.info("Total shared flyweights ... ${factory.getFlyWeightCount()}")
        }
    }
}