package com.buyehou.demo.ui

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.util.Log
import android.view.View

private const val TAG = "FitCircleView"

/**
 * 拟合圆
 *
 * @author buyehou
 */
class FitCircleView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) :
    View(context, attrs, defStyleAttr) {

    private val paint = Paint()

    private val path = Path()

    init {

    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        val wMode = MeasureSpec.getMode(widthMeasureSpec)
        val hMode = MeasureSpec.getMode(heightMeasureSpec)
        Log.d(TAG, "onMeasure: $widthMeasureSpec $heightMeasureSpec $width $height")

    }

    override fun onLayout(changed: Boolean, left: Int, top: Int, right: Int, bottom: Int) {
        super.onLayout(changed, left, top, right, bottom)
        Log.d(TAG, "onLayout: $changed $left $top $right $bottom $width $height")
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        Log.d(TAG, "onDraw: ")
        paint.isAntiAlias = true
        paint.style = Paint.Style.STROKE
        canvas.drawCircle(width.toFloat() / 2f, height.toFloat() / 2f, 100f, paint)

        canvas.drawPath(path, paint)
    }

}