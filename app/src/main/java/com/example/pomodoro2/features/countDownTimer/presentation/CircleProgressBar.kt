package com.example.pomodoro2.features.countDownTimer.presentation

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import com.example.pomodoro2.R

class CircleProgressBar : View {
    private var mBackPaint: Paint? = null
    private var mFrontPaint: Paint? = null
    private var mTextPaint: Paint? = null
    private var mFrontColor = 0
    private var mBackColor = 0
    private var mTextColor = 0
    private var mTextSize = 0
    private var mStrokeWidth = 0f
    private val mHalfStrokeWidth = mStrokeWidth / 2
    private var mRadius = 0f
    private var mRect: RectF? = null
    private var mWidth = 0
    private var mHeight = 0

    // interface of CircleProgressBar
    var progress = 0
    private var mMax = 0

    constructor(context: Context?) : super(context) {
        initPaint()
    }

    constructor(context: Context, attrs: AttributeSet?) : super(context, attrs) {
        val ta = context.obtainStyledAttributes(attrs, R.styleable.circleProgressBar)
        mStrokeWidth = ta.getInteger(R.styleable.circleProgressBar_stroke_width, 20).toFloat()
        mRadius = ta.getInteger(R.styleable.circleProgressBar_radius, 600).toFloat()
        mFrontColor = ta.getColor(R.styleable.circleProgressBar_front_color, Color.BLUE)
        mBackColor = ta.getColor(R.styleable.circleProgressBar_background_color, Color.WHITE)
        mTextColor = ta.getColor(R.styleable.circleProgressBar_text_color, Color.BLUE)
        mTextSize = ta.getDimensionPixelSize(R.styleable.circleProgressBar_text_size, 60)
        progress = ta.getInteger(R.styleable.circleProgressBar_default_progress, 0)
        mMax = ta.getInteger(R.styleable.circleProgressBar_max, 100)
        //最后记得将TypedArray对象回收
        ta.recycle()
        initPaint()
    }

    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    ) {
        initPaint()
    }

    // 完成画笔相关参数的初始化
    private fun initPaint() {
        mBackPaint = Paint()
        mBackPaint!!.color = mBackColor
        mBackPaint!!.isAntiAlias = true
        mBackPaint!!.style = Paint.Style.STROKE
        mBackPaint!!.strokeWidth = mStrokeWidth
        mFrontPaint = Paint()
        mFrontPaint!!.color = mFrontColor
        mFrontPaint!!.isAntiAlias = true
        mFrontPaint!!.style = Paint.Style.STROKE
        mFrontPaint!!.strokeWidth = mStrokeWidth
        mTextPaint = Paint()
        mTextPaint!!.color = mTextColor
        mTextPaint!!.isAntiAlias = true
        mTextPaint!!.textSize = 80f
        mTextPaint!!.textAlign = Paint.Align.CENTER
    }

    // 重写测量大小的onMeasure方法
    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        mWidth = getRealSize(widthMeasureSpec)
        mHeight = getRealSize(heightMeasureSpec)
        val min = if (mWidth < mHeight) mWidth else mHeight
        mRadius = (min - mStrokeWidth) / 2
        setMeasuredDimension(mWidth, mHeight)
    }

    // 重写绘制View的核心方法onDraw()
    override fun onDraw(canvas: Canvas) {
        initRect()
        val angle = progress / mMax.toFloat() * 360
        canvas.drawCircle(mWidth / 2.toFloat(), mHeight / 2.toFloat(), mRadius, mBackPaint)
        canvas.drawArc(mRect, -90f, angle, false, mFrontPaint)
        canvas.drawText(
            "$progress%", mWidth / 2 + mHalfStrokeWidth,
            mHeight * 4 / 5 + mHalfStrokeWidth, mTextPaint
        )
        invalidate()
    }

    private fun getRealSize(measureSpec: Int): Int {
        var result = 1
        val mode = MeasureSpec.getMode(measureSpec)
        val size = MeasureSpec.getSize(measureSpec)
        result = if (mode == MeasureSpec.AT_MOST || mode == MeasureSpec.UNSPECIFIED) {
            //自己计算
            val demand = (mRadius * 2 + mStrokeWidth).toInt()
            if (demand > size) size else demand //
        } else {
            size
        }
        return result
    }

    private fun initRect() {
        if (mRect == null) {
            mRect = RectF()
            val viewSize = (mRadius * 2).toInt()
            val left = (mWidth - viewSize) / 2
            val top = (mHeight - viewSize) / 2
            val right = left + viewSize
            val bottom = top + viewSize
            mRect!![left.toFloat(), top.toFloat(), right.toFloat()] = bottom.toFloat()
        }
    }
}
