package com.focus617.mylib.designpattern

import com.focus617.mylib.designpattern.platform.BaseObject
import com.focus617.mylib.logging.WithLogging

/**
 * @description 抽象中介者
 */
abstract class Mediator : BaseObject() {
    /**
     * 定义一个抽象的转发消息的方法，得到同事对象和转发消息
     */
    abstract fun forward(msg: String, sourceColleague: Colleague)
}

/**
 * @description 抽象同事类
 * 构造方法，得到中介者对象 [mediator]
 */
abstract class Colleague(private val mediator: Mediator) : BaseObject() {
    /**
     * 发送消息
     */
    abstract fun send(msg: String)

    /**
     * 接收消息
     */
    abstract fun receive(msg: String)
}

/**
 * @description 具体的中介者
 */
class ConcreteMediator : Mediator() {
    var colleagueA: Colleague? = null
    var colleagueB: Colleague? = null

    /**
     * 判断消息的来源，并转发给对应的同事
     */
    override fun forward(msg: String, sourceColleague: Colleague) {
        when (sourceColleague) {
            colleagueA -> {
                colleagueB?.receive(msg)
            }
            colleagueB -> {
                colleagueA?.receive(msg)
            }
        }
    }
}

/**
 * @description 具体同事A
 */
class ConcreteColleagueA(private val mediator: Mediator) : Colleague(mediator) {

    /**
     * 发送消息通过中介者进行转发
     */
    override fun send(msg: String) {
        LOG.info("${this::class.java.simpleName}:发送消息--->$msg")
        mediator.forward(msg, this)
    }

    override fun receive(msg: String) {
        LOG.info("${this::class.java.simpleName}:收到消息--->$msg")
    }
}

/**
 * @description 具体同事B
 */
class ConcreteColleagueB(private val mediator: Mediator) : Colleague(mediator) {

    /**
     * 发送消息通过中介者进行转发
     */
    override fun send(msg: String) {
        LOG.info("${this::class.java.simpleName}:发送消息--->$msg")
        mediator.forward(msg, this)
    }

    override fun receive(msg: String) {
        LOG.info("${this::class.java.simpleName}:收到消息--->$msg")
    }
}

/**
 * @description 客户端类
 * 测试类
 */
class ClientMediator {
    companion object : WithLogging() {
        @JvmStatic
        fun main(vararg args: String) {
            //初始化
            val mediator = ConcreteMediator()
            val colleagueA = ConcreteColleagueA(mediator)//让通过具体的同事类都安装有wechat这个中介
            val colleagueB = ConcreteColleagueB(mediator)
            mediator.colleagueA = colleagueA//让 wechat 指定这两个同事的存在
            mediator.colleagueB = colleagueB

            colleagueA.send("在干嘛呢？")
            colleagueB.send("在发呆呀！")

        }
    }
}