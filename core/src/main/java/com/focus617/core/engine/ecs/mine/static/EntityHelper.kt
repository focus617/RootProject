package com.focus617.core.engine.ecs.mine.static

import com.focus617.core.engine.ecs.fleks.Entity
import com.focus617.core.engine.ecs.fleks.World
import com.focus617.core.engine.ecs.mine.component.Relationship

fun Entity.setParent(parentEntity: Entity) {
    val mapper = World.CURRENT_WORLD.mapper<Relationship>()
    val parentRelationship = mapper[parentEntity]
    val childRelationship = mapper[this]

    childRelationship.parent = parentEntity.id

    // Parent没有Child
    if (parentRelationship.childrenNumber == 0) {
        parentRelationship.first = this.id
        parentRelationship.last = this.id
        parentRelationship.childrenNumber++
    }
    // Parent已经有Children
    else {
        // 将自己添加到Parent的表尾
        childRelationship.prev = parentRelationship.last
        parentRelationship.last = this.id
        parentRelationship.childrenNumber++

        // 同时修改last Entity的next
        val prevEntity = Entity(childRelationship.prev)
        mapper[prevEntity].next = this.id
    }
}
