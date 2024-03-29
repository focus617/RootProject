package com.focus617.core.engine.ecs.fleks

import org.junit.Assert.*
import org.junit.Test


private data class ComponentTestComponent(var x: Int = 0)

private data class ComponentTestExceptionComponent(var y: Int)

private class ComponentTestComponentListener : ComponentListener<ComponentTestComponent> {
    var numAddCalls = 0
    var numRemoveCalls = 0
    lateinit var cmpCalled: ComponentTestComponent
    var entityCalled = Entity(-1)
    var lastCall = ""

    override fun onComponentAdded(entity: Entity, component: ComponentTestComponent) {
        numAddCalls++
        cmpCalled = component
        entityCalled = entity
        lastCall = "add"
    }

    override fun onComponentRemoved(entity: Entity, component: ComponentTestComponent) {
        numRemoveCalls++
        cmpCalled = component
        entityCalled = entity
        lastCall = "remove"
    }
}

internal class ComponentTest {
    @Test
    fun `add entity to mapper with sufficient capacity`() {
        val cmpService = ComponentService()
        val mapper = cmpService.mapper<ComponentTestComponent>()
        val entity = Entity(0)

        val cmp = mapper.addInternal(entity) { x = 5 }

        assertTrue(entity in mapper)
        assertEquals(5, cmp.x)
    }

    @Test
    fun `add entity to mapper with insufficient capacity`() {
        val cmpService = ComponentService()
        val mapper = cmpService.mapper<ComponentTestComponent>()
        val entity = Entity(10_000)

        val cmp = mapper.addInternal(entity)

        assertTrue(entity in mapper)
        assertEquals(0, cmp.x)
    }

    @Test
    fun `add already existing entity to mapper`() {
        val cmpService = ComponentService()
        val mapper = cmpService.mapper<ComponentTestComponent>()
        val entity = Entity(10_000)
        val expected = mapper.addInternal(entity)

        val actual = mapper.addInternal(entity) { x = 2 }

        assertSame(expected, actual)
        assertEquals(2, actual.x)
    }

    @Test
    fun `returns false when entity is not part of mapper`() {
        val cmpService = ComponentService()
        val mapper = cmpService.mapper<ComponentTestComponent>()

        assertFalse(Entity(0) in mapper)
        assertFalse(Entity(10_000) in mapper)
    }

    @Test
    fun `remove existing entity from mapper`() {
        val cmpService = ComponentService()
        val mapper = cmpService.mapper<ComponentTestComponent>()
        val entity = Entity(0)
        mapper.addInternal(entity)

        mapper.removeInternal(entity)

        assertFalse(entity in mapper)
    }

    @Test
    fun `cannot remove non-existing entity from mapper with insufficient capacity`() {
        val cmpService = ComponentService()
        val mapper = cmpService.mapper<ComponentTestComponent>()
        val entity = Entity(10_000)

        assertThrows(ArrayIndexOutOfBoundsException::class.java) {
            mapper.removeInternal(entity)
        }
    }

    @Test
    fun `get component of existing entity`() {
        val cmpService = ComponentService()
        val mapper = cmpService.mapper<ComponentTestComponent>()
        val entity = Entity(0)
        mapper.addInternal(entity) { x = 2 }

        val cmp = mapper[entity]

        assertEquals(2, cmp.x)
    }

    @Test
    fun `cannot get component of non-existing entity`() {
        val cmpService = ComponentService()
        val mapper = cmpService.mapper<ComponentTestComponent>()
        val entity = Entity(0)

        assertThrows(FleksNoSuchComponentException::class.java) { mapper[entity] }
    }

    @Test
    fun `get component of non-existing entity with sufficient capacity`() {
        val cmpService = ComponentService()
        val mapper = cmpService.mapper<ComponentTestComponent>()
        val entity = Entity(0)

        val cmp = mapper.getOrNull(entity)

        assertNull(cmp)
    }

    @Test
    fun `get component of non-existing entity without sufficient capacity`() {
        val cmpService = ComponentService()
        val mapper = cmpService.mapper<ComponentTestComponent>()
        val entity = Entity(2048)

        val cmp = mapper.getOrNull(entity)

        assertNull(cmp)
    }

