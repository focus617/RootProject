package com.focus617.tankwar.netty.client

import com.focus617.mylib.logging.ILoggable
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.DelimiterBasedFrameDecoder
import io.netty.handler.codec.Delimiters
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import io.netty.handler.timeout.IdleStateHandler
import io.netty.util.CharsetUtil
import java.util.concurrent.TimeUnit

/**
 * Netty客户端数据收发线程
 * @author focus617
 * */
class ChannelInitClient : ChannelInitializer<SocketChannel>(), ILoggable {
    val LOG = logger()

    enum class TestClient { ChatClient, WebSocketClient }

    private val switch: TestClient = TestClient.ChatClient

    @Throws(Exception::class)
    override fun initChannel(ch: SocketChannel) {
        when (switch) {
            TestClient.ChatClient -> initChatClientPipeline(ch)
            TestClient.WebSocketClient -> initChatClientPipeline(ch)
        }
    }

    private fun initChatClientPipeline(ch: SocketChannel) {
        LOG.info("Setup ChatClient...")
        ch.pipeline()
            //添加心跳机制，每60s发送一次心跳
            .addLast(
                "ping",
                IdleStateHandler(
                    40, 50,
                    90, TimeUnit.SECONDS
                )
            )
            .addLast(
                "framer",
                DelimiterBasedFrameDecoder(8192, *Delimiters.lineDelimiter())
            )
            // 使用 pipeline.addLast()添加 Decoder、Encode和 Handler对象
            .addLast("decoder", StringDecoder(CharsetUtil.UTF_8))
            .addLast("encoder", StringEncoder(CharsetUtil.UTF_8))

            //添加自己的Handler，进行数据处理（接收、发送）
            .addLast("handler", ClientHandler())
    }
}