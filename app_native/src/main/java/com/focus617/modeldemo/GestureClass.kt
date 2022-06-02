package com.focus617.modeldemo

import android.app.Activity
import android.view.GestureDetector
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.View
import androidx.core.view.GestureDetectorCompat
import androidx.core.view.MotionEventCompat


class GestureClass(activity: Activity) {

    private external fun DoubleTapNative()
    private external fun ScrollNative(
        distanceX: Float,
        distanceY: Float,
        positionX: Float,
        positionY: Float
    )
    private external fun ScaleNative(scaleFactor: Float)
    private external fun MoveNative(distanceX: Float, distanceY: Float)

    private val mTapScrollDetector: GestureDetectorCompat
    private val mScaleDetector: ScaleGestureDetector
    private var mTwoFingerPointerId = INVALID_POINTER_ID

    init {

        // instantiate two listeners for detecting double-tap/drag and pinch-zoom
        mTapScrollDetector = GestureDetectorCompat(activity, MyTapScrollListener())
        mScaleDetector = ScaleGestureDetector(
            activity.applicationContext,
            ScaleListener()
        )
    }

    // this listener detects gesture of dragging with two fingers
    var TwoFingerGestureListener: View.OnTouchListener = object : View.OnTouchListener {
        private var mLastTouchX = 0f
        private var mLastTouchY = 0f

        override fun onTouch(v: View, event: MotionEvent): Boolean {

            // let the other detectors also consume the event
            mTapScrollDetector.onTouchEvent(event)
            mScaleDetector.onTouchEvent(event)
            val action = MotionEventCompat.getActionMasked(event)
            when (action) {

                /**
                 * Touch listener to use for in-layout UI controls to delay hiding the
                 * system UI. This is to prevent the jarring behavior of controls going away
                 * while interacting with activity UI.
                 */
                MotionEvent.ACTION_DOWN -> {
                    (activity as FullscreenActivity).delayedHide()
                }

                MotionEvent.ACTION_UP -> {
                    mTwoFingerPointerId = INVALID_POINTER_ID
                    v.performClick()
                }

                MotionEvent.ACTION_MOVE -> {
                    // track the drag only if two fingers are placed on screen
                    if (mTwoFingerPointerId != INVALID_POINTER_ID) {
                        val x = MotionEventCompat.getX(event, mTwoFingerPointerId)
                        val y = MotionEventCompat.getY(event, mTwoFingerPointerId)

                        // Calculate the distance moved
                        val dx = x - mLastTouchX
                        val dy = y - mLastTouchY

                        // Remember this touch position for the next move event
                        mLastTouchX = x
                        mLastTouchY = y
                        MoveNative(dx, dy)
                    }
                }

                MotionEvent.ACTION_CANCEL -> {
                    mTwoFingerPointerId = INVALID_POINTER_ID
                }

                MotionEvent.ACTION_POINTER_DOWN -> {
                    // detected two fingers, start the drag
                    mTwoFingerPointerId = MotionEventCompat.getActionIndex(event)
                    val x = MotionEventCompat.getX(event, mTwoFingerPointerId)
                    val y = MotionEventCompat.getY(event, mTwoFingerPointerId)

                    // Remember where we started (for dragging)
                    mLastTouchX = x
                    mLastTouchY = y
                }

                MotionEvent.ACTION_POINTER_UP -> {
                    // two fingers are not placed on screen anymore
                    mTwoFingerPointerId = INVALID_POINTER_ID
                }
            }
            return true
        }
    }

    // this class detects double-tap gesture and tracks the drag gesture by single finger
    internal inner class MyTapScrollListener : GestureDetector.SimpleOnGestureListener() {
        override fun onDoubleTap(event: MotionEvent): Boolean {
            DoubleTapNative()
            return true
        }

        // function is called if user scrolls with one/two fingers
        // we ignore the call if two fingers are placed on screen
        override fun onScroll(
            e1: MotionEvent, e2: MotionEvent,
            distanceX: Float, distanceY: Float
        ): Boolean {
            if (mTwoFingerPointerId == INVALID_POINTER_ID) {
                ScrollNative(distanceX, distanceY, e2.x, e2.y)
            }
            return true
        }
    }

    // this class detects pinch and zoom gestures
    private inner class ScaleListener : ScaleGestureDetector.SimpleOnScaleGestureListener() {
        override fun onScale(detector: ScaleGestureDetector): Boolean {
            ScaleNative(detector.scaleFactor)
            return true
        }
    }

    companion object {
        var INVALID_POINTER_ID = -100
    }

}