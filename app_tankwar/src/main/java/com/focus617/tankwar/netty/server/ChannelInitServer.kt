package com.focus617.tankwar.netty.server

import com.focus617.mylib.logging.ILoggable
import io.netty.channel.ChannelInitializer
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.DelimiterBasedFrameDecoder
import io.netty.handler.codec.Delimiters
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import io.netty.handler.timeout.IdleStateHandler
import io.netty.util.CharsetUtil
import java.util.concurrent.TimeUnit

/**
 * Netty服务器的 Worker线程：Pipeline初始化程序
 * @author focus617
 * */
class ChannelInitServer : ChannelInitializer<SocketChannel>(), ILoggable {
    val LOG = logger()

    enum class TestServer { ChatServer, HttpServer }

    private val switch: TestServer = TestServer.ChatServer

    @Throws(Exception::class)
    public override fun initChannel(ch: SocketChannel) {
        when (switch) {
            TestServer.ChatServer -> initChatServerPipeline(ch)
            TestServer.HttpServer -> initHttpServerPipeline(ch)
        }
    }

    private fun initChatServerPipeline(ch: SocketChannel) {
        LOG.info("Setup ChatServer...")

        ch.pipeline()
            //添加心跳机制，每60s发送一次心跳
            .addLast(
                "ping",
                IdleStateHandler(
                    60, 60,
                    60, TimeUnit.SECONDS
                )
            )
            .addLast(
                "framer",
                DelimiterBasedFrameDecoder(8192, *Delimiters.lineDelimiter())
            )
            .addLast("decoder", StringDecoder(CharsetUtil.UTF_8))
            .addLast("encoder", StringEncoder(CharsetUtil.UTF_8))
            .addLast("handler", ChatServerHandler())
    }


    private fun initHttpServerPipeline(ch: SocketChannel) {
        LOG.info("Setup HttpServer...")

        ch.pipeline()
            .addLast("httpServerCodec", HttpServerCodec())
            .addLast("aggregator", HttpObjectAggregator(1048576))
            .addLast("handler", HttpRequestHandler())
    }
}