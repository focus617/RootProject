package com.focus617.login.test_dispatcher.di_in_module

import com.focus617.core.platform.event.applicationEvents.AppUpdateEvent
import com.focus617.login.test_dispatcher.EventHandler
import com.focus617.mylib.helper.DateHelper
import com.focus617.mylib.logging.WithLogging
import javax.inject.Inject

class AppUpdateEventHandler @Inject constructor() : WithLogging() {

    val appUpdateEventHandler = EventHandler<AppUpdateEvent> {
        LOG.info("${it.name} from ${it.source} received")
        LOG.info("It was submit at ${DateHelper.timeStampAsStr(it.timestamp)}")
        LOG.info("It's variant is ${it.variant}")

        it.handleFinished()
    }

}
