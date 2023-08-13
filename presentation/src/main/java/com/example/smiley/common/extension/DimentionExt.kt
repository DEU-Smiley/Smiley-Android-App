package com.example.smiley.common.extension

import android.content.Context
import android.util.DisplayMetrics
import android.util.TypedValue

fun Int.dpToPx(context: Context): Int {
    return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, this.toFloat(), context.resources.displayMetrics).toInt()
}

fun Int.pxToDp(context: Context): Int {
    val resources = context.resources
    val metrics = resources.displayMetrics
    return this / (metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT)
}