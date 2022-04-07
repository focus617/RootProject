package com.focus617.tankwar.netty.server

import com.focus617.mylib.logging.ILoggable
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandler.Sharable
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.ChannelInboundHandlerAdapter
import io.netty.util.CharsetUtil

@Sharable
class EchoServerHandler : ChannelInboundHandlerAdapter(), ILoggable {
    val LOG = logger()

    /**
     * 因为需要将入站的信息返回给发送者，由于 write() 是异步的，在 channelRead() 返回时，可能还没有完成。
     * 所以，我们使用 ChannelInboundHandlerAdapter,无需释放 msg。
     * 最后在 channelReadComplete() 我们调用 ctxWriteAndFlush() 来释放 msg。
     */
    override fun channelRead(ctx: ChannelHandlerContext, msg: Any) {
        val byteBuf: ByteBuf = msg as ByteBuf
        LOG.info("Server received: ${byteBuf.toString(CharsetUtil.UTF_8)}")
        ctx.write(msg)
    }

    /** implement flush */
    override fun channelReadComplete(ctx: ChannelHandlerContext) {
        ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
            .addListener(ChannelFutureListener.CLOSE)
    }

    override fun exceptionCaught(ctx: ChannelHandlerContext, cause: Throwable) {
        // Close the connection when an exception is raised.
        cause.printStackTrace()
        ctx.close()
    }
}