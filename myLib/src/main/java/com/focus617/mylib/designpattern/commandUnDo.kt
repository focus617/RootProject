package com.focus617.mylib.designpattern

import com.focus617.mylib.designpattern.platform.AppStartEvent
import com.focus617.mylib.designpattern.platform.BaseObject
import com.focus617.mylib.designpattern.platform.Event
import com.focus617.mylib.logging.ILoggable
import com.focus617.mylib.logging.WithLogging


/**
 * @description CommandUndo类用来将一个命令执行封装为一个对象，并声明执行do、undo操作的接口
 */
abstract class CommandUnDo {
    open fun doit(event: Event) {
        CommandChain.add(this)
    }

    abstract fun undo()
}

/**
 * @description 另一种责任链实现方式：具有统一的处理者，
 * 将请求依次传递给持有的处理者
 * 处理者负责如果可处理该请求，就处理之，否则返回不能处理的通知；
 * 再由本类将该请求转发给它的后继者。
 */
object CommandChain : ILoggable {
    private val LOG = logger()
    private val commands = ArrayList<CommandUnDo>()

    fun add(cmd: CommandUnDo) {
        commands.add(cmd)
    }

    fun undo() {
        if (commands.size == 0) {
            LOG.info("${this::class.java.simpleName}: nothing to undo\n")
            return
        }

        val cmd = commands[commands.size - 1]
        cmd.undo()
        commands.remove(cmd)
    }
}

// 被操作对象
object Content{
    var msg: String = "This is testing content."
}

class AppendCmd() : CommandUnDo() {
    companion object : WithLogging()

    val stringToAppend = "Append_Content"

    override fun doit(event: Event) {
        super.doit(event)
        LOG.info("${this::class.java.simpleName}.doit is called")
        Content.msg += stringToAppend
    }

    override fun undo() {
        LOG.info("${this::class.java.simpleName}.undo is called")
        Content.msg = Content.msg.substring(0, Content.msg.length - stringToAppend.length)
    }

}

class DeleteCmd() : CommandUnDo() {
    companion object : WithLogging()

    // Command负责记录和保存自己操作的变化量
    var stringToDelete = ""

    override fun doit(event: Event) {
        super.doit(event)
        LOG.info("${this::class.java.simpleName}.doit is called")
        stringToDelete = Content.msg
        Content.msg = ""
    }

    override fun undo() {
        LOG.info("${this::class.java.simpleName}.undo is called")
        Content.msg = stringToDelete
    }

}

/**
 * @description Invoker类 要求该Command执行这个请求
 */
class InvokerUndo : BaseObject() {
    companion object : WithLogging()
}

/**
 * @description Client 客户端
 */
class ClientCommandUndoTesting {
    companion object : WithLogging() {
        @JvmStatic
        fun main(vararg arg: String) {
            // 构造测试条件
            val invoker = InvokerUndo()
            val event = AppStartEvent(invoker)

            LOG.info(Content.msg + '\n')
            //进行操作
            AppendCmd().doit(event)
            LOG.info(Content.msg + '\n')

            DeleteCmd().doit(event)
            LOG.info(Content.msg + '\n')

            // 测试undo
            CommandChain.undo()
            LOG.info(Content.msg + '\n')
            CommandChain.undo()
            LOG.info(Content.msg + '\n')
            CommandChain.undo()
            LOG.info(Content.msg + '\n')
        }
    }
}

