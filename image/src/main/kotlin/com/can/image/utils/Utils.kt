package com.can.image.utils

import android.content.Context

/**
 * Created by CAN on 19-1-5.
 * 一些公共方法
 */
fun dp2px(context: Context, dp: Float): Int {
    return (getDensity(context) * dp + 0.5f).toInt()
}

fun sp2px(context: Context, sp: Float): Int {
    return (getFontDensity(context) * sp + 0.5f).toInt()
}

private fun getDensity(context: Context): Float {
    return context.resources.displayMetrics.density
}

private fun getFontDensity(context: Context): Float {
    return context.resources.displayMetrics.scaledDensity
}