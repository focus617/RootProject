package com.focus617.login.test_dispatcher

import com.focus617.core.platform.event.applicationEvents.AppUpdateEvent
import com.focus617.core.platform.event.applicationEvents.AppVariant
import com.focus617.login.test_dispatcher.di_in_module.AppUpdateEventHandler
import com.focus617.mylib.logging.WithLogging
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import javax.inject.Provider

class EventDispatcherTest : WithLogging() {

    val appUpdateEventHandler = AppUpdateEventHandler()

    object Handlers : Provider<EventHandlers<AppUpdateEvent>> {
        override fun get(): EventHandlers<AppUpdateEvent> =
            setOf(AppUpdateEventHandler().appUpdateEventHandler)
    }

    lateinit var dispatcher: EventDispatcher<AppUpdateEvent>

    @Before
    fun setUp() {
        dispatcher = EventDispatcher(Handlers)
    }

    @Test
    fun dispatch() {
        LOG.info("Test AppUpdateEvent and EventDispatcher")
        val event = AppUpdateEvent(AppVariant.TV, this)

        runBlocking {
            dispatcher.dispatch(event)
        }
    }
}