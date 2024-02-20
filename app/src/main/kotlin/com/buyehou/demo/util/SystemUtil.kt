package com.buyehou.demo.util

import android.content.Context

/**
 * @author buyehou
 */
object SystemUtil {
    fun dip2px(context: Context, dipValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dipValue * scale + 0.5f).toInt()
    }
}