    @Test
    fun `get component of existing entity via getOrNull`() {
        val cmpService = ComponentService()
        val mapper = cmpService.mapper<ComponentTestComponent>()
        val entity = Entity(0)
        mapper.addInternal(entity) { x = 2 }

        val cmp = mapper.getOrNull(entity)

        assertEquals(2, cmp?.x)
    }

    @Test
    fun `create new mapper`() {
        val cmpService = ComponentService()

        val mapper = cmpService.mapper<ComponentTestComponent>()

        assertEquals(0, mapper.id)
    }

    @Test
    fun `do not create the same mapper twice`() {
        val cmpService = ComponentService()
        val expected = cmpService.mapper<ComponentTestComponent>()

        val actual = cmpService.mapper<ComponentTestComponent>()

        assertSame(expected, actual)
    }

    @Test
    fun `cannot create mapper for component without no-args constructor`() {
        val cmpService = ComponentService()

        assertThrows(FleksMissingNoArgsComponentConstructorException::class.java) {
            cmpService.mapper(ComponentTestExceptionComponent::class)
        }
    }

    @Test
    fun `get mapper by component id`() {
        val cmpService = ComponentService()
        val expected = cmpService.mapper<ComponentTestComponent>()

        val actual = cmpService.mapper(0)

        assertSame(expected, actual)
    }

    @Test
    fun `add ComponentListener`() {
        val cmpService = ComponentService()
        val listener = ComponentTestComponentListener()
        val mapper = cmpService.mapper<ComponentTestComponent>()

        mapper.addComponentListenerInternal(listener)

        assertTrue(listener in mapper)
    }

    @Test
    fun `remove ComponentListener`() {
        val cmpService = ComponentService()
        val listener = ComponentTestComponentListener()
        val mapper = cmpService.mapper<ComponentTestComponent>()
        mapper.addComponentListenerInternal(listener)

        mapper.removeComponentListener(listener)

        assertFalse(listener in mapper)
    }

    @Test
    fun `add component with ComponentListener`() {
        val cmpService = ComponentService()
        val mapper = cmpService.mapper<ComponentTestComponent>()
        val listener = ComponentTestComponentListener()
        mapper.addComponentListener(listener)
        val expectedEntity = Entity(1)

        val expectedCmp = mapper.addInternal(expectedEntity)

        assertEquals(1, listener.numAddCalls)
        assertEquals(0, listener.numRemoveCalls)
        assertEquals(expectedEntity, listener.entityCalled)
        assertEquals(expectedCmp, listener.cmpCalled)
    }

    @Test
    fun `add component with ComponentListener when component already present`() {
        val cmpService = ComponentService()
        val mapper = cmpService.mapper<ComponentTestComponent>()
        val expectedEntity = Entity(1)
        mapper.addInternal(expectedEntity)
        val listener = ComponentTestComponentListener()
        mapper.addComponentListener(listener)

        val expectedCmp = mapper.addInternal(expectedEntity)

        assertEquals(1, listener.numAddCalls)
        assertEquals(1, listener.numRemoveCalls)
        assertEquals(expectedEntity, listener.entityCalled)
        assertEquals(expectedCmp, listener.cmpCalled)
        assertEquals("add", listener.lastCall)
    }

    @Test
    fun `add component if it does not exist yet`() {
        val cmpService = ComponentService()
        val mapper = cmpService.mapper<ComponentTestComponent>()
        val entity = Entity(1)

        val cmp = mapper.addOrUpdateInternal(entity) { x++ }

        assertTrue(entity in mapper)
        assertEquals(1, cmp.x)
    }

    @Test
    fun `update component if it already exists`() {
        val cmpService = ComponentService()
        val mapper = cmpService.mapper<ComponentTestComponent>()
        val entity = Entity(1)
        val expectedCmp = mapper.addOrUpdateInternal(entity) { x++ }

        val actualCmp = mapper.addOrUpdateInternal(entity) { x++ }

        assertTrue(entity in mapper)
        assertEquals(expectedCmp, actualCmp)
        assertEquals(2, actualCmp.x)
    }
}
