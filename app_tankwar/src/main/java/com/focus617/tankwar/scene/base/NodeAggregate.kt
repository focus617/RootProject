package com.focus617.tankwar.scene.base

import android.graphics.Canvas

/*
 * Node的集合类，本身不需要绘制，但反映了集合的统一特征，
 * 例如能否移动等（背后代表了计算策略的不同）
 */
class AggregateNode(name: String) : Composite(name) {

    override fun drawComposite(canvas: Canvas) {}
    override fun refreshCompositeData() {}

}