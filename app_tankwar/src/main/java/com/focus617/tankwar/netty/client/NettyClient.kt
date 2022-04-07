package com.focus617.tankwar.netty.client

import com.focus617.mylib.logging.WithLogging
import io.netty.bootstrap.Bootstrap
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import java.net.InetSocketAddress


/**
 * Netty客户端
 * @author focus617
 * */
class NettyClient(
    private var host: String = "127.0.0.1", /** 连接地址 */
    private var port: Int = 8888            /** 监听端口 */
) : WithLogging() {
    private val TAG = "NettyClient"

    companion object {
        @JvmStatic
        fun main(vararg args: String) {
            if (args.size != 2) {
                System.err.println("Usage: ${NettyClient::class.java.simpleName} <host> <port>")
                return
            }
            val host = args[0]
            val port = args[1].toInt()
            NettyClient(host, port).start()
        }
    }

    @Throws(Exception::class)
    fun start() {
        val serverAddress = InetSocketAddress(host, port)
        LOG.info("$TAG: 正在链接服务器${serverAddress}")

        /**
         * NioEventLoopGroup能够理解为一个线程池，内部维护了一组线程，
         * 每一个线程负责处理多个Channel上的事件，而一个Channel只对应于一个线程，
         * 这样能够回避多线程下的数据同步问题。
         */
        val group: EventLoopGroup = NioEventLoopGroup()

        try {
            /**
             * Bootstrap是开发netty客户端的辅助类，经过Bootstrap的connect方法来链接服务器端。
             * 该方法返回的也是ChannelFuture， 经此就能够判断客户端是否链接成功。
             */
            val bootstrap: Bootstrap = Bootstrap()

            bootstrap.group(group)
                .channel(NioSocketChannel::class.java)
                .remoteAddress(serverAddress)
                .handler(ChannelInitClient())

            /**
             * A ChannelFuture starts the connection to the Server and
             * is notified when the handshake is completed.
             */
            val future: ChannelFuture = bootstrap.connect().sync()

            if (future.isSuccess) {
                LOG.info("$TAG: 成功链接到服务器")
                val channel: Channel = future.sync().channel()

                val input = "Frank"
                channel.writeAndFlush(input)
                channel.flush()

            } else {
                val cause: Throwable = future.cause()
                cause.printStackTrace()
                // TODO：重新连接
            }

            // Wait until the connection is closed.
            future.channel().closeFuture().sync()

        } catch (e: InterruptedException) {
            LOG.info("$TAG: 链接服务器出现异常Exception=${e.message}")
            e.printStackTrace()

        } finally {
            // Shut down the event loop to terminate all threads.
            group.shutdownGracefully()
            LOG.info("$TAG: 链接关闭,资源释放")
        }

    }
}
