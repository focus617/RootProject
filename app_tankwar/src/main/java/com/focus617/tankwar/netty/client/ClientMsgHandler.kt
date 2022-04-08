package com.focus617.tankwar.netty.client

import io.netty.channel.ChannelHandlerContext
import io.netty.channel.SimpleUserEventChannelHandler

class ClientMsgHandler: SimpleUserEventChannelHandler<String>() {

    override fun eventReceived(ctx: ChannelHandlerContext?, evt: String?) {
        TODO("Not yet implemented")
    }
}