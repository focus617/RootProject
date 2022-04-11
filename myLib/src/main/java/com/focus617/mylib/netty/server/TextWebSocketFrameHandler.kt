package com.focus617.mylib.netty.server

import com.focus617.mylib.logging.ILoggable
import io.netty.channel.Channel
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.websocketx.TextWebSocketFrame
import java.time.LocalDateTime

/**
 * Http服务器 Handler
 * @author focus617
 * */
class TextWebSocketFrameHandler : SimpleChannelInboundHandler<TextWebSocketFrame>(), ILoggable {
    val LOG = logger()

    @Throws(Exception::class)
    override fun handlerAdded(ctx: ChannelHandlerContext?) {
        ctx?.apply {
            LOG.info("Handler Added: ${ctx.channel()?.id()?.asLongText()}")
        }
    }

    @Throws(Exception::class)
    override fun handlerRemoved(ctx: ChannelHandlerContext?) {
        ctx?.apply {
            LOG.info("Handler Removed: ${ctx.channel()?.id()?.asLongText()}")
        }
    }

    // Handle Exception
    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        val incoming: Channel = ctx.channel()
        LOG.info("connection to ${incoming.remoteAddress()} broken")
        // 当出现异常就关闭连接
//        cause.printStackTrace()
        ctx.close()
    }

    override fun channelRead0(ctx: ChannelHandlerContext?, msg: TextWebSocketFrame?) {
        if ((ctx == null) || (msg == null)) {
            return
        }
        LOG.info("received msg: ${msg.text()}")

        responseMessage(ctx)

    }

    // 服务端回应数据
    private fun responseMessage(ctx: ChannelHandlerContext) {
        val response = TextWebSocketFrame("Server Time: ${LocalDateTime.now()}")
        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
    }


}