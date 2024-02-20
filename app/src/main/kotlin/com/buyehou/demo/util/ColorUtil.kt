package com.buyehou.demo.util

/**
 * @author buyehou
 */
object ColorUtil {

    fun getColors(color: Int, count: Int, offset: Int): IntArray {
        val colors = IntArray(count)
        val a = color shr 24 and 0xff
        val r = color shr 16 and 0xff
        val g = color shr 8 and 0xff
        val b = color and 0xff
        val index: Int
        var c = r
        if (g > b) {
            if (r > g) {
                index = 0
                c = r
            } else {
                index = 1
                c = g
            }
        } else {
            if (r > b) {
                index = 0
                c = r
            } else {
                c = b
                index = 2
            }
        }
        for (i in 0 until count) {
            val temp = (c + offset * (i - count / 2)) % 0XFF
            if (i == 1) {
                colors[i] = createColor(a, r, temp, b)
            } else if (i == 2) {
                colors[i] = createColor(a, r, g, temp)
            } else {
                colors[i] = createColor(a, temp, g, b)
            }
        }
        return colors
    }

    private fun createColor(a: Int, r: Int, g: Int, b: Int): Int {
        return a shl 24 or
                (r shl 16) or
                (g shl 8) or
                b
    }

    fun setAlpha(color: Int, alpha: Int): Int {
//        int a = (color >> 24) & 0xff;
        val r = color shr 16 and 0xff
        val g = color shr 8 and 0xff
        val b = color and 0xff
        return createColor(alpha, r, g, b)
    }

}