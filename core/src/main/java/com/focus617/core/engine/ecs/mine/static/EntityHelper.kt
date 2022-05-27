package com.focus617.core.engine.ecs.mine.static

import com.focus617.core.engine.ecs.fleks.Entity
import com.focus617.core.engine.ecs.mine.component.Relationship

inline fun <reified T : Any> Entity.hasComponent(): Boolean = (this in Scene.world().mapper<T>())

inline fun <reified T : Any> Entity.getComponentOrNull(): T? {
    val mapper = Scene.world().mapper<T>()
    return if (this in mapper) mapper.getOrNull(this) else null
}

inline fun <reified T : Any> Entity.removeComponent() {
    val mapper = Scene.world().mapper<T>()
    if (this in mapper) mapper.removeInternal(this)
}

inline fun <reified T : Any> Entity.addOrUpdateComponent(configuration: T.() -> Unit = {}): T {
    val mapper = Scene.world().mapper<T>()
    return mapper.addOrUpdateInternal(this, configuration)
}

fun Entity.setParent(parentEntity: Entity) {
    val mapper = Scene.world().mapper<Relationship>()
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

fun Entity.removeChild(child: Entity) {
    val mapper = Scene.world().mapper<Relationship>()
    val childRelationship = mapper[child]

    check(this.id == childRelationship.parent) {
        "wrong entity(${child.id}), which is not child of mine(${this.id})"
        return
    }

    val parentRelationship = mapper[this]
    val prevChildId = childRelationship.prev
    val nextChildId = childRelationship.next

    with(parentRelationship) {
        childrenNumber--

        // 删除的Child在链表的中间
        if ((first != child.id) && (last != child.id)) {
            with(mapper[Entity(prevChildId)]) {
                next = nextChildId
            }
            with(mapper[Entity(nextChildId)]) {
                prev = prevChildId
            }
        }

        // 删除的Child在链表的首部
        if (first == child.id) {
            first = nextChildId
            if (nextChildId != -1) {
                mapper[Entity(nextChildId)].prev = -1
            }
        }

        // 删除的Child在链表的尾部
        if (last == child.id) {
            last = prevChildId
            if (prevChildId != -1) {
                mapper[Entity(prevChildId)].next = -1
            }
        }
    }

    with(childRelationship) {
        parent = -1
        prev = -1
        next = -1
    }
}

fun Entity.removeChild(childEntityId: Int){
    val mapper = Scene.world().mapper<Relationship>()
    val childEntity = Entity(childEntityId)
    val childRelationship = mapper[childEntity]

    check(this.id == childRelationship.parent) {
        "wrong entity($childEntityId), which is not child of mine(${this.id})"
        return
    }

    removeChild(childEntity)
}



