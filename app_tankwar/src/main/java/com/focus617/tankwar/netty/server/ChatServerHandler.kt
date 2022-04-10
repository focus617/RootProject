package com.focus617.tankwar.netty.server

import com.focus617.mylib.logging.ILoggable
import io.netty.channel.Channel
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.channel.group.ChannelGroup
import io.netty.channel.group.DefaultChannelGroup
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent
import io.netty.util.concurrent.GlobalEventExecutor

@Sharable
class ChatServerHandler : SimpleChannelInboundHandler<String>(), ILoggable {
    val LOG = logger()

    @Throws(Exception::class)
    override fun handlerAdded(ctx: ChannelHandlerContext?) {
        ctx?.apply {
            val incoming: Channel = ctx.channel()
            val msg = "Client ${incoming.remoteAddress()} joined"
            LOG.info(msg)

            channelGroup.writeAndFlush("[SERVER] - $msg\n")
            channelGroup.add(incoming)
            LOG.info("total on-line client: ${channelGroup.size} ")
        }
    }

    @Throws(Exception::class)
    override fun handlerRemoved(ctx: ChannelHandlerContext?) {
        ctx?.apply {
            val incoming: Channel = ctx.channel()
            val msg = "Client ${incoming.remoteAddress()} left"
            LOG.info(msg)

            channelGroup.writeAndFlush("[SERVER] - $msg\n")

            // 下面这个步骤可以省略，因为channelGroup会自动完成
//            channelGroup.remove(incoming)
            LOG.info("total on-line client: ${channelGroup.size} ")
        }
    }

    // read Request Message
    @Throws(Exception::class)
    public override fun channelRead0(ctx: ChannelHandlerContext, msg: String) {
        val incoming: Channel = ctx.channel()
        LOG.info("[${incoming.remoteAddress()}]: $msg")

        channelGroup.forEach { ch ->
            if (ch != incoming) {
                ch.writeAndFlush("[${incoming.remoteAddress()}]: $msg\n")
            } else {
                ch.writeAndFlush("[You]: $msg\n")
            }
        }
    }

    @Throws(Exception::class)
    override fun channelActive(ctx: ChannelHandlerContext?) {
        ctx?.apply {
            val incoming: Channel = ctx.channel()
            LOG.info("${incoming.remoteAddress()} online")

            ctx.writeAndFlush("[SERVER] - Welcome!\n")
        }
    }

    @Throws(Exception::class)
    override fun channelInactive(ctx: ChannelHandlerContext?) {
        ctx?.apply {
            val incoming: Channel = ctx.channel()
            LOG.info("${incoming.remoteAddress()} offline")
        }
    }

    // Handle Exception
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        val incoming: Channel = ctx.channel()
        LOG.info("connection to ${incoming.remoteAddress()} abnormal")
        // 当出现异常就关闭连接
        cause.printStackTrace()
        ctx.close()
    }

    @Throws(Exception::class)
    override fun userEventTriggered(ctx: ChannelHandlerContext?, evt: Any?) {
        if ((ctx != null) && (evt is IdleStateEvent)) {
            LOG.info("${ctx.channel().remoteAddress()} Trigger Idle Event(State=${evt.state()})")

            when (evt.state()) {
                IdleState.WRITER_IDLE -> sendHeartPkg()
                // 在规定时间内没有收到客户端的上行数据, 主动断开连接
                IdleState.READER_IDLE -> ctx.disconnect()
                IdleState.ALL_IDLE -> {}
                null -> {}
            }

        } else {
            super.userEventTriggered(ctx, evt)
        }
    }

    /**
     * 发送心跳
     */
    private fun sendHeartPkg() {
        val msg = "Send heartbeat package"
        channelGroup.writeAndFlush("[SERVER] - $msg\n")
    }

    companion object {
        //      val channelGroup: MutableList<Channel> = ArrayList()
        val channelGroup: ChannelGroup = DefaultChannelGroup(GlobalEventExecutor.INSTANCE)
    }
}