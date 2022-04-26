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
    private var isZooming: Boolean = false
    private var zoomStartX: Float = 0f
    private var zoomStartY: Float = 0f

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        var hasConsumed: Boolean = false

        if (event != null) {
            when (event.action) {
                // 在第一个点被按下时触发
                MotionEvent.ACTION_DOWN -> {
                    LOG.info("MotionEvent.ACTION_DOWN Event: ${event.action}(${event.x},${event.y})")
                    val nativeEvent = TouchPressEvent(event.x, event.y, this)
                    window.mData.callback?.let { hasConsumed = it(nativeEvent) }
                }

                // 当屏幕上已经有一个点被按住，此时再按下其他点时触发
                MotionEvent.ACTION_POINTER_DOWN -> {
                    LOG.info("MotionEvent.ACTION_POINTER_DOWN Event: ${event.action}(${event.x},${event.y})")
                    isZooming = true
                    zoomStartX = event.x
                    zoomStartY = event.y
                }

                // 当屏幕上有多个点被按住，松开其中一个点时触发（即非最后一个点被放开时）
                MotionEvent.ACTION_POINTER_UP -> {
                    LOG.info("MotionEvent.ACTION_POINTER_UP Event: ${event.action}(${event.x},${event.y})")
                    isZooming = false
                }

                MotionEvent.ACTION_MOVE -> {
                    LOG.info("MotionEvent.ACTION_MOVE Event: ${event.action}(${event.x},${event.y})")
                    if (isZooming) {

                    } else {
                        val nativeEvent = TouchDragEvent(event.x, event.y, this)
                        window.mData.callback?.let { hasConsumed = it(nativeEvent) }
                    }
                }

                else -> {
                    LOG.info("MotionEvent.${event.action}")
                }
            }
        }
        return hasConsumed
    }

}