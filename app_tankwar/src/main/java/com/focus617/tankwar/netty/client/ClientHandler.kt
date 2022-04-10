package com.focus617.tankwar.netty.client

import com.focus617.mylib.logging.ILoggable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent

/** SimpleChannelInboundHandler 会自动释放资源，而无需存储任何信息的引用。*/
class ClientHandler : SimpleChannelInboundHandler<String>(), ILoggable {
    val LOG = logger()

    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext, msg: String) {
        LOG.info("$msg")
    }

    @Throws(Exception::class)
    override fun userEventTriggered(ctx: ChannelHandlerContext?, evt: Any?) {
        if ((ctx != null) && (evt is IdleStateEvent)) {
            if (evt.state() == IdleState.WRITER_IDLE) {
                sendHeartPkg(ctx)
            }
        } else {
            super.userEventTriggered(ctx, evt)
        }
    }

    /**
     * 发送心跳
     */
    private fun sendHeartPkg(ctx: ChannelHandlerContext) {
        val msg = "Send heartbeat package"
        ctx.channel().writeAndFlush("$msg\n")
    }

}