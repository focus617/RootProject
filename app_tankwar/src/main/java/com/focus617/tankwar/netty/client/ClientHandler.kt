package com.focus617.tankwar.netty.client

import com.focus617.mylib.logging.ILoggable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler


class ClientHandler : SimpleChannelInboundHandler<String>(), ILoggable {
    val LOG = logger()

    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext, msg: String) {
        LOG.info("Client received: $msg")
    }

}