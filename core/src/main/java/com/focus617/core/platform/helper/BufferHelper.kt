package com.focus617.core.platform.helper

import com.focus617.core.engine.math.Vector2
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.Vector4
import java.nio.ByteBuffer
import java.nio.ByteOrder

fun ByteBuffer.putVector2(value: Vector2): ByteBuffer {
    try {
        this.order(ByteOrder.nativeOrder())         //设置字节顺序为本地操作系统顺序
            .putFloat(value.x)
            .putFloat(value.y)
    } catch (e: Exception) {
        println(e.message)
    }
    return this
}

fun ByteBuffer.putVector3(value: Vector3): ByteBuffer {
    try {
        this.order(ByteOrder.nativeOrder())         //设置字节顺序为本地操作系统顺序
            .putFloat(value.x)
            .putFloat(value.y)
            .putFloat(value.z)
    } catch (e: Exception) {
        println(e.message)
    }
    return this
}

fun ByteBuffer.putVector4(value: Vector4): ByteBuffer {
    try {
        this.order(ByteOrder.nativeOrder())         //设置字节顺序为本地操作系统顺序
            .putFloat(value.x)
            .putFloat(value.y)
            .putFloat(value.z)
            .putFloat(value.w)
    } catch (e: Exception) {
        println(e.message)
    }
    return this
}

