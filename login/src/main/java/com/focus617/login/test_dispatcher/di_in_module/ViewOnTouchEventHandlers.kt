package com.focus617.login.test_dispatcher.di_in_module

import com.focus617.core.platform.event.screenTouchEvents.TouchMovedEvent
import com.focus617.login.test_dispatcher.EventHandler
import com.focus617.mylib.helper.DateHelper
import com.focus617.mylib.logging.WithLogging
import javax.inject.Inject

class ViewOnTouchEventHandlers @Inject constructor() : WithLogging() {

    val viewOnTouchEventHandler = EventHandler<TouchMovedEvent> {
        LOG.info("${it.name} from ${it.source} received")
        LOG.info("It was submit at ${DateHelper.timeStampAsStr(it.timestamp)}")
        LOG.info("It's position is (${it.x},${it.y})")

        it.handleFinished()
    }

}
