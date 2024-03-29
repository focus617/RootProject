package com.focus617.mylib.netty.client

import com.focus617.mylib.helper.DateHelper
import com.focus617.mylib.logging.ILoggable
import com.focus617.mylib.netty.api.IfNorthBoundChannel
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.timeout.IdleState
import io.netty.handler.timeout.IdleStateEvent
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/** SimpleChannelInboundHandler 会自动释放资源，而无需存储任何信息的引用。*/
class ClientInboundHandler : SimpleChannelInboundHandler<String>(), ILoggable {
    val LOG = logger()

    private val uiChannel: IfNorthBoundChannel? = NettyClient.getNettyClient().uiChannel

    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext, msg: String) {
        LOG.info(msg)

        val data: String = StringBuilder().append(msg).toString()

        CoroutineScope(Dispatchers.IO).launch {
            uiChannel?.inboundChannel?.send(data)
        }
    }

    override fun channelInactive(ctx: ChannelHandlerContext?) {
        ctx?.apply {
            LOG.info("Client disconnected from ${ctx.channel().remoteAddress()}")
        }
    }

    /**
     * 自动重连逻辑
     */
    override fun channelUnregistered(ctx: ChannelHandlerContext?) {
        ctx?.apply {
            val RECONNECT_DELAY = 2000L
            LOG.info("Client plan to reconnect to server in ${RECONNECT_DELAY/1000}s")

            channel().eventLoop().schedule(Runnable {
                NettyClient.startup()
//                NettyClient.getNettyClient().connect()
            }, RECONNECT_DELAY, TimeUnit.MILLISECONDS)
        }
    }

    /**
     * 超时逻辑
     */
    @Throws(Exception::class)
    override fun userEventTriggered(ctx: ChannelHandlerContext?, evt: Any?) {
        if ((ctx != null) && (evt is IdleStateEvent)) {
            if (evt.state() == IdleState.WRITER_IDLE) {
                // The connection was OK but there was no traffic for last period.
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
        val msg = "Heartbeat Handshake: ${DateHelper.nowAsStr()}"
        ctx.channel().writeAndFlush("$msg\n")
    }

}