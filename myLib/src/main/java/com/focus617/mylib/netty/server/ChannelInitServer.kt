package com.focus617.mylib.netty.server

import com.focus617.mylib.logging.ILoggable
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelPipeline
import io.netty.channel.socket.SocketChannel
import io.netty.handler.codec.DelimiterBasedFrameDecoder
import io.netty.handler.codec.Delimiters
import io.netty.handler.codec.http.HttpObjectAggregator
import io.netty.handler.codec.http.HttpRequestDecoder
import io.netty.handler.codec.http.HttpResponseEncoder
import io.netty.handler.codec.http.HttpServerCodec
import io.netty.handler.codec.http.websocketx.WebSocketServerProtocolHandler
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import io.netty.handler.ssl.SslContext
import io.netty.handler.stream.ChunkedWriteHandler
import io.netty.handler.timeout.IdleStateHandler
import io.netty.util.CharsetUtil
import java.util.concurrent.TimeUnit

/**
 * Netty服务器的 Worker线程：Pipeline初始化程序
 * @author focus617
 * */
class ChannelInitServer(var sslCtx: SslContext?) : ChannelInitializer<SocketChannel>(), ILoggable {
    val LOG = logger()

    enum class TestServer { ChatServer, HttpServer, WebSocketServer }

    private val switch: TestServer = TestServer.HttpServer

    @Throws(Exception::class)
    public override fun initChannel(ch: SocketChannel) {
        when (switch) {
            TestServer.ChatServer -> initChatServerPipeline(ch)
            TestServer.HttpServer -> initHttpServerPipeline(ch)
            TestServer.WebSocketServer -> initWebSocketServerPipeline(ch)
        }
    }

    private fun initChatServerPipeline(ch: SocketChannel) {
        LOG.info("Setup ChatServer...")

        ch.pipeline()
            //添加心跳机制，每60s发送一次心跳
            .addLast(
                "ping",
                IdleStateHandler(
                    70, 50,
                    100, TimeUnit.SECONDS
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
        val p: ChannelPipeline = ch.pipeline()

        sslCtx?.apply {
            p.addLast(sslCtx!!.newHandler(ch.alloc())) }

        p.addLast("httpDecoder", HttpRequestDecoder())

        // Uncomment the following line if you don't want to handle HttpChunks.
        p.addLast("aggregator", HttpObjectAggregator(1048576))
        p.addLast("httpEncoder", HttpResponseEncoder())

        // Remove the following line if you don't want automatic content compression.
        //p.addLast(new HttpContentCompressor());
        p.addLast("handler", HttpSnoopServerHandler())
    }

    private fun initWebSocketServerPipeline(ch: SocketChannel) {
        LOG.info("Setup WebSocketServer...")

        ch.pipeline()
            .addLast("httpServerCodec", HttpServerCodec())
            .addLast("ChunkedWriter", ChunkedWriteHandler())
            .addLast("aggregator", HttpObjectAggregator(1048576))
            .addLast("webSocketHandShaker", WebSocketServerProtocolHandler("/ws"))
            .addLast("handler", TextWebSocketFrameHandler())
    }
}