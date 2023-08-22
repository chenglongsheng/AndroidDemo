@file:JvmName("ViewExtensions")

package com.loong.widget.utils

import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.ColorRes

@ColorInt
fun View.getColor(@ColorRes colorRes: Int): Int = context.resources.getColor(colorRes, null)