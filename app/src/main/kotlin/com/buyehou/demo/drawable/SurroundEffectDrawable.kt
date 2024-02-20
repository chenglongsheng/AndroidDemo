package com.buyehou.demo.drawable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Paint
import android.graphics.PointF
import android.util.Log
import com.buyehou.demo.helper.CircleDrawHelper
import com.buyehou.demo.util.SystemUtil
import kotlin.math.cos
import kotlin.math.sin

private const val TAG = "SurroundEffectDrawable"

/**
 * @author buyehou
 */
class SurroundEffectDrawable(private val context: Context) : BaseEffectDrawable(context) {

    private lateinit var points: Array<PointF>
    private var mDrawWidth = 10f

    private var mCircleDrawHelper: CircleDrawHelper? = null

    private var mLineWidth = 2

    private val mAmount = 4

    init {
        init()
    }

    private fun init() {
        mLineWidth = SystemUtil.dip2px(context, mLineWidth.toFloat())
        mDrawWidth = SystemUtil.dip2px(context, mDrawWidth).toFloat()
        mCircleDrawHelper = CircleDrawHelper(mCount / mAmount)
        points = arrayOf()
        for (i in 0 until mCount / mAmount) {
            points[i] = PointF()
        }
    }

    override fun draw(canvas: Canvas) {
        mIsDrawing = true
        drawWare(canvas)
        mIsDrawing = false
    }

    private fun drawWare(canvas: Canvas) {
        Log.d(TAG, "drawWare: ${canvas.width} ${canvas.height}")
        for (j in 0 until mAmount) {
            for (i in points.indices) {
                val index = i * mCountOffset * mAmount
                var value = 0f
                if (mData != null) {
                    value = mData!![j + i].toInt().toFloat()
                }
                if (value > 0) {
                    value = SystemUtil.dip2px(context, value * 0.5f).toFloat()
                }
                val r = mRadius + value
                val x = sin(Math.toRadians(index.toDouble())).toFloat() * r + canvas.width / 2
                val y = cos(Math.toRadians(index.toDouble())).toFloat() * r + canvas.height / 2
                mCircleDrawHelper?.setPoint(points[i], x, y)
            }
            mCircleDrawHelper?.calculate(points, 0.8)
            mPaint.style = Paint.Style.STROKE
            mPaint.color = getColor(j)
            mPaint.alpha = 255 - 50 * (j + 1)
            mCircleDrawHelper?.drawBezierCurve(canvas, points, mPaint)
        }
    }

}