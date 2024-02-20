package com.buyehou.demo.ui

import android.content.Context
import android.graphics.Color
import android.util.AttributeSet
import androidx.appcompat.widget.AppCompatImageView
import com.buyehou.demo.drawable.BaseEffectDrawable
import com.buyehou.demo.drawable.SurroundEffectDrawable

/**
 * @author buyehou
 */
class EffectView @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {

    private var mPaintColor = Color.parseColor("#CABFA3")
    private var mPaintColors: IntArray? = null


    private var mDrawable: BaseEffectDrawable? = null

    private fun initDrawable(drawable: BaseEffectDrawable) {
        mDrawable = drawable
        setImageDrawable(mDrawable)
        setColor()
    }

    fun setSurroundEffectDrawable() {
        initDrawable(SurroundEffectDrawable(context))
    }

    fun setColor() {
        mDrawable?.setColor(mPaintColor)
    }

    fun setColor(color: Int) {
        mPaintColor = color
        mDrawable?.setColor(color)
    }

//    fun setColors(colors: IntArray) {
//        mPaintColors = colors
//        if (mDrawable != null) {
//            mDrawable.setColors(colors)
//        }
//    }

    fun onCall(data: ByteArray) {
        mDrawable?.onCall(data)
    }

    fun onWaveCall(data: ByteArray) {
        mDrawable?.onWaveCall(data)
    }

}