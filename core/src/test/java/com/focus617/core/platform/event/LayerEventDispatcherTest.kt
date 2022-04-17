package com.focus617.core.platform.event

import com.focus617.core.platform.event.applicationEvents.AppLaunchedEvent
import com.focus617.core.platform.event.applicationEvents.AppUpdateEvent
import com.focus617.core.platform.event.applicationEvents.AppVariant
import com.focus617.core.platform.event.base.EventType
import com.focus617.core.platform.event.base.LayerEventDispatcher
import com.focus617.mylib.helper.DateHelper
import com.focus617.mylib.logging.WithLogging
import com.google.common.truth.Truth.assertThat
import org.junit.Before
import org.junit.Test

class LayerEventDispatcherTest : WithLogging() {

    lateinit var dispatcher: LayerEventDispatcher

    @Before
    fun setUp() {
        // Given: only AppLaunchedHandler registered in dispatcher
        dispatcher = LayerEventDispatcher()

        dispatcher.register(EventType.AppLaunched) { event ->
            LOG.info("${event.name} from ${event.source} received")
            LOG.info("It's type is ${event.eventType}")
            LOG.info("It's was submit at ${DateHelper.timeStampAsStr(event.timestamp)}")
            event.handleFinished()
            true
        }
    }

    @Test
    fun register() {
        assertThat(dispatcher.sizeForTest()).isEqualTo(1)

        // When
        dispatcher.register(EventType.AppUpdate) { event ->
            LOG.info("${event.name} from ${event.source} received")
            event.handleFinished()
            true
        }
        // Then
        assertThat(dispatcher.sizeForTest()).isEqualTo(2)
    }

    @Test
    fun unRegister() {
        assertThat(dispatcher.sizeForTest()).isEqualTo(1)

        // When
        dispatcher.unRegister(EventType.AppLaunched)

        // Then
        assertThat(dispatcher.sizeForTest()).isEqualTo(0)
    }

    @Test
    fun `dispatch for registered event`() {
        // When
        val event = AppLaunchedEvent(AppVariant.MOBILE_DEMO, this)
        assertThat(event.hasBeenHandled).isFalse()

        LOG.info("dispatch for registered event")
        val result: Boolean = dispatcher.dispatch(event)

        // Then
        assertThat(result).isTrue()
        assertThat(event.hasBeenHandled).isTrue()

    }

    @Test
    fun `dispatch for unregistered event`() {
        // When
        val event = AppUpdateEvent(AppVariant.MOBILE_DEMO, this)
        assertThat(event.hasBeenHandled).isFalse()

        LOG.info("dispatch for unregistered event")
        val result: Boolean = dispatcher.dispatch(event)

        // Then
        assertThat(result).isFalse()
        assertThat(event.hasBeenHandled).isFalse()

    }
}