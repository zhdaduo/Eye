package com.mor.eye.util

import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.v4.graphics.drawable.DrawableCompat

object DrawableUtils {
    fun tintDrawable(drawable: Drawable, tint: Int): Drawable {
        var drawable = drawable
        drawable = DrawableCompat.wrap(drawable)
        DrawableCompat.setTint(drawable, tint)
        DrawableCompat.setTintMode(drawable, PorterDuff.Mode.SRC_ATOP)

        return drawable
    }
}