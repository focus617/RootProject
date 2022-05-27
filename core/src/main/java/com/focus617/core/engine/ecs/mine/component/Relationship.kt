package com.focus617.core.engine.ecs.mine.component

import com.focus617.core.engine.ecs.fleks.Entity

/**
 * 表达层级关系
 * 里面的数据值（Int类型） = Entity.id，值为-1表示无效值
 */
data class Relationship(
    var childrenNumber: Int = 0,
    var first: Int = InvalidateEntity.id,   // children链表的表头
    var last: Int = InvalidateEntity.id,    // children链表的表尾

    var parent: Int = InvalidateEntity.id,
    var prev: Int = InvalidateEntity.id,    // 链表的前一个
    var next: Int = InvalidateEntity.id     // 链表的后一个
){

    companion object{
        val InvalidateEntity = Entity(-1)
    }
}
