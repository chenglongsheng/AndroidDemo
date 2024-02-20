package com.buyehou.demo.drawable

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.ColorFilter
import android.graphics.Paint
import android.graphics.PixelFormat
import android.graphics.drawable.Drawable
import android.util.Log
import android.util.TypedValue
import com.buyehou.demo.util.ColorUtil
import kotlin.math.abs

/**
 * @author buyehou
 */
abstract class BaseEffectDrawable(private val context: Context) : Drawable() {

    companion object {
        const val LUMP_OFFSET = 4
        const val LUMP_COUNT = 360 / LUMP_OFFSET

        /**
         * 预处理数据
         *
         * @return
         */
        protected fun readyData(fft: ByteArray): ByteArray {
            val newData = ByteArray(LUMP_COUNT)
            var abs: Byte
            for (i in 0 until LUMP_COUNT) {
                abs = abs(fft[i].toInt()).toByte()
                //描述：Math.abs -128时越界
                newData[i] = if (abs < 0) 127 else abs
            }
            return newData
        }
    }

    protected var mCount = LUMP_COUNT
    protected var mCountOffset = LUMP_OFFSET
    protected var mPaint: Paint = Paint()
    protected var mPaintColor = Color.parseColor("#CABFA3")
    protected var mPaintColors: IntArray? = null
    protected var mRadius = 113

    protected var mData: ByteArray? = null
    protected var mIsDrawing = false

    init {
        mPaint.isAntiAlias = true
        mPaint.color = mPaintColor
        mPaint.style = Paint.Style.FILL
        mPaint.strokeWidth = 5f

        mRadius = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            mRadius.toFloat(),
            context.resources.displayMetrics
        ).toInt()
    }

    open fun setColor(color: Int) {
        mPaintColor = color
        mPaint.color = mPaintColor
        mPaintColors = ColorUtil.getColors(color, 4, 30)
        invalidateSelf()
    }

    open fun onCall(data: ByteArray) {
        if (mIsDrawing) {
            return
        }
        Log.e("yijunwu", data[0].toString() + " data.length=" + data.size)
        mData = readyData(data)
        invalidateSelf()
    }

    protected open fun getColor(i: Int): Int {
        return if (mPaintColors != null && i < mPaintColors!!.size) {
            mPaintColors!![i]
        } else {
            mPaintColor
        }
    }

    open fun onWaveCall(data: ByteArray?) {}

    override fun draw(canvas: Canvas) {
    }

    override fun setAlpha(alpha: Int) {
    }

    override fun setColorFilter(colorFilter: ColorFilter?) {
    }

    override fun getOpacity(): Int {
        return PixelFormat.UNKNOWN
    }
}