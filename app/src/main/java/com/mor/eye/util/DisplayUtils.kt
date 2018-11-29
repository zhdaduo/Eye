package com.mor.eye.util

import android.content.Context
import android.content.res.Resources
import android.graphics.drawable.Drawable
import android.support.annotation.ColorRes
import android.support.annotation.DrawableRes
import android.support.v4.content.ContextCompat
import android.util.DisplayMetrics
import android.view.WindowManager

object DisplayUtils {
    fun getDisplayWidth(context: Context): Int {
        val windowManager = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        val displayMetrics = DisplayMetrics()
        windowManager.defaultDisplay.getMetrics(displayMetrics)
        return displayMetrics.widthPixels
    }

    fun dip2px(context: Context, dpValue: Float): Int {
        val scale = context.resources.displayMetrics.density
        return (dpValue * scale + 0.5f).toInt()
    }

    fun getColor(context: Context, @ColorRes color: Int): Int {
        return ContextCompat.getColor(context, color)
    }

    fun getDrawable(context: Context, @DrawableRes drawable: Int): Drawable {
        return ContextCompat.getDrawable(context, drawable)!!
    }

    @JvmStatic
    fun dpToPx(context: Context, dp: Int): Int {
        val metrics = Resources.getSystem().displayMetrics
        val px = dp * (metrics.density)
        return Math.round(px)
    }

    @JvmStatic
    fun dpToPx(context: Context, dp: Float): Int {
        val metrics = Resources.getSystem().displayMetrics
        val px = dp * (metrics.density)
        return Math.round(px)
    }
}