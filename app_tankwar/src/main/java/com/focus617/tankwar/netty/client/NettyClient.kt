package com.focus617.tankwar.netty.client

import com.focus617.mylib.logging.WithLogging
import io.netty.bootstrap.Bootstrap
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.Channel
import io.netty.channel.ChannelFuture
import io.netty.channel.ChannelOption
import io.netty.channel.EventLoopGroup
import io.netty.channel.nio.NioEventLoopGroup
import io.netty.channel.socket.nio.NioSocketChannel
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.InetSocketAddress


/**
 * Netty客户端
 * @author focus617
 * */
class NettyClient(
    private var host: String = "127.0.0.1",
    /** 连接地址 */
    private var port: Int = 8888
    /** 监听端口 */
) : WithLogging() {

    private val TAG = "NettyClient"

    /**
     * NioEventLoopGroup能够理解为一个线程池，内部维护了一组线程，
     * 每一个线程负责处理多个Channel上的事件，而一个Channel只对应于一个线程，
     * 这样能够回避多线程下的数据同步问题。
     */
    private val group: EventLoopGroup = NioEventLoopGroup()
    private val serverAddress = InetSocketAddress(host, port)
    private var channel: Channel? = null

    companion object : WithLogging() {
        @JvmStatic
        fun main(vararg args: String) {
            if (args.size != 2) {
                System.err.println("Usage: ${NettyClient::class.java.simpleName} <host> <port>")
                return
            }
            val host = args[0]
            val port = args[1].toInt()
            val client = NettyClient(host, port)
            client.startup()

            client.send("Test Message")

        }
    }

    // 重新连接的逻辑判断
    private var isConnected = false

    fun startup() {
        LOG.info("$TAG: 启动...")
        connect()
        // TODO：重新连接的逻辑是否正确？
        if (!isConnected) reconnect()
    }

    @Throws(Exception::class)
    fun connect() {
        LOG.info("$TAG: 正在链接服务器${serverAddress}...")
        try {
            /**
             * Bootstrap是开发netty客户端的辅助类，经过Bootstrap的connect方法来链接服务器端。
             * 该方法返回的也是ChannelFuture， 经此就能够判断客户端是否链接成功。
             */
            val bootstrap: Bootstrap = Bootstrap()
                .group(group)
                .channel(NioSocketChannel::class.java)
                .remoteAddress(serverAddress)
                .option(ChannelOption.TCP_NODELAY, true)
                .option(ChannelOption.SO_KEEPALIVE, true)
                .handler(ChannelInitClient())

            /**
             * A ChannelFuture starts the connection to the Server and
             * is notified when the handshake is completed.
             */
            val future: ChannelFuture = bootstrap.connect().sync()
            if (future.isSuccess) {
                LOG.info("$TAG: 成功链接到服务器")
                channel = future.channel()
                isConnected = true

            } else {
                val cause: Throwable = future.cause()
                cause.printStackTrace()
                isConnected = false
            }

            // Wait until the connection is closed.
//            channel?.closeFuture()?.sync()

            val input = BufferedReader(InputStreamReader(System.`in`))
            while(isConnected && (channel != null)) {
                channel!!.writeAndFlush(input.readLine() + "\r\n")
            }

        } catch (e: InterruptedException) {
            LOG.info("$TAG: 链接服务器出现异常Exception=${e.message}")
            e.printStackTrace()

        } finally {
            disconnect()
        }
    }

    fun disconnect() {
        LOG.info("$TAG: 链接关闭,资源释放")
        // Shut down the event loop to terminate all threads.
        group.shutdownGracefully()
        isConnected = false
        channel = null
    }

    private var reconnectNum = Int.MAX_VALUE
    private val reconnectIntervalTime: Long = 10000

    private fun reconnect() {
        while (!isConnected && reconnectNum > 0) {
            reconnectNum--
            try {
                Thread.sleep(reconnectIntervalTime);
            } catch (e: InterruptedException) {
                e.printStackTrace()
            }
            disconnect()
            LOG.info("$TAG: 重新进行连接...")
            connect()
        }
    }

    @Throws(Exception::class)
    fun send(msg: String) {
        if (!isConnected || channel == null) {
            LOG.info("$TAG: 尚未建立与服务器的连接")
            return
        }
        val buf: ByteBuf = Unpooled.copiedBuffer(msg.toByteArray())
        LOG.info("$TAG: 发送消息")
        channel!!.writeAndFlush(buf).addListener {
            LOG.info("$TAG: 发送消息成功")
        }
    }
}
