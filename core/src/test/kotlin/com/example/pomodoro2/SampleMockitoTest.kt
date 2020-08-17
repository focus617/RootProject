package com.example.pomodoro2

import com.nhaarman.mockito_kotlin.*
import org.junit.Before
import org.junit.Test
import org.mockito.ArgumentMatchers.*
import org.mockito.Mockito
import org.mockito.Mockito.`when`

class SampleMockitoTest:BaseUnitTest(){

    lateinit var mockedList: MutableList<String>

    @Before
    fun setup(){
        //mock creation
        //we can not use MutableList<String>::class.java as Class type
        mockedList = Mockito.mock(mutableListOf<String>().javaClass)
    }

    @Test fun `Verify Mockito Basic Function`(){

        //using mock object
        mockedList.add("once")

        mockedList.add("twice")
        mockedList.add("twice")

        mockedList.add("three times")
        mockedList.add("three times")
        mockedList.add("three times")

        //verification
        verify(mockedList).add("once")
        verify(mockedList, times(1)).add("once")

        //exact number of invocations verification
        verify(mockedList, times(2)).add("twice")
        verify(mockedList, times(3)).add("three times")

        //verification using never(). never() is an alias to times(0)
        verify(mockedList, never()).add("never happened")

        //verification using atLeast()/atMost()
        verify(mockedList, atLeastOnce()).add("three times")
        verify(mockedList, atLeast(2)).add("three times")
        verify(mockedList, atMost(5)).add("three times")

    }

    @Test fun `Verify Mockito Parameter Match Function`() {

        //using mock object
        mockedList.add("once")

        //verification argument matchers
        verify(mockedList).add(anyString())
//        verify(mockedList).add(notNull())
//        verify(mockedList).add(argThat{ argument -> argument.length > 5 })
    }

    @Test fun `Verify the stubbing function`(){

        // mockedList[0] 第一次返回 first，之后都会抛出异常
        `when`(mockedList[0]).thenReturn("first").thenThrow(IllegalArgumentException())

        `when`(mockedList[1]).thenThrow(RuntimeException())

        `when`(mockedList.set(anyInt(), anyString())).thenAnswer {
                invocation -> val args =
                    invocation.arguments
                    println("set index ${args[0]} to ${args[1]}")
                    args[1]
        }

        // use doThrow when stubbing void methods with exceptions
        doThrow(RuntimeException()).`when`(mockedList).clear()
        doReturn("third").`when`(mockedList)[2]
    }
}