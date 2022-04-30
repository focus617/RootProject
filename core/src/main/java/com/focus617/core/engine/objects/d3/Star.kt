package com.focus617.core.engine.objects.d3

import com.focus617.core.engine.objects.DynamicCreationObject
import com.focus617.core.engine.objects.ObjectBuilder

// TODO: Z轴显示不正常
class Star(
    var angleNum: Int,  // 星形的锐角个数
    var radius: Float,  // 内角半径
    var R: Float,       // 外角半径
    var z: Float,       // z轴基准坐标
) : DynamicCreationObject() {

    override fun beforeBuild() {
        ObjectBuilder.appendStar(angleNum, radius, R, z)
        buildData = ObjectBuilder.buildData(false)
    }

}