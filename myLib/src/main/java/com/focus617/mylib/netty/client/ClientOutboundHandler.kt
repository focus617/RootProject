package com.focus617.mylib.netty.client

import com.focus617.mylib.logging.ILoggable
import com.focus617.mylib.netty.api.IfNorthBoundChannel
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelOutboundHandlerAdapter
import io.netty.channel.ChannelPromise
import io.netty.util.CharsetUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class ClientOutboundHandler : ChannelOutboundHandlerAdapter(), ILoggable {
    val LOG = logger()

    private val uiChannel: IfNorthBoundChannel? = NettyClient.getNettyClient().uiChannel

    @Throws(Exception::class)
    override fun handlerAdded(ctx: ChannelHandlerContext?) {
        ctx?.apply {
            // 定时收取北向数据，从Socket发出
            executor().schedule({
                flushData(ctx)
            }, 500, TimeUnit.MILLISECONDS)

        }
    }

    @Throws(Exception::class)
    override fun write(ctx: ChannelHandlerContext?, msg: Any?, promise: ChannelPromise?) {
        ctx?.run { flushData(ctx) }
        super.write(ctx, msg, promise)
    }

    private fun flushData(ctx: ChannelHandlerContext) {
        uiChannel?.apply {
            CoroutineScope(Dispatchers.IO).launch {
                for (myMsg in uiChannel.outboundChannel) {
                    LOG.info("OutboundHandler: $myMsg")
                    val buffer = Unpooled.copiedBuffer(myMsg + "\n", CharsetUtil.UTF_8)
                    ctx.writeAndFlush(buffer)
                }
            }
        }
    }
}