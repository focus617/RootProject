package com.focus617.login.test_dispatcher.di_in_module

import com.focus617.core.platform.event.applicationEvents.AppLaunchedEvent
import com.focus617.login.test_dispatcher.EventHandler
import com.focus617.mylib.helper.DateHelper
import com.focus617.mylib.logging.WithLogging
import javax.inject.Inject

class AppLaunchedEventHandler @Inject constructor() : WithLogging() {

    val appLaunchedHandler = EventHandler<AppLaunchedEvent> {
        LOG.info("${it.name} from ${it.source} received")
        LOG.info("It was submit at ${DateHelper.timeStampAsStr(it.timestamp)}")
        LOG.info("It's variant is ${it.variant}")

        it.handleFinished()
    }

}
