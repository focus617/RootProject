package com.focus617.core.platform.functional

import org.junit.Assert.*

import org.junit.Test

class ResultKtTest {

    @Test
    fun `getSucceeded_create value with success_should return true`() {
        val result = Result.Success(2)
        assertTrue(result.succeeded)
    }

    @Test
    fun `getSucceeded_create value with error_should return false`() {
        val result = Result.Error(Exception("Test exception"))
        assertFalse(result.succeeded)
    }

    @Test
    fun `toString_create value with success_output correct string`(){
        val result = Result.Success(2)
        val str = result.toString()
        assertEquals("Success(data=2)", str)
    }

    @Test
    fun `successOrFallback_create value with success_should return correct value`() {
        val result = Result.Success(2)
        assertEquals(2, result.successOr(0))
    }

    @Test
    fun `successOrFallback_create value with error_should return fallback value`() {
        val result = Result.Error(Exception("Test exception"))
        assertEquals(0, result.successOr(0))
    }
}