package com.focus617.tankwar.netty.server

import com.focus617.mylib.logging.ILoggable
import io.netty.channel.Channel
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent

@Sharable
class ServerHandler : SimpleChannelInboundHandler<String>(), ILoggable {
    val LOG = logger()

    @Throws(Exception::class)
    override fun handlerAdded(ctx: ChannelHandlerContext?) {
        ctx?.apply {
            val incoming: Channel = ctx.channel()
            val msg = "Client ${incoming.remoteAddress()} joined"
            LOG.info(msg)

            for (ch in channels) {
                ch.writeAndFlush("[SERVER] - $msg\n")
            }
            channels.add(incoming)
            LOG.info("total on-line client: ${channels.size} ")
        }
    }

    @Throws(Exception::class)
    override fun handlerRemoved(ctx: ChannelHandlerContext?) {
        ctx?.apply {
            val incoming: Channel = ctx.channel()
            val msg = "Client ${incoming.remoteAddress()} left"
            LOG.info(msg)

            for (ch in channels) {
                ch.writeAndFlush("[SERVER] - $msg\n")
            }
            channels.remove(incoming)
            LOG.info("total on-line client: ${channels.size} ")
        }
    }

    // read Request Message
    @Throws(Exception::class)
    public override fun channelRead0(ctx: ChannelHandlerContext, msg: String) {
        val incoming: Channel = ctx.channel()
        LOG.info("[${incoming.remoteAddress()}]: $msg")

        for (ch in channels) {
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
            if (evt.state() == IdleState.WRITER_IDLE) {
                sendHeartPkg()
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

        for (ch in channels) {
            ch.writeAndFlush("[SERVER] - $msg\n")
        }
    }

    companion object {
        val channels: MutableList<Channel> = ArrayList()
    }
}