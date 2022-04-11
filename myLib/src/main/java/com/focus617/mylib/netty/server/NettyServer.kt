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
import java.net.InetSocketAddress


class NettyServer private constructor(port: Int) : WithLogging() {
    // Port where chat server will listen for connections.
    private val PORT: Int = port

    companion object {
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
            NettyServer(port).start()
        }
    }

    @Throws(Exception::class)
    fun start() {
        // Configure the server.
        val bossGroup: EventLoopGroup = NioEventLoopGroup(1)
        val workerGroup: EventLoopGroup = NioEventLoopGroup()

        try {
            val b = ServerBootstrap()
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel::class.java) // Use NIO to accept new connections.
                .localAddress(InetSocketAddress(PORT))
                .option(ChannelOption.SO_BACKLOG, 100)
                .childOption(ChannelOption.SO_KEEPALIVE, true)  //保持长连接状态
                .handler(LoggingHandler(LogLevel.INFO))
                .childHandler(ChannelInitServer())

            // Start the server.
            val f: ChannelFuture = b.bind().sync()
            LOG.info("NettyServer started and listening on port:$PORT")

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync()

        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully()
            workerGroup.shutdownGracefully()
        }
    }
}



