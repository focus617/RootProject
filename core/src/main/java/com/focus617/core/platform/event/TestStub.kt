package com.focus617.core.platform.event

import com.focus617.core.platform.base.BaseEntity
import com.focus617.core.platform.event.applicationEvent.AppLaunchedEvent
import com.focus617.core.platform.event.applicationEvent.AppUpdateEvent
import com.focus617.core.platform.event.applicationEvent.AppVariant
import com.focus617.core.platform.event.base.Event
import com.focus617.core.platform.event.base.EventCategory
import com.focus617.core.platform.event.di_in__module.AppUpdateEventHandlers
import com.focus617.mylib.helper.DateHelper
import com.focus617.mylib.logging.WithLogging
import kotlinx.coroutines.runBlocking
import javax.inject.Provider

class TestStub : BaseEntity() {
    companion object : WithLogging() {
        @JvmStatic
        fun main(vararg args: String) {
            val stub = TestStub()
            val event1 = AppLaunchedEvent(AppVariant.MOBILE_DEMO, stub)

            LOG.info("${event1.name} from ${event1.source} received.")
            LOG.info("It's type is ${event1.eventType}")
            LOG.info("It's was submit at ${DateHelper.timeStampAsStr(event1.timestamp)}")

            testCategory(event1)


            LOG.info("Test AppUpdateEvent")
            val event2 = AppUpdateEvent(AppVariant.TV, stub)
            testCategory(event2)

            val eventDispatcher = EventDispatcher(Handlers)

            runBlocking {
                eventDispatcher.dispatch(event2)
            }
        }

        private fun testCategory(event: Event) {
            LOG.info("It's category is ${event.category}")
            LOG.info(
                "It belongs to EventCategoryApplication" +
                        " is ${event.isInCategory(EventCategory.EventCategoryApplication)}"
            )
            LOG.info(
                "It belongs to EventCategoryInput" +
                        " is ${event.isInCategory(EventCategory.EventCategoryInput)}"
            )
            LOG.info(
                "It belongs to EventCategoryKeyboard" +
                        " is ${event.isInCategory(EventCategory.EventCategoryKeyboard)}"
            )
            LOG.info(
                "It belongs to EventCategoryMouse" +
                        " is ${event.isInCategory(EventCategory.EventCategoryMouse)}"
            )
            LOG.info(
                "It belongs to EventCategoryMouseButton" +
                        " is ${event.isInCategory(EventCategory.EventCategoryMouseButton)}"
            )
            LOG.info("Test finish\n\n")
        }

        object Handlers : Provider<EventHandlers<AppUpdateEvent>> {
            override fun get(): EventHandlers<AppUpdateEvent> =
                setOf(AppUpdateEventHandlers().appUpdateEventHandler)
        }
    }
}
