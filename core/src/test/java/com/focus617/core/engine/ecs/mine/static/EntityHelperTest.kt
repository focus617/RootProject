package com.focus617.core.engine.ecs.mine.static

import com.focus617.core.engine.ecs.fleks.Entity
import com.focus617.core.engine.ecs.fleks.World
import com.focus617.core.engine.ecs.mine.component.Relationship
import com.focus617.core.engine.ecs.mine.component.Relationship.Companion.invalidateEntity
import org.junit.Assert.*
import org.junit.Before
import org.junit.Test
import kotlin.properties.Delegates

/**
 * Unit test, which will execute on the development machine (host).
 */
class EntityHelperTest {
    data class ComponentDemo(var value: Int = 1)

    private lateinit var world: World
    var sceneId by Delegates.notNull<Int>()

    @Before
    fun setUp() {
        world = World {
            entityCapacity = 600
        }

        // Scene is always the first entity(id=0)
        val scene = world.entity {
            add<Relationship>()
        }
        sceneId = scene.id
    }

    @Test
    fun test_world() {
        val entt = world.entity()
        assertEquals(world, entt.world())
    }

    @Test
    fun test_scene() {
        assertEquals(0, sceneId)

        val entt = world.entity()
        assertEquals(Entity(sceneId), entt.scene())
    }

    @Test
    fun `test_Entity equals`(){
        val entt1 = world.entity()
        val entt2 = world.entity()

        assertTrue(entt1 == Entity(entt1.id))
        assertFalse(entt1 == entt2)
        assertTrue(entt1 != entt2)
    }

    @Test
    fun test_hasComponent() {
        val entt = world.entity { add<Relationship>() }

        assertTrue(entt.hasComponent<Relationship>())
        assertFalse(entt.hasComponent<ComponentDemo>())
    }

    @Test
    fun `test_getComponentOrNull_return null for not exist component`() {
        val entt = world.entity { add<ComponentDemo>() }

        assertNull(entt.getComponentOrNull<Relationship>())
    }

    @Test
    fun `test_getComponentOrNull_return correct value for exist component`() {
        val entt = world.entity { add<ComponentDemo> { value = 100 } }
        // When
        val comp = entt.getComponentOrNull<ComponentDemo>()
        // Then
        assertNotNull(entt.getComponentOrNull<ComponentDemo>())
        assertEquals(100, comp!!.value)
    }

    @Test
    fun `test_addOrUpdateInternal_add not exist component`() {
        val entt = world.entity()
        // When
        entt.addOrUpdateComponent<ComponentDemo>{ value = 200 }
        assertNotNull(entt.getComponentOrNull<ComponentDemo>())

        val comp = entt.getComponentOrNull<ComponentDemo>()
        assertEquals(200, comp!!.value)
    }

    @Test
    fun `remove existing component`() {
        // Given
        val entt = world.entity { add<Relationship>() }
        // When
        entt.removeComponent<Relationship>()
        // Then
        assertFalse(entt.hasComponent<Relationship>())
    }

    @Test
    fun `remove non_existing component`() {
        // Given
        val entt = world.entity { add<Relationship>() }
        // When
        entt.removeComponent<ComponentDemo>()
        // Then
        assertFalse(entt.hasComponent<ComponentDemo>())
    }

    @Test
    fun `setParent_parent got correct set for 1st child`() {
        // Given
        val scene = Entity(sceneId)
        val entt = world.entity { entt ->
            add<Relationship>()
            entt.setParent(entt.scene())
        }

        // Then
        val sceneRelationship = scene.getComponentOrNull<Relationship>()
        assertEquals(1, sceneRelationship!!.childrenNumber)
        assertEquals(entt.id, sceneRelationship.first)
        assertEquals(entt.id, sceneRelationship.last)
    }

    @Test
    fun `setParent_first child got correct set`() {
        // Given
        val scene = Entity(sceneId)
        val entt = world.entity { entt ->
            add<Relationship>()
            entt.setParent(entt.scene())
        }

        // Then
        val enttRelationship = entt.getComponentOrNull<Relationship>()
        assertEquals(scene.id, enttRelationship!!.parent)
        assertEquals(invalidateEntity.id, enttRelationship.prev)
        assertEquals(invalidateEntity.id, enttRelationship.next)
    }

    @Test
    fun `setParent_parent got correct set for 2nd child`() {
        // Given
        val scene = Entity(sceneId)
        val entt1 = world.entity { entt1 ->
            add<Relationship>()
            entt1.setParent(scene)
        }
        val entt2 = world.entity { entt2 ->
            add<Relationship>()
            entt2.setParent(scene)
        }

        // Then
        val sceneRelationship = scene.getComponentOrNull<Relationship>()
        assertEquals(2, sceneRelationship!!.childrenNumber)
        assertEquals(entt1.id, sceneRelationship.first)
        assertEquals(entt2.id, sceneRelationship.last)
    }

    @Test
    fun `setParent_add 2nd child, children got correct set`() {
        // Given
        val scene = Entity(sceneId)
        val entt1 = world.entity { entt1 ->
            add<Relationship>()
            entt1.setParent(scene)
        }
        val entt2 = world.entity { entt2 ->
            add<Relationship>()
            entt2.setParent(scene)
        }

        // Then
        val entt1Relationship = entt1.getComponentOrNull<Relationship>()
        assertEquals(scene.id, entt1Relationship!!.parent)
        assertEquals(invalidateEntity.id, entt1Relationship.prev)
        assertEquals(entt2.id, entt1Relationship.next)

        val entt2Relationship = entt2.getComponentOrNull<Relationship>()
        assertEquals(scene.id, entt2Relationship!!.parent)
        assertEquals(entt1.id, entt2Relationship.prev)
        assertEquals(invalidateEntity.id, entt2Relationship.next)
    }

