package com.focus617.tankwar.netty.server

import com.focus617.mylib.logging.ILoggable
import io.netty.channel.Channel
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler

@Sharable
class ServerHandler : SimpleChannelInboundHandler<String>(), ILoggable {
    val LOG = logger()

    override fun channelActive(ctx: ChannelHandlerContext) {
        LOG.info("Client joined - $ctx")
        channels.add(ctx.channel())
    }

    // read Request Message
    @Throws(java.lang.Exception::class)
    public override fun channelRead0(ctx: ChannelHandlerContext, msg: String) {
        LOG.info("Message received: $msg")
        for (c in channels) {
            c.writeAndFlush("Hello $msg\n")
        }
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext?) {
        // implement flush
    }

    // Handle Exception
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        LOG.info("Closing connection for client - $ctx")
        ctx.close()
    }

    companion object {
        val channels: MutableList<Channel> = ArrayList()
    }
}