package com.focus617.app_demo.engine.d2

import com.focus617.core.engine.math.Vector2
import com.focus617.core.engine.math.Vector3
import com.focus617.core.engine.math.Vector4
import com.focus617.core.platform.helper.putVector2
import com.focus617.core.platform.helper.putVector3
import com.focus617.core.platform.helper.putVector4
import timber.log.Timber
import java.nio.ByteBuffer

class QuadVertex(
    val Position: Vector3,
    val Color: Vector4,
    val TextCoord: Vector2
    //TODO: Texid
)

fun ByteBuffer.putQuadVertex(value: QuadVertex): ByteBuffer {
    try {
        this.putVector3(value.Position)
            .putVector4(value.Color)
            .putVector2(value.TextCoord)
    } catch (e: Exception) {
        Timber.e(e)
    }
    return this
}