    @Test
    fun `removeChild_remove single child_verify parent`() {
        // Given
        val scene = Entity(sceneId)
        val entt = world.entity { entt ->
            add<Relationship>()
            entt.setParent(scene)
        }
        // When
        scene.removeChild(entt)
        // Then
        val sceneRelationship = scene.getComponentOrNull<Relationship>()
        assertEquals(0, sceneRelationship!!.childrenNumber)
        assertEquals(invalidateEntity.id, sceneRelationship.first)
        assertEquals(invalidateEntity.id, sceneRelationship.last)
    }

    @Test
    fun `removeChild_remove single child_verify child`() {
        // Given
        val scene = Entity(sceneId)
        val entt = world.entity { entt ->
            add<Relationship>()
            entt.setParent(scene)
        }
        // When
        scene.removeChild(entt)
        // Then
        val enttRelationship = entt.getComponentOrNull<Relationship>()
        assertEquals(invalidateEntity.id, enttRelationship!!.parent)
        assertEquals(invalidateEntity.id, enttRelationship.prev)
        assertEquals(invalidateEntity.id, enttRelationship.next)
    }

    @Test
    fun `removeChild_one of child which is at first`() {
        // Given
        val scene = Entity(sceneId)
        val entt1 = world.entity { entt1 ->
            add<Relationship>()
            entt1.setParent(scene)
        }
        val entt2 = world.entity { entt2 ->
            add<Relationship>()
            entt2.setParent(scene)
        }
        // When
        scene.removeChild(entt1)

        // Then
        val sceneRelationship = scene.getComponentOrNull<Relationship>()
        assertEquals(1, sceneRelationship!!.childrenNumber)
        assertEquals(entt2.id, sceneRelationship.first)
        assertEquals(entt2.id, sceneRelationship.last)

        val entt1Relationship = entt1.getComponentOrNull<Relationship>()
        assertEquals(invalidateEntity.id, entt1Relationship!!.parent)
        assertEquals(invalidateEntity.id, entt1Relationship.prev)
        assertEquals(invalidateEntity.id, entt1Relationship.next)

        val entt2Relationship = entt2.getComponentOrNull<Relationship>()
        assertEquals(scene.id, entt2Relationship!!.parent)
        assertEquals(invalidateEntity.id, entt2Relationship.prev)
        assertEquals(invalidateEntity.id, entt2Relationship.next)
    }

    @Test
    fun `removeChild_one of child which is at last`() {
        // Given
        val scene = Entity(sceneId)
        val entt1 = world.entity { entt1 ->
            add<Relationship>()
            entt1.setParent(scene)
        }
        val entt2 = world.entity { entt2 ->
            add<Relationship>()
            entt2.setParent(scene)
        }
        // When
        scene.removeChild(entt2)

        // Then
        val sceneRelationship = scene.getComponentOrNull<Relationship>()
        assertEquals(1, sceneRelationship!!.childrenNumber)
        assertEquals(entt1.id, sceneRelationship.first)
        assertEquals(entt1.id, sceneRelationship.last)

        val entt1Relationship = entt1.getComponentOrNull<Relationship>()
        assertEquals(scene.id, entt1Relationship!!.parent)
        assertEquals(invalidateEntity.id, entt1Relationship.prev)
        assertEquals(invalidateEntity.id, entt1Relationship.next)

        val entt2Relationship = entt2.getComponentOrNull<Relationship>()
        assertEquals(invalidateEntity.id, entt2Relationship!!.parent)
        assertEquals(invalidateEntity.id, entt2Relationship.prev)
        assertEquals(invalidateEntity.id, entt2Relationship.next)
    }

    @Test
    fun `removeChild_child is in the middle`() {
        // Given
        val scene = Entity(sceneId)
        val entt1 = world.entity { entt1 ->
            add<Relationship>()
            entt1.setParent(scene)
        }
        val entt2 = world.entity { entt2 ->
            add<Relationship>()
            entt2.setParent(scene)
        }
        val entt3 = world.entity { entt3 ->
            add<Relationship>()
            entt3.setParent(scene)
        }
        // When
        scene.removeChild(entt2)

        // Then
        val sceneRelationship = scene.getComponentOrNull<Relationship>()
        assertEquals(2, sceneRelationship!!.childrenNumber)
        assertEquals(entt1.id, sceneRelationship.first)
        assertEquals(entt3.id, sceneRelationship.last)

        val entt1Relationship = entt1.getComponentOrNull<Relationship>()
        assertEquals(scene.id, entt1Relationship!!.parent)
        assertEquals(invalidateEntity.id, entt1Relationship.prev)
        assertEquals(entt3.id, entt1Relationship.next)

        val entt2Relationship = entt2.getComponentOrNull<Relationship>()
        assertEquals(invalidateEntity.id, entt2Relationship!!.parent)
        assertEquals(invalidateEntity.id, entt2Relationship.prev)
        assertEquals(invalidateEntity.id, entt2Relationship.next)

        val entt3Relationship = entt3.getComponentOrNull<Relationship>()
        assertEquals(scene.id, entt3Relationship!!.parent)
        assertEquals(entt1.id, entt3Relationship.prev)
        assertEquals(invalidateEntity.id, entt3Relationship.next)    }
}