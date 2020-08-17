package com.example.pomodoro2.platform.mockito

import org.junit.Rule
import org.junit.runner.RunWith
import org.mockito.ArgumentCaptor
import org.mockito.junit.MockitoJUnitRunner

/**
 * Base class for Unit tests.
 * It contains the method captureArg(), making use of Generics to cast
 * the null object returned by Mockito into the specific class object.
 *
 * Inherit from it to create test cases which DO NOT contain android
 * framework dependencies or components.
 *
 * The extended test case class will have access to this modified version of
 * capture() when you want to use them.
 *
 */
@RunWith(MockitoJUnitRunner::class)
abstract class BaseUnitTest {
    @Suppress("LeakingThis")
    @Rule
    @JvmField val injectMocks = InjectMocksRule.create(this@BaseUnitTest)

    open fun <T> captureArg(argumentCaptor: ArgumentCaptor<T>): T =
        argumentCaptor.capture()
}
