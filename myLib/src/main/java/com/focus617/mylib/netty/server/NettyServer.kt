package com.focus617.mylib.netty.server

import com.focus617.mylib.logging.WithLogging
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelInitializer
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler
import java.net.InetSocketAddress


class NettyServer(val port: Int) : WithLogging() {
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
        val serverHandler = ServerHandler()

        try {
            val b = ServerBootstrap()
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel::class.java) // Use NIO to accept new connections.
                .localAddress(InetSocketAddress(PORT))
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(LoggingHandler(LogLevel.INFO))
                .childHandler(object : ChannelInitializer<SocketChannel>() {
                    @Throws(Exception::class)
                    public override fun initChannel(ch: SocketChannel) {
                        /** the communication happens in Byte Streams through the ByteBuf interface.*/
                        ch.pipeline().addLast(StringDecoder(), StringEncoder(), serverHandler)
                    }
                })

            // Start the server.
            val f: ChannelFuture = b.bind().sync()
            LOG.info(
                "${NettyServer::class.java.simpleName}: started and listen on "
                        + f.channel().localAddress()
            );

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync()

        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully()
            workerGroup.shutdownGracefully()
        }
    }
}



