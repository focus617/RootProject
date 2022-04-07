package com.focus617.tankwar.netty.server

import com.focus617.mylib.logging.WithLogging
import io.netty.bootstrap.ServerBootstrap
import io.netty.channel.*
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.SocketChannel
import io.netty.channel.socket.nio.NioServerSocketChannel
import io.netty.handler.codec.string.StringDecoder
import io.netty.handler.codec.string.StringEncoder
import io.netty.handler.logging.LogLevel
import io.netty.handler.logging.LoggingHandler


object NettyServer : WithLogging() {
    private const val TAG = "NettyServer"

    // Port where chat server will listen for connections.
    private val PORT = System.getProperty("port", "8888")!!.toInt()

    @Throws(Exception::class)
    @JvmStatic
    fun main(args: Array<String>) {

        // Configure the server.
        val bossGroup: EventLoopGroup = NioEventLoopGroup(1)
        val workerGroup: EventLoopGroup = NioEventLoopGroup()
        val serverHandler = ServerHandler()

        try {
            val b = ServerBootstrap()
            b.group(bossGroup, workerGroup)
                .channel(NioServerSocketChannel::class.java) // Use NIO to accept new connections.
                .option(ChannelOption.SO_BACKLOG, 100)
                .handler(LoggingHandler(LogLevel.INFO))
                .childHandler(object : ChannelInitializer<SocketChannel>() {

                    @Throws(Exception::class)
                    public override fun initChannel(ch: SocketChannel) {
                        val p: ChannelPipeline = ch.pipeline()
                        /** the communication happens in Byte Streams through the ByteBuf interface.*/
                        p.addLast(StringDecoder())
                        p.addLast(StringEncoder())
                        p.addLast(serverHandler)
                    }
                })

            // Start the server.
            val f: ChannelFuture = b.bind(PORT).sync()
            LOG.info("${TAG}: started")

            // Wait until the server socket is closed.
            f.channel().closeFuture().sync()

        } finally {
            // Shut down all event loops to terminate all threads.
            bossGroup.shutdownGracefully()
            workerGroup.shutdownGracefully()
        }
    }
}



