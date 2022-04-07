package com.focus617.tankwar.netty.client

import com.focus617.mylib.logging.ILoggable
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelPipeline
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import io.netty.handler.timeout.IdleStateHandler
import java.util.concurrent.TimeUnit

/**
 * Netty客户端数据收发线程
 * @author focus617
 * */
class ChannelInitClient : ChannelInitializer<SocketChannel>(), ILoggable {
    val LOG = logger()

    @Throws(Exception::class)
    override fun initChannel(ch: SocketChannel) {
        val pipeline: ChannelPipeline = ch.pipeline()

        // 使用 pipeline.addLast()添加 Decoder、Encode和 Handler对象
        pipeline.addLast(StringDecoder())
        pipeline.addLast(StringEncoder())

        //添加心跳机制，例：每3000ms发送一次心跳
        pipeline.addLast(
            IdleStateHandler(
                3000, 3000,
                3000, TimeUnit.MILLISECONDS
            )
        )
        //添加自己的Handler，进行数据处理（接收、发送）
        pipeline.addLast(ClientHandler())
    }
}