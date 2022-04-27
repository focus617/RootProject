package com.focus617.app_demo.engine

import android.view.MotionEvent
import android.view.View
import com.focus617.core.engine.math.Point2D
import com.focus617.core.platform.event.screenTouchEvents.*
import com.focus617.mylib.logging.WithLogging

/**
 * MotionEvent reports input details from the touch screen
 * and other input controls. In this case, you are only
 * interested in events where the touch position changed.
 */
class GestureInput(private val window: AndroidWindow) : WithLogging(), View.OnTouchListener {
    private var isZooming: Boolean = false

    override fun onTouch(v: View?, event: MotionEvent?): Boolean {
        var hasConsumed: Boolean = false

        if (event != null) {
            when (event.actionMasked) {
                // 在第一个点被按下时触发
                MotionEvent.ACTION_DOWN -> {
                    LOG.info("MotionEvent.ACTION_DOWN Event: ${event.action}(${event.x},${event.y})")
                    val nativeEvent = TouchPressEvent(event.x, event.y, window)
                    window.mData.callback?.let { hasConsumed = it(nativeEvent) }
                    hasConsumed = true
                }

                // 在第一个点被按下后，再松开时触发
                MotionEvent.ACTION_UP -> {
                    LOG.info("MotionEvent.ACTION_UP Event: ${event.action}(${event.x},${event.y})")
                    hasConsumed = true
                }

                // 当屏幕上已经有一个点被按住，此时再按下其他点时触发
                MotionEvent.ACTION_POINTER_DOWN -> {
                    LOG.info("MotionEvent.ACTION_POINTER_DOWN Event: ${event.action}(${event.x},${event.y})")
                    isZooming = true

                    val zoomStart1 = Point2D(event.getX(0), event.getY(0))
                    val zoomStart2 = Point2D(event.getX(1), event.getY(1))
                    val focus = zoomStart1 + zoomStart2
                    val span = (zoomStart2 - zoomStart1).length()
                    val nativeEvent = PinchStartEvent(focus.x, focus.y, span, window)
                    window.mData.callback?.let { hasConsumed = it(nativeEvent) }
                    hasConsumed = true
                }

                // 当屏幕上有多个点被按住，松开其中一个点时触发（即非最后一个点被放开时）
                MotionEvent.ACTION_POINTER_UP -> {
                    LOG.info("MotionEvent.ACTION_POINTER_UP Event: ${event.action}")
                    isZooming = false

                    val nativeEvent = PinchEndEvent(window)
                    window.mData.callback?.let { hasConsumed = it(nativeEvent) }
                    hasConsumed = true
                }

                MotionEvent.ACTION_MOVE -> {
                    if (isZooming) {
                        LOG.info("MotionEvent.ACTION_MOVE_WHEN_PINCH Event: ${event.action}(${event.x},${event.y})")

                        val zoomStart1 = Point2D(event.getX(0), event.getY(0))
                        val zoomStart2 = Point2D(event.getX(1), event.getY(1))
                        val focus = zoomStart1 + zoomStart2
                        val span = (zoomStart2 - zoomStart1).length()
                        val nativeEvent = PinchEvent(focus.x, focus.y, span, window)
                        window.mData.callback?.let { hasConsumed = it(nativeEvent) }
                    } else {
                        val nativeEvent = TouchDragEvent(event.x, event.y, window)
                        window.mData.callback?.let { hasConsumed = it(nativeEvent) }
                    }
                    hasConsumed = true
                }

                else -> {
                    LOG.info("MotionEvent.${event.action}")
                }
            }
        }
        return hasConsumed
    }

}