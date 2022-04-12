package com.focus617.mylib.netty.server

import com.focus617.mylib.logging.ILoggable
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.*
import io.netty.util.CharsetUtil

/**
 * Http服务器 Handler
 * @author focus617
 * */
class HttpRequestHandler : SimpleChannelInboundHandler<HttpObject>(), ILoggable {
    val LOG = logger()

    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext?, msg: HttpObject?) {
        if ((ctx == null) || (msg == null)) {
            return
        }

        when(msg){
            is HttpRequest -> {
                val httpHeaders: HttpHeaders = msg.headers()

                // 查看请求头的详细信息
                LOG.info(httpHeaders.toString())
                // 打印uri
                LOG.info("Uri: ${msg.uri()}")
                // 获取Content-Type信息
                val strContentType = httpHeaders["Content-Type"]?.trim() ?: "null"
                LOG.info("ContentType: $strContentType")

                // 处理请求
                val decoder = QueryStringDecoder(msg.uri())
                when (msg.method()) {

                    HttpMethod.GET -> {
                        LOG.info("HttpMethod=GET")
                        val getParameter
                                : Map<String, List<String>> = decoder.parameters()
                        LOG.info("GET：$getParameter")
                        responseMessage(ctx)
                    }

                    HttpMethod.POST -> {
                        LOG.info("HttpMethod=POST")
                        if (strContentType.contains("application/json")) {
                            val mapReturnData: Map<String, Any> = HashMap()
                        }
                    }
                }
            }

            is HttpContent -> {
                val content: HttpContent = msg
                val buf: ByteBuf = content.content()
                LOG.info(buf.toString(CharsetUtil.UTF_8))
            }
        }


    }

    // 服务端回应数据
    private fun responseMessage(ctx: ChannelHandlerContext) {
        val response: FullHttpResponse =
            DefaultFullHttpResponse(HttpVersion.HTTP_1_1, HttpResponseStatus.OK)

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "application/json; charset=UTF-8")

        // 构造返回数据
        val content = buildStringContent()
        val bufResponse = StringBuilder().append(content)
        val buffer = Unpooled.copiedBuffer(bufResponse, CharsetUtil.UTF_8)
        response.content().writeBytes(buffer)
        buffer.release()

        ctx.writeAndFlush(response).addListener(ChannelFutureListener.CLOSE)
    }

    private fun buildStringContent(): String {
        return "Hello World!"
    }

}