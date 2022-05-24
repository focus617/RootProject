package com.focus617.core.engine.ecs.mine.component

/**
 * 表达层级关系
 * 里面的数据值（Int类型） = Entity.id，值为-1表示无效值
 */
data class Relationship(
    var childrenNumber: Int = 0,
    var first: Int = -1,   // children链表的表头
    var last: Int = -1,    // children链表的表尾

    var parent: Int = -1,
    var prev: Int = -1,    // 链表的前一个
    var next: Int = -1     // 链表的后一个
)
