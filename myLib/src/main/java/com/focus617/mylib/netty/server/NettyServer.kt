package com.focus617.mylib.netty.server

import com.focus617.mylib.logging.WithLogging
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import io.netty.handler.ssl.SslContext
import io.netty.handler.ssl.SslContextBuilder
import io.netty.handler.ssl.util.SelfSignedCertificate
import java.net.InetSocketAddress

/**
 * Netty服务器端
 * @author focus617
 * */
class NettyServer private constructor() : WithLogging() {
    // SSL for HttpServer
    private var enableSSL: Boolean = true

    // Port where chat server will listen for connections.
    private var port: Int = 8888

    companion object ServerBuilder : WithLogging() {
        private val TAG = "NettyServer"
        private val server = NettyServer()

        @Throws(Exception::class)
        @JvmStatic
        fun main(args: Array<String>) {
            val port: Int = when (args.size) {
                0 -> System.getProperty("port", "8888")!!.toInt()
                1 -> args[0].toInt()
                else -> {
                    System.err.println("Usage: ${NettyServer::class.java.simpleName} <port>")
                    return
                }
            }
            startup(port)
        }

        fun startup(port: Int): NettyServer {
            LOG.info("${TAG}: NettyServer start...")
            server.port = port

            // 需要在子线程中发起连接
            Thread(ThreadGroup("Netty-Server")) {
                server.start()
            }.start()

            return server
        }

        fun getNettyServer(): NettyServer = server

    }

    @Throws(Exception::class)
    private fun start() {
        // Configure SSL
        val sslCtx: SslContext? =
            if (enableSSL) {
                val ssc = SelfSignedCertificate()
                SslContextBuilder.forServer(ssc.certificate(), ssc.privateKey()).build();
            } else {
                null
            }

        // Configure the server.
        val bossGroup: EventLoopGroup = NioEventLoopGroup(1)
        val workerGroup: EventLoopGroup = NioEventLoopGroup()

        try {
            val b = ServerBootstrap()
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel::class.java) // Use NIO to accept new connections.
                .localAddress(InetSocketAddress(port))
                .option(ChannelOption.SO_BACKLOG, 100)
                .childOption(ChannelOption.SO_KEEPALIVE, true)  //保持长连接状态
                .handler(LoggingHandler(LogLevel.INFO))
                .childHandler(ChannelInitServer(sslCtx))

            // Start the server.
            val f: ChannelFuture = b.bind().sync()
            LOG.info("NettyServer started and listening on port:$port")

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync()

        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully()
            workerGroup.shutdownGracefully()
        }
    }
}



