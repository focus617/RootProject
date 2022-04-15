package com.focus617.core.platform.event.di_in__module

import com.focus617.core.platform.event.applicationEvent.AppLaunchedEvent
import com.focus617.core.platform.event.base.EventHandler
import com.focus617.mylib.helper.DateHelper
import com.focus617.mylib.logging.WithLogging
import javax.inject.Inject

class AppLaunchedEventHandlers @Inject constructor() : WithLogging() {

    val appLaunchedHandler = EventHandler<AppLaunchedEvent> {
        LOG.info("${it.name} from ${it.source} received")
        LOG.info("It was submit at ${DateHelper.timeStampAsStr(it.timestamp)}")
        LOG.info("It's variant is ${it.variant}")

        it.handleFinished()
    }

}
