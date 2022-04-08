package com.focus617.tankwar.netty.client

import com.focus617.mylib.logging.ILoggable
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelPipeline
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.DelimiterBasedFrameDecoder
import io.netty.handler.codec.Delimiters
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder

/**
 * Netty客户端数据收发线程
 * @author focus617
 * */
class ChannelInitClient : ChannelInitializer<SocketChannel>(), ILoggable {
    val LOG = logger()

    @Throws(Exception::class)
    override fun initChannel(ch: SocketChannel) {
        val pipeline: ChannelPipeline = ch.pipeline()

        //TODO: 添加心跳机制，每3000ms发送一次心跳

        pipeline.addLast(
            "framer",
            DelimiterBasedFrameDecoder(8192, *Delimiters.lineDelimiter())
        )
        // 使用 pipeline.addLast()添加 Decoder、Encode和 Handler对象
        pipeline.addLast("decoder", StringDecoder())
        pipeline.addLast("encoder", StringEncoder())

        //添加自己的Handler，进行数据处理（接收、发送）
        pipeline.addLast("handler", ClientHandler())
    }
}