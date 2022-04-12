package com.focus617.mylib.netty.api

import kotlinx.coroutines.channels.Channel

interface IfNorthBoundChannel {
    val inboundChannel: Channel<String>
    val outboundChannel: Channel<String>
}