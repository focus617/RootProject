package com.focus617.mylib.designpattern

import com.focus617.mylib.logging.WithLogging

/**
 * @description 发起人，负责创建备忘录，用以记录当前的对象状态，并可以使用该备忘录恢复状态。
 */
class Originator {
    private var state: String = "on"//初始状态
    private var caretaker: Caretaker = Caretaker()

    /**
     * 设置状态
     */
    fun setState(state: String) {
        this.state = state
    }

    /**
     * 显示当前的状态
     */
    fun showState() {
        println("state ---> $state")
    }

    /**
     * 创建备忘录，用来保存当前状态，以便恢复
     */
    fun createMemento() {
        caretaker.setMemento(Memento(state))
    }

    /**
     * 恢复状态
     */
    fun recoveryState() {
        this.state = caretaker.getMemento()?.state ?: state
    }
}

/**
 * @description 备忘录，用来存储Originator需要保存的状态。
 */
class Memento(val state: String)

/**
 * @description 备忘录管理者，负责存取备忘录。
 */
class Caretaker {
    private var memento: Memento? = null

    fun setMemento(memento: Memento) {
        this.memento = memento
    }

    fun getMemento(): Memento? {
        return memento
    }
}

// 测试类
class ClientMemento {
    companion object : WithLogging() {
        @JvmStatic
        fun main(vararg args: String) {
            //创建一个发起人
            val originator = Originator()
            //改变初始状态
            originator.setState("off")
            //创建备忘录，保存当前状态
            originator.createMemento()
            //改变状态
            originator.setState("on")
            //确认状态
            originator.showState()
            //突然想恢复状态
            originator.recoveryState()
            //输出
            originator.showState()

        }
    }
}