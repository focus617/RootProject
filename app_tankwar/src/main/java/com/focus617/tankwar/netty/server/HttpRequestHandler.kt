package com.focus617.tankwar.netty.server

import com.focus617.mylib.logging.ILoggable
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.*
import io.netty.util.CharsetUtil
import org.json.JSONObject

/**
 * Http服务器 Handler
 * @author focus617
 * */
class HttpRequestHandler : SimpleChannelInboundHandler<HttpObject>(), ILoggable {
    val LOG = logger()

    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext?, msg: HttpObject?) {
        // 获取请求头
        val httpHeaders: HttpHeaders = (msg as HttpRequest).headers()

        // 查看请求头的详细信息
        LOG.info(httpHeaders.toString())
        // 打印uri
        LOG.info(msg.uri())

        // 获取Content-Type信息
        val strContentType = httpHeaders["Content-Type"]?.trim() ?: "null"
        LOG.info("ContentType: $strContentType")

        // 处理get请求
        val decoder = QueryStringDecoder(msg.uri())
        when (msg.method()) {

            HttpMethod.GET -> {
                LOG.info("HttpMethod=GET")
                val getParameter
                        : Map<String, List<String>> = decoder.parameters()
                LOG.info("GET：$getParameter")
                responseMessage(ctx!!)
            }

            HttpMethod.POST -> {
                LOG.info("HttpMethod=POST")
                if (strContentType.contains("application/json")) {
                    val mapReturnData: Map<String, Any> = HashMap()
                }
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

    private fun buildJSONContent(): String {
        val jsonRootObj = JSONObject()
        val jsonUserInfo = JSONObject()
        jsonUserInfo.put("id", 1)
        jsonUserInfo.put("name", "张三")
        jsonUserInfo.put("password", "123")
        jsonRootObj.put("userInfo", jsonUserInfo)

        return StringBuilder().append(jsonRootObj.toJSONArray(null)).toString()
    }

}