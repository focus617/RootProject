package com.focus617.core.engine.ecs.mine.static

import com.focus617.core.engine.ecs.fleks.Entity
import com.focus617.core.engine.ecs.fleks.World
import com.focus617.core.engine.ecs.mine.component.Relationship
import com.focus617.core.engine.ecs.mine.component.Relationship.Companion.InvalidateEntity
import com.focus617.core.platform.base.BaseEntity
import org.junit.Assert.*
import org.junit.Test

/**
 * Unit test, which will execute on the development machine (host).
 */
class EntityHelperTest : BaseEntity() {
    data class ComponentDemo(var value: Int = 1)

    private val world: World = Scene.world()

    @Test
    fun `test_Entity equals`() {
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
        entt.addOrUpdateComponent<ComponentDemo> { value = 200 }
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
        val parentEntity = world.entity { add<Relationship>() }
        val childEntity = world.entity { entt ->
            add<Relationship>()
            entt.setParent(parentEntity)
        }

        // Then
        val parentRelationship = parentEntity.getComponentOrNull<Relationship>()
        assertEquals(1, parentRelationship!!.childrenNumber)
        assertEquals(childEntity.id, parentRelationship.first)
        assertEquals(childEntity.id, parentRelationship.last)
    }

    @Test
    fun `setParent_first child got correct set`() {
        // Given
        val parentEntity = world.entity { add<Relationship>() }
        val childEntity = world.entity { entt ->
            add<Relationship>()
            entt.setParent(parentEntity)
        }

        // Then
        val childRelationship = childEntity.getComponentOrNull<Relationship>()
        assertEquals(parentEntity.id, childRelationship!!.parent)
        assertEquals(InvalidateEntity.id, childRelationship.prev)
        assertEquals(InvalidateEntity.id, childRelationship.next)
    }

    @Test
    fun `setParent_parent got correct set for 2nd child`() {
        // Given
        val parentEntity = world.entity { add<Relationship>() }
        val entt1 = world.entity { entt1 ->
            add<Relationship>()
            entt1.setParent(parentEntity)
        }
        val entt2 = world.entity { entt2 ->
            add<Relationship>()
            entt2.setParent(parentEntity)
        }

        // Then
        val parentRelationship = parentEntity.getComponentOrNull<Relationship>()
        assertEquals(2, parentRelationship!!.childrenNumber)
        assertEquals(entt1.id, parentRelationship.first)
        assertEquals(entt2.id, parentRelationship.last)
    }

    @Test
    fun `setParent_add 2nd child, children got correct set`() {
        // Given
        val parentEntity = world.entity { add<Relationship>() }
        val entt1 = world.entity { entt1 ->
            add<Relationship>()
            entt1.setParent(parentEntity)
        }
        val entt2 = world.entity { entt2 ->
            add<Relationship>()
            entt2.setParent(parentEntity)
        }

        // Then
        val entt1Relationship = entt1.getComponentOrNull<Relationship>()
        assertEquals(parentEntity.id, entt1Relationship!!.parent)
        assertEquals(InvalidateEntity.id, entt1Relationship.prev)
        assertEquals(entt2.id, entt1Relationship.next)

        val entt2Relationship = entt2.getComponentOrNull<Relationship>()
        assertEquals(parentEntity.id, entt2Relationship!!.parent)
        assertEquals(entt1.id, entt2Relationship.prev)
        assertEquals(InvalidateEntity.id, entt2Relationship.next)
    }

    @Test
    fun `removeChild_remove single child_verify parent`() {
        // Given
        val parentEntity = world.entity { add<Relationship>() }
        val childEntity = world.entity { entt ->
            add<Relationship>()
            entt.setParent(parentEntity)
        }
        // When
        parentEntity.removeChild(childEntity)
        // Then
        val parentRelationship = parentEntity.getComponentOrNull<Relationship>()
        assertEquals(0, parentRelationship!!.childrenNumber)
        assertEquals(InvalidateEntity.id, parentRelationship.first)
        assertEquals(InvalidateEntity.id, parentRelationship.last)
    }

    @Test
    fun `removeChild_remove single child_verify child`() {
        // Given
        val parentEntity = world.entity { add<Relationship>() }
        val childEntity = world.entity { entt ->
            add<Relationship>()
            entt.setParent(parentEntity)
        }
        // When
        parentEntity.removeChild(childEntity)
        // Then
        val childRelationship = childEntity.getComponentOrNull<Relationship>()
        assertEquals(InvalidateEntity.id, childRelationship!!.parent)
        assertEquals(InvalidateEntity.id, childRelationship.prev)
        assertEquals(InvalidateEntity.id, childRelationship.next)
    }

