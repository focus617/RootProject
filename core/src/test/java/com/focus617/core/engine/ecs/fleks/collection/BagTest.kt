package com.focus617.core.engine.ecs.fleks.collection

import org.junit.Assert.*
import org.junit.Test

internal class BagTest {
//    @Nested
//    inner class GenericBagTest {
        @Test
        fun `create empty bag of String of size 32`() {
            val bag = bag<String>(32)

            assertEquals(32, bag.capacity)
            assertEquals(0, bag.size)
        }

        @Test
        fun `add String to bag`() {
            val bag = bag<String>()

            bag.add("42")

            assertEquals(1, bag.size)
            assertTrue("42" in bag)
        }


        @Test
        fun `remove existing String from bag`() {
            val bag = bag<String>()
            bag.add("42")

            val expected = bag.removeValue("42")

            assertEquals(0, bag.size)
            assertFalse("42" in bag)
            assertTrue(expected)
        }

        @Test
        fun `remove non-existing String from bag`() {
            val bag = bag<String>()

            val expected = bag.removeValue("42")

            assertFalse(expected)
        }


        @Test
        fun `set String value at index with sufficient capacity`() {
            val bag = bag<String>()

            bag[3] = "42"

            assertEquals(4, bag.size)
            assertEquals("42", bag[3])
        }


        @Test
        fun `set String value at index with insufficient capacity`() {
            val bag = bag<String>(2)

            bag[2] = "42"

            assertEquals(3, bag.size)
            assertEquals("42", bag[2])
            assertEquals(3, bag.capacity)
        }


        @Test
        fun `add String to bag with insufficient capacity`() {
            val bag = bag<String>(0)

            bag.add("42")

            assertEquals(1, bag.size)
            assertEquals("42", bag[0])
            assertEquals(1, bag.capacity)
        }

        @Test
        fun `cannot get String value of invalid in bounds index`() {
            val bag = bag<String>()

            assertThrows(NoSuchElementException::class.java) { bag[0] }
        }

        @Test
        fun `cannot get String value of invalid out of bounds index`() {
            val bag = bag<String>(2)

            assertThrows(IndexOutOfBoundsException::class.java) { bag[2] }
        }

        @Test
        fun `execute action for each value of a String bag`() {
            val bag = bag<String>(4)
            bag[1] = "42"
            bag[2] = "43"
            var numCalls = 0
            val valuesCalled = mutableListOf<String>()

            bag.forEach {
                ++numCalls
                valuesCalled.add(it)
            }

            assertEquals(2, numCalls)
            assertEquals(listOf("42", "43"), valuesCalled)
        }
//    }

//    @Nested
//    inner class IntBagTest {
        @Test
        fun `create empty bag of size 32`() {
            val bag = IntBag(32)

            assertEquals(32, bag.capacity)
            assertEquals(0, bag.size)
        }

        @Test
        fun `add value to bag`() {
            val bag = IntBag()

            bag.add(42)

            assertTrue(bag.isNotEmpty)
            assertEquals(1, bag.size)
            assertTrue(42 in bag)
        }

        @Test
        fun `clear all values from bag`() {
            val bag = IntBag()
            bag.add(42)
            bag.add(43)

            bag.clear()

            assertEquals(0, bag.size)
            assertFalse(42 in bag)
            assertFalse(43 in bag)
        }

        @Test
        fun `add value unsafe with sufficient capacity`() {
            val bag = IntBag(1)

            bag.unsafeAdd(42)

            assertTrue(42 in bag)
        }

        @Test
        fun `add value unsafe with insufficient capacity`() {
            val bag = IntBag(0)

            assertThrows(IndexOutOfBoundsException::class.java) { bag.unsafeAdd(42) }
        }

        @Test
        fun `add value to bag with insufficient capacity`() {
            val bag = IntBag(0)

            bag.add(42)

            assertEquals(1, bag.size)
            assertEquals(42, bag[0])
            assertEquals(1, bag.capacity)
        }

        @Test
        fun `cannot get value of out of bounds index`() {
            val bag = IntBag(2)

            assertThrows(IndexOutOfBoundsException::class.java) { bag[2] }
        }

        @Test
        fun `do not resize when bag has sufficient capacity`() {
            val bag = IntBag(8)

            bag.ensureCapacity(7)

            assertEquals(8, bag.capacity)
        }

        @Test
        fun `resize when bag has insufficient capacity`() {
            val bag = IntBag(8)

            bag.ensureCapacity(9)

            assertEquals(10, bag.capacity)
        }

        @Test
        fun `execute action for each value of the bag`() {
            val bag = IntBag(4)
            bag.add(42)
            bag.add(43)
            var numCalls = 0
            val valuesCalled = mutableListOf<Int>()

            bag.forEach {
                ++numCalls
                valuesCalled.add(it)
            }

            assertEquals(2, numCalls)
            assertEquals(listOf(42, 43), valuesCalled)
        }

        @Test
        fun `sort values by normal Int comparison with size less than 7`() {
            val bag = IntBag()
            repeat(6) { bag.add(6 - it) }

            bag.sort(compareEntity { e1, e2 -> e1.id.compareTo(e2.id) })

            repeat(6) {
                assertEquals(it + 1, bag[it])
            }
        }

        @Test
        fun `sort values by normal Int comparison with size less than 50 but greater 7`() {
            val bag = IntBag()
            repeat(8) { bag.add(8 - it) }

            bag.sort(compareEntity { e1, e2 -> e1.id.compareTo(e2.id) })

            repeat(8) {
                assertEquals(it + 1, bag[it])
            }
        }

        @Test
        fun `sort values by normal Int comparison with size greater 50`() {
            val bag = IntBag()
            repeat(51) { bag.add(51 - it) }

            bag.sort(compareEntity { e1, e2 -> e1.id.compareTo(e2.id) })

            repeat(51) {
                assertEquals(it + 1, bag[it])
            }
        }
    }
//}
