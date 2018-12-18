package com.mor.eye.util.databinding

import android.content.Context
import android.databinding.BindingAdapter
import android.graphics.PorterDuff
import android.graphics.drawable.Drawable
import android.support.annotation.DrawableRes
import android.widget.ProgressBar
import android.widget.TextView
import com.mor.eye.util.other.VersionsUtils

/**
 * Kotlin Extension for set/get compound Drawable to TextView/EditText/Button/ProgressBar as property
 * Pre-Lollipop : vectorDrawables.useSupportLibrary = true
 */

@BindingAdapter("drawableColor")
fun setDrawableColor(view: TextView, color: Int) {
    view.drawableStart?.apply { tint(color) }
    view.drawableEnd?.apply { tint(color) }
    view.drawableTop?.apply { tint(color) }
    view.drawableBottom?.apply { tint(color) }
}

@BindingAdapter("progressColor")
fun setProgressBarColor(loader: ProgressBar?, color: Int) {
    loader?.indeterminateDrawable?.setColorFilter(color, android.graphics.PorterDuff.Mode.SRC_IN)
}

fun Context.drawable(@DrawableRes id: Int): Drawable {
    return if (VersionsUtils.isLollipop()) {
        resources.getDrawable(id, null)
    } else {
        @Suppress("DEPRECATION")
        resources.getDrawable(id)
    }
}

fun Drawable.tint(color: Int, mode: PorterDuff.Mode = PorterDuff.Mode.SRC_IN) {
    mutate().setColorFilter(color, mode)
}

var TextView.drawableStart: Drawable?
    get() = drawables[0]
    set(value) = setDrawables(value, drawableTop, drawableEnd, drawableBottom)

var TextView.drawableTop: Drawable?
    get() = drawables[1]
    set(value) = setDrawables(drawableStart, value, drawableEnd, drawableBottom)

var TextView.drawableEnd: Drawable?
    get() = drawables[2]
    set(value) = setDrawables(drawableStart, drawableTop, value, drawableBottom)

var TextView.drawableBottom: Drawable?
    get() = drawables[3]
    set(value) = setDrawables(drawableStart, drawableTop, drawableEnd, value)

private val TextView.drawables: Array<Drawable?>
    get() = if (VersionsUtils.isJellyBeanMR1()) compoundDrawablesRelative else compoundDrawables

private fun TextView.setDrawables(start: Drawable?, top: Drawable?, end: Drawable?, bottom: Drawable?) {
    if (VersionsUtils.isJellyBeanMR1())
        setCompoundDrawablesRelativeWithIntrinsicBounds(start, top, end, bottom)
    else
        setCompoundDrawablesWithIntrinsicBounds(start, top, end, bottom)
}