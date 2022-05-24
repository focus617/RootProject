package com.focus617.core.ecs.mine.static

import com.focus617.core.ecs.fleks.Entity
import com.focus617.core.ecs.fleks.World
import com.focus617.core.ecs.mine.component.Relationship

fun Entity.setParent(parentEntity: Entity) {
    val mapper = World.CURRENT_WORLD.mapper<Relationship>()
    val parentRelationship = mapper[parentEntity]
    val childRelationship = mapper[this]

    // Parent没有Child
    if (parentRelationship.childrenNumber == 0) {
        parentRelationship.first = this.id
        parentRelationship.last = this.id
    }
    // Parent已经有Children
    else {
        // 将自己添加到Parent的表尾
        childRelationship.prev = parentRelationship.last
        parentRelationship.last = this.id
    }
    childRelationship.parent = parentEntity.id
    parentRelationship.childrenNumber++
}