    @Test
    fun `removeChild_one of child which is at first`() {
        // Given
        val parentEntity = world.entity { add<Relationship>() }
        val entt1 = world.entity { entt1 ->
            add<Relationship>()
            entt1.setParent(parentEntity)
        }
        val entt2 = world.entity { entt2 ->
            add<Relationship>()
            entt2.setParent(parentEntity)
        }

        // When
        parentEntity.removeChild(entt1)

        // Then
        val parentRelationship = parentEntity.getComponentOrNull<Relationship>()
        assertEquals(1, parentRelationship!!.childrenNumber)
        assertEquals(entt2.id, parentRelationship.first)
        assertEquals(entt2.id, parentRelationship.last)

        val entt1Relationship = entt1.getComponentOrNull<Relationship>()
        assertEquals(InvalidateEntity.id, entt1Relationship!!.parent)
        assertEquals(InvalidateEntity.id, entt1Relationship.prev)
        assertEquals(InvalidateEntity.id, entt1Relationship.next)

        val entt2Relationship = entt2.getComponentOrNull<Relationship>()
        assertEquals(parentEntity.id, entt2Relationship!!.parent)
        assertEquals(InvalidateEntity.id, entt2Relationship.prev)
        assertEquals(InvalidateEntity.id, entt2Relationship.next)
    }

    @Test
    fun `removeChild_one of child which is at last`() {
        // Given
        val parentEntity = world.entity { add<Relationship>() }
        val entt1 = world.entity { entt1 ->
            add<Relationship>()
            entt1.setParent(parentEntity)
        }
        val entt2 = world.entity { entt2 ->
            add<Relationship>()
            entt2.setParent(parentEntity)
        }

        // When
        parentEntity.removeChild(entt2)

        // Then
        val parentRelationship = parentEntity.getComponentOrNull<Relationship>()
        assertEquals(1, parentRelationship!!.childrenNumber)
        assertEquals(entt1.id, parentRelationship.first)
        assertEquals(entt1.id, parentRelationship.last)

        val entt1Relationship = entt1.getComponentOrNull<Relationship>()
        assertEquals(parentEntity.id, entt1Relationship!!.parent)
        assertEquals(InvalidateEntity.id, entt1Relationship.prev)
        assertEquals(InvalidateEntity.id, entt1Relationship.next)

        val entt2Relationship = entt2.getComponentOrNull<Relationship>()
        assertEquals(InvalidateEntity.id, entt2Relationship!!.parent)
        assertEquals(InvalidateEntity.id, entt2Relationship.prev)
        assertEquals(InvalidateEntity.id, entt2Relationship.next)
    }

    @Test
    fun `removeChild_child is in the middle`() {
        // Given
        val parentEntity = world.entity { add<Relationship>() }
        val entt1 = world.entity { entt1 ->
            add<Relationship>()
            entt1.setParent(parentEntity)
        }
        val entt2 = world.entity { entt2 ->
            add<Relationship>()
            entt2.setParent(parentEntity)
        }
        val entt3 = world.entity { entt3 ->
            add<Relationship>()
            entt3.setParent(parentEntity)
        }
        // When
        parentEntity.removeChild(entt2)

        // Then
        val parentRelationship = parentEntity.getComponentOrNull<Relationship>()
        assertEquals(2, parentRelationship!!.childrenNumber)
        assertEquals(entt1.id, parentRelationship.first)
        assertEquals(entt3.id, parentRelationship.last)

        val entt1Relationship = entt1.getComponentOrNull<Relationship>()
        assertEquals(parentEntity.id, entt1Relationship!!.parent)
        assertEquals(InvalidateEntity.id, entt1Relationship.prev)
        assertEquals(entt3.id, entt1Relationship.next)

        val entt2Relationship = entt2.getComponentOrNull<Relationship>()
        assertEquals(InvalidateEntity.id, entt2Relationship!!.parent)
        assertEquals(InvalidateEntity.id, entt2Relationship.prev)
        assertEquals(InvalidateEntity.id, entt2Relationship.next)

        val entt3Relationship = entt3.getComponentOrNull<Relationship>()
        assertEquals(parentEntity.id, entt3Relationship!!.parent)
        assertEquals(entt1.id, entt3Relationship.prev)
        assertEquals(InvalidateEntity.id, entt3Relationship.next)
    }
}