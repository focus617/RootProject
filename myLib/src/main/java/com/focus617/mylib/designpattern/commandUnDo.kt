package com.focus617.mylib.designpattern

import com.focus617.mylib.designpattern.platform.AppStartEvent
import com.focus617.mylib.designpattern.platform.BaseObject
import com.focus617.mylib.designpattern.platform.Event
import com.focus617.mylib.logging.ILoggable
import com.focus617.mylib.logging.WithLogging


/**
 * @description CommandUndo类用来将一个命令执行封装为一个对象，并声明执行do、undo操作的接口
 */
abstract class CommandUnDo(val event: Event) : BaseObject() {
    open fun execute() {
        CommandChain.add(this)
    }

    abstract fun undo()
}

/**
 * @description 命令链，负责保存
 */
object CommandChain : ILoggable {
    private val LOG = logger()

    private val commands = ArrayList<CommandUnDo>()
    private var cursor: Int = 0

    fun add(cmd: CommandUnDo) {
        commands.add(cmd)
        cursor++
    }

    fun undo() {
        if (cursor == 0) {
            LOG.info("${this::class.java.simpleName}: nothing to undo\n")
            return
        }

        commands[cursor - 1].undo()
        cursor--
    }

    fun redo() {
        if (cursor == commands.size) {
            LOG.info("${this::class.java.simpleName}: nothing to redo\n")
            return
        }
        commands[cursor++].execute()
    }
}

// 被操作对象
object Content {
    var msg: String = "This is testing content."
}

class AppendCmd(event: Event) : CommandUnDo(event) {

    val stringToAppend = "Append_Content"

    override fun execute() {
        super.execute()
        LOG.info("${this::class.java.simpleName}.doit is called")
        Content.msg += stringToAppend
    }

    override fun undo() {
        LOG.info("${this::class.java.simpleName}.undo is called")
        Content.msg = Content.msg.substring(0, Content.msg.length - stringToAppend.length)
    }

}

class DeleteCmd(event: Event) : CommandUnDo(event) {

    // Command负责记录和保存自己操作的变化量
    var stringToDelete = ""

    override fun execute() {
        super.execute()
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

            LOG.info("Origin:" + Content.msg + '\n')
            //进行操作
            AppendCmd(event).execute()
            LOG.info(Content.msg + '\n')

            DeleteCmd(event).execute()
            LOG.info(Content.msg + '\n')

            // 测试undo
            CommandChain.undo()
            LOG.info(Content.msg + '\n')
            CommandChain.undo()
            LOG.info(Content.msg + '\n')
            CommandChain.undo()
            LOG.info(Content.msg + '\n')

            // 测试redo
            CommandChain.redo()
            LOG.info(Content.msg + '\n')
            //TODO: check why this step is failure?
            CommandChain.redo()
            LOG.info(Content.msg + '\n')
            CommandChain.redo()
            LOG.info(Content.msg + '\n')
        }
    }
}

