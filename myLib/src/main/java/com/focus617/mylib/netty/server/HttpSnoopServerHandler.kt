package com.focus617.mylib.netty.server

import com.focus617.mylib.logging.ILoggable
import io.netty.buffer.ByteBuf
import io.netty.buffer.Unpooled
import io.netty.channel.ChannelFutureListener
import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleChannelInboundHandler
import io.netty.handler.codec.http.*
import io.netty.handler.codec.http.HttpResponseStatus.BAD_REQUEST
import io.netty.handler.codec.http.HttpResponseStatus.OK
import io.netty.handler.codec.http.cookie.Cookie
import io.netty.handler.codec.http.cookie.ServerCookieDecoder
import io.netty.handler.codec.http.cookie.ServerCookieEncoder
import io.netty.util.CharsetUtil


/**
 * Http服务器 Handler
 * @author focus617
 * */
class HttpSnoopServerHandler : SimpleChannelInboundHandler<HttpObject>(), ILoggable {
    val LOG = logger()

    private var request: HttpRequest? = null

    /** Buffer that stores the response content */
    private val buf = StringBuilder()

    override fun exceptionCaught(ctx: ChannelHandlerContext?, cause: Throwable?) {
        cause?.printStackTrace()
        ctx?.close()
    }

    override fun channelReadComplete(ctx: ChannelHandlerContext?) {
        ctx?.flush()
    }

    @Throws(Exception::class)
    override fun channelRead0(ctx: ChannelHandlerContext?, msg: HttpObject?) {
        if ((ctx == null) || (msg == null)) {
            return
        }

        when (msg) {
            is HttpRequest -> processHttpRequest(msg, ctx)
            is HttpContent -> processHttpContent(msg, ctx)
        }
    }

    private fun processHttpRequest(msg: HttpRequest, ctx: ChannelHandlerContext) {
        val request: HttpRequest = msg.also { request = it }

        if (HttpUtil.is100ContinueExpected(request)) {
            send100Continue(ctx)
        }

        buf.setLength(0)
        buf.append("WELCOME TO THE WILD WILD WEB SERVER\r\n")
        buf.append("===================================\r\n")

        buf.append("VERSION: ").append(request.protocolVersion()).append("\r\n")
        buf.append("HOSTNAME: ").append(
            request.headers()
                .get(HttpHeaderNames.HOST, "unknown")
        ).append("\r\n")
        buf.append("REQUEST_URI: ").append(request.uri()).append("\r\n\r\n")


        val headers: HttpHeaders = request.headers()
        if (!headers.isEmpty) {
            for ((key, value) in headers) {
                buf.append("HEADER: ").append(key).append(" = ").append(value)
                    .append("\r\n")
            }
            buf.append("\r\n")
        }

        val queryStringDecoder = QueryStringDecoder(request.uri())
        val params: Map<String, List<String>> = queryStringDecoder.parameters()
        if (params.isNotEmpty()) {
            for ((key, value) in params) {
                buf.append("PARAM: ").append(key).append(" = ").append(value).append("\r\n")
            }
            buf.append("\r\n")
        }
        appendDecoderResult(buf, request)
    }

    private fun processHttpContent(msg: HttpContent, ctx: ChannelHandlerContext) {
        val httpContent: HttpContent = msg

        val content: ByteBuf = httpContent.content()
        if (content.isReadable) {
            buf.append("CONTENT: ")
            buf.append(content.toString(CharsetUtil.UTF_8))
            buf.append("\r\n")
            appendDecoderResult(buf, request!!)
        }

        if (msg is LastHttpContent) {
            buf.append("END OF CONTENT\r\n")
            val trailer: LastHttpContent = msg
            if (!trailer.trailingHeaders().isEmpty) {
                buf.append("\r\n")
                for (name in trailer.trailingHeaders().names()) {
                    for (value in trailer.trailingHeaders().getAll(name)) {
                        buf.append("TRAILING HEADER: ")
                        buf.append(name).append(" = ").append(value).append("\r\n")
                    }
                }
                buf.append("\r\n")
            }

            if (!writeResponse(trailer, ctx)) {
                // If keep-alive is off, close the connection once the content is fully written.
                ctx.writeAndFlush(Unpooled.EMPTY_BUFFER)
                    .addListener(ChannelFutureListener.CLOSE)
            }
        }
    }

    // 服务端回应数据
    private fun writeResponse(currentObj: HttpObject, ctx: ChannelHandlerContext): Boolean {
        // Decide whether to close the connection or not.
        val keepAlive = HttpUtil.isKeepAlive(request)

        // Build the response object.
        val response: FullHttpResponse =
            DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1,
                if (currentObj.decoderResult().isSuccess) OK else BAD_REQUEST,
                Unpooled.copiedBuffer(buf.toString(), CharsetUtil.UTF_8)
            )

        response.headers().set(HttpHeaderNames.CONTENT_TYPE, "text/plain; charset=UTF-8")

        // 构造返回数据
        if (keepAlive) {
            // Add 'Content-Length' header only for a keep-alive connection.
            response.headers()
                .setInt(HttpHeaderNames.CONTENT_LENGTH, response.content().readableBytes())
            // Add keep alive header as per:
            // - https://www.w3.org/Protocols/HTTP/1.1/draft-ietf-http-v11-spec-01.html#Connection
            response.headers().set(HttpHeaderNames.CONNECTION, HttpHeaderValues.KEEP_ALIVE)
        }

        // Encode the cookie.
        val cookieString = request!!.headers()[HttpHeaderNames.COOKIE]
        if (cookieString != null) {
            val cookies: Set<Cookie> = ServerCookieDecoder.STRICT.decode(cookieString)
            if (cookies.isNotEmpty()) {
                // Reset the cookies if necessary.
                cookies.forEach { it ->
                    response.headers()
                        .add(HttpHeaderNames.SET_COOKIE, ServerCookieEncoder.STRICT.encode(it))
                }
            }
        } else {
            // Browser sent no cookie.  Add some.
            response.headers().add(
                HttpHeaderNames.SET_COOKIE,
                ServerCookieEncoder.STRICT.encode("key1", "value1")
            )
            response.headers().add(
                HttpHeaderNames.SET_COOKIE,
                ServerCookieEncoder.STRICT.encode("key2", "value2")
            )
        }

        // Write the response.
        ctx.write(response)

        return keepAlive
    }

    companion object {

        private fun send100Continue(ctx: ChannelHandlerContext) {
            val response = DefaultFullHttpResponse(
                HttpVersion.HTTP_1_1, HttpResponseStatus.CONTINUE, Unpooled.EMPTY_BUFFER
            )
            ctx.write(response)
        }

        private fun appendDecoderResult(buf: StringBuilder, o: HttpObject) {
            val result = o.decoderResult()
            if (result.isSuccess) return

            buf.append(".. WITH DECODER FAILURE: ")
            buf.append(result.cause())
            buf.append("\r\n")
        }
    }

}