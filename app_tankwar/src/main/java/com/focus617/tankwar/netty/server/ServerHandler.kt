package com.focus617.tankwar.netty.server

import com.focus617.mylib.logging.ILoggable
import io.netty.channel.Channel
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

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
                ch.writeAndFlush("[SERVER] - $msg")
            }
            channels.add(incoming)
            LOG.info("total client: ${channels.size} ")
        }
    }

    @Throws(Exception::class)
    override fun handlerRemoved(ctx: ChannelHandlerContext?) {
        ctx?.apply {
            val incoming: Channel = ctx.channel()
            val msg = "Client ${incoming.remoteAddress()} left"
            LOG.info(msg)

            for (ch in channels) {
                ch.writeAndFlush("[SERVER] - $msg")
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
                ch.writeAndFlush("[${incoming.remoteAddress()}]: $msg")
            } else {
                ch.writeAndFlush("[You]: $msg")
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
        cause.printStackTrace()
        LOG.info("Closing connection for client - ${incoming.remoteAddress()}")
        ctx.close()
    }

    companion object {
        val channels: MutableList<Channel> = ArrayList()
    }
}