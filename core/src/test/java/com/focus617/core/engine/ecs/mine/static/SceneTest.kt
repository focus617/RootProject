package com.focus617.core.engine.ecs.mine.static

import com.focus617.core.engine.ecs.fleks.Entity
import com.focus617.core.engine.ecs.mine.component.CameraMatrix
import com.focus617.core.engine.ecs.mine.component.OrthographicCameraCmp
import com.focus617.core.engine.ecs.mine.component.Relationship
import com.focus617.core.engine.ecs.mine.component.Relationship.Companion.InvalidateEntity
import com.focus617.core.engine.ecs.mine.component.Tag
import com.focus617.core.platform.base.BaseEntity
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test

/**
 * Unit test, which will execute on the development machine (host).
 */
class SceneTest : BaseEntity() {

    @Before
    fun setUp() {
        // 因为Scene是Object类型，需要先将除SceneCamera以外的所有childEntity删除
        val cameraRelationship = Scene.camera.getComponentOrNull<Relationship>()

        var nextEntityId: Int = cameraRelationship!!.next
        while (nextEntityId != InvalidateEntity.id) {
            val relationship = Entity(nextEntityId).getComponentOrNull<Relationship>()

            Scene.entity().removeChild(nextEntityId)
            nextEntityId = relationship?.next ?: InvalidateEntity.id
        }

    }

    @Test
    fun test_scene() {
        assertEquals(0, Scene.id)
        assertEquals(Entity(Scene.id), Scene.entity())

        val sceneRelationship = Scene.entity().getComponentOrNull<Relationship>()
        assertEquals(1, sceneRelationship!!.childrenNumber)
        assertEquals(Scene.camera.id, sceneRelationship.first)
        assertEquals(Scene.camera.id, sceneRelationship.last)
        assertEquals(InvalidateEntity.id, sceneRelationship.parent)
        assertEquals(InvalidateEntity.id, sceneRelationship.prev)
        assertEquals(InvalidateEntity.id, sceneRelationship.next)
    }

    @Test
    fun test_Camera() {
        assertEquals(1, Scene.camera.id)

        assertTrue(Scene.camera.hasComponent<Tag>())
        assertTrue(Scene.camera.hasComponent<OrthographicCameraCmp>())
        assertTrue(Scene.camera.hasComponent<CameraMatrix>())
        assertTrue(Scene.camera.hasComponent<Relationship>())

        val cameraRelationship = Scene.camera.getComponentOrNull<Relationship>()
        assertEquals(0, cameraRelationship!!.childrenNumber)
        assertEquals(InvalidateEntity.id, cameraRelationship.first)
        assertEquals(InvalidateEntity.id, cameraRelationship.last)
        assertEquals(Scene.id, cameraRelationship.parent)
        assertEquals(InvalidateEntity.id, cameraRelationship.prev)
        assertEquals(InvalidateEntity.id, cameraRelationship.next)
    }

    @Test
    fun `createEntity_by default has default Tag with correct value`() {
        val entity = Scene.createEntity()

        assertTrue(entity.hasComponent<Tag>())
        assertEquals(Scene.DefaultTag, entity.getComponentOrNull<Tag>()!!.tag)
    }

    @Test
    fun `createEntity_by default has Tag component with correct value`() {
        val tag = "NewEntity"
        val entity = Scene.createEntity(tag)

        assertTrue(entity.hasComponent<Tag>())
        assertEquals(tag, entity.getComponentOrNull<Tag>()!!.tag)
    }

    @Test
    fun `createEntity_by default has Relationship component with correct value`() {
        val tag = "NewEntity"
        val entity = Scene.createEntity(tag)

        assertTrue(entity.hasComponent<Relationship>())

        val relationship = entity.getComponentOrNull<Relationship>()
        assertNotNull(relationship)
        assertEquals(Scene.id, relationship!!.parent)

        val sceneRelationship = Scene.entity().getComponentOrNull<Relationship>()
        // The first child is Scene Camera
        assertEquals(2, sceneRelationship!!.childrenNumber)
        assertEquals(entity.id, sceneRelationship.last)
    }

    @Test
    fun `createEntity_with parent has Relationship component with correct value`() {
        val parentEntity = Scene.createEntity("ParentEntity")
        val childEntity = Scene.createEntity("ChildEntity", parentEntity)

        val parentRelationship = parentEntity.getComponentOrNull<Relationship>()
        assertEquals(1, parentRelationship!!.childrenNumber)
        assertEquals(childEntity.id, parentRelationship.first)
        assertEquals(childEntity.id, parentRelationship.last)

        val childRelationship = childEntity.getComponentOrNull<Relationship>()
        assertEquals(parentEntity.id, childRelationship!!.parent)
        assertEquals(InvalidateEntity.id, childRelationship.prev)
        assertEquals(InvalidateEntity.id, childRelationship.next)
    }

}