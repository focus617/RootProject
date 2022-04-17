package com.focus617.core.platform.event.base

import com.focus617.core.platform.event.applicationEvents.AppLaunchedEvent
import com.focus617.core.platform.event.applicationEvents.AppVariant
import com.focus617.mylib.helper.DateHelper
import com.focus617.mylib.logging.WithLogging
import com.google.common.truth.Truth.assertThat

import org.junit.Test

class EventTest: WithLogging() {

    @Test
    fun `event has correct attributes`() {
        // Given
        val event = AppLaunchedEvent(AppVariant.MOBILE_DEMO, this)
        // When
        // Then
        LOG.info("${event.name} from ${event.source} received.")
        LOG.info("It's type is ${event.eventType}")
        LOG.info("It's was submit at ${DateHelper.timeStampAsStr(event.timestamp)}")

        assertThat(event.name).isEqualTo("AppLaunchedEvent")
        assertThat(event.eventType).isEqualTo(EventType.AppLaunched)
    }

    @Test
    fun isInCategory() {
        // Given
        val event = AppLaunchedEvent(AppVariant.MOBILE_DEMO, this)
        // When
        // Then
        assertThat(event.isInCategory(EventCategory.EventCategoryApplication)).isTrue()
        assertThat(event.isInCategory(EventCategory.EventCategoryInput)).isFalse()
        assertThat(event.isInCategory(EventCategory.EventCategoryKeyboard)).isFalse()
        assertThat(event.isInCategory(EventCategory.EventCategoryMouse)).isFalse()
        assertThat(event.isInCategory(EventCategory.EventCategoryMouseButton)).isFalse()
    }
}