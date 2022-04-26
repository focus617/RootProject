package com.focus617.app_demo.engine

import android.view.MotionEvent
import android.view.View
import com.focus617.core.platform.event.screenTouchEvents.TouchDragEvent
import com.focus617.core.platform.event.screenTouchEvents.TouchPressEvent
import com.focus617.mylib.logging.WithLogging

/**
 * MotionEvent reports input details from the touch screen
 * and other input controls. In this case, you are only
 * interested in events where the touch position changed.
 */
class TouchInput(private val window: AndroidWindow) : WithLogging(), View.OnTouchListener {

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        var hasConsumed: Boolean = false

        if (event != null) {
            LOG.info("onTouchEvent: ${event.action}(${event.x},${event.y})")
            when (event.action) {
                MotionEvent.ACTION_DOWN -> {
                    val event = TouchPressEvent(event.x, event.y, this)
                    window.mData.callback?.let { hasConsumed = it(event) }
                }

                MotionEvent.ACTION_MOVE -> {
                    val event = TouchDragEvent(event.x, event.y, this)
                    window.mData.callback?.let { hasConsumed = it(event) }
                }

                else -> {
                    LOG.info("onTouchEvent: ${event.action}")
                }
            }
        }
        return hasConsumed
    }